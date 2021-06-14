package com.hundun.mirai.bot.core.function;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hundun.mirai.bot.core.CharacterRouter;
import com.hundun.mirai.bot.core.CustomBeanFactory;
import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.SessionId;
import com.hundun.mirai.bot.core.data.configuration.GroupConfig;
import com.hundun.mirai.bot.core.parser.statement.Statement;
import com.hundun.mirai.bot.core.parser.statement.SubFunctionCallStatement;
import com.hundun.mirai.bot.cp.weibo.WeiboService;
import com.hundun.mirai.bot.cp.weibo.WeiboService.WeiboCardCacheAndImage;
import com.hundun.mirai.bot.cp.weibo.domain.WeiboCardCache;
import com.hundun.mirai.bot.export.IConsole;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageUtils;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
public class WeiboFunction implements IFunction {
    
    WeiboService weiboService;
    
    IConsole offlineConsole;
    
    CharacterRouter characterRouter;
    
    Map<String, List<String>> characterIdToBlogUids = new HashMap<>();
    Map<Long, SessionData> groupIdToSessionData = new HashMap<>();
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    @Override
    public void manualWired() {
        this.weiboService = CustomBeanFactory.getInstance().weiboService;
        this.offlineConsole = CustomBeanFactory.getInstance().console;
        this.characterRouter = CustomBeanFactory.getInstance().characterRouter;
    }
    
    @Override
    public void afterManualWired() {
        registerSchedule();
    }
    
    @Data
    private class SessionData {
        LocalDateTime lastUpdateTime;
        
        public SessionData() {
            this.lastUpdateTime = LocalDateTime.now();
        }
    }
    
    
    public Long lastAsk = Long.valueOf(0);
    
    public int lastBlogHash = -1;
    
    
    public void putCharacterToData(String characterId, List<String> blogUids) {
        this.characterIdToBlogUids.put(characterId, blogUids);
        log.info("characterId = {} listening: {}", characterId, blogUids);
    }
    
    private Set<String> getDataByGroupId(List<String> characterIds) {
        Set<String> allBlogUids = new HashSet<>();
        
        for (String characterId : characterIds) {
            List<String> blogUids = characterIdToBlogUids.get(characterId);
            if (blogUids != null) {
                allBlogUids.addAll(blogUids);
            }
        }
        
        return allBlogUids;
    }
    
    
    private void sendNewBlogByOneBot(WeiboCardCacheAndImage newCardCacheAndImage, Bot bot , long groupId) {
        WeiboCardCache newBlog = newCardCacheAndImage.getWeiboCardCache();
        MessageChain chain = MessageUtils.newChain();
        
        chain = chain.plus(new PlainText("新饼！来自：" + newBlog.getScreenName() + "\n\n"));
        
        if (newBlog.getMblog_textDetail() != null) {
            chain = chain.plus(new PlainText(newBlog.getMblog_textDetail()));   
        }
        
        if (newCardCacheAndImage.getImage() != null) {
            ExternalResource externalResource = ExternalResource.create(newCardCacheAndImage.getImage());
            Image image = offlineConsole.uploadImage(bot, groupId, externalResource);
            chain = chain.plus(image);
        } else if (newBlog.getPicsLargeUrls() != null && !newBlog.getPicsLargeUrls().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("\n图片资源：\n");
            for (String url : newBlog.getPicsLargeUrls()) {
                builder.append(url).append("\n");
            }
            chain = chain.plus(new PlainText(builder.toString()));       
        }

        offlineConsole.sendToGroup(bot, groupId, chain);
    }
    
    public void registerSchedule() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                log.info("checkNewBlog Scheduled arrival");
                
                Collection<Bot> bots = offlineConsole.getBots();
                for (Bot bot: bots) {
                    for (GroupConfig entry : characterRouter.getGroupConfigsOrEmpty(bot.getId())) {
                        Long groupId = entry.getGroupId();
                        Set<String> blogUids = getDataByGroupId(entry.getEnableCharacters());
                        if (!groupIdToSessionData.containsKey(groupId)) {
                            groupIdToSessionData.put(groupId, new SessionData());
                        }
                        SessionData sessionData = groupIdToSessionData.get(groupId);
                        log.info("groupId = {}, LastUpdateTime = {}, checkNewBlog: {}", groupId, sessionData.getLastUpdateTime(), blogUids);
                        
                        for (String blogUid : blogUids) {
                            List<WeiboCardCacheAndImage> cardCacheAndImages = weiboService.updateAndGetTopBlog(blogUid);
                            List<WeiboCardCacheAndImage> newCardCacheAndImages = new ArrayList<>(0);
                            for (WeiboCardCacheAndImage cardCacheAndImage : cardCacheAndImages) {
                                boolean isNew = cardCacheAndImage.getWeiboCardCache().getMblogCreatedDateTime().isAfter(sessionData.getLastUpdateTime());
                                //boolean isNew = true;
                                if (isNew) {
                                    newCardCacheAndImages.add(cardCacheAndImage);
                                }
                            }
                            
                            for (WeiboCardCacheAndImage newCardCacheAndImage : newCardCacheAndImages) {
                                sendNewBlogByOneBot(newCardCacheAndImage, bot, groupId);
                            }
    
                        }
                        
                        sessionData.setLastUpdateTime(LocalDateTime.now());
                        
                    }
                }
            }
        }, 5, 5, TimeUnit.MINUTES);
        
        
        
    }




    @Override
    public boolean acceptStatement(SessionId sessionId, EventInfo event, Statement statement) {
        if (statement instanceof SubFunctionCallStatement) {
            SubFunctionCallStatement subFunctionCallStatement = (SubFunctionCallStatement)statement;
            if (subFunctionCallStatement.getSubFunction() != SubFunction.WEIBO_SHOW_LATEST) {
                return false;
            }

            long now = System.currentTimeMillis();
            long time = now - lastAsk;
            if (time > 5 * 1000) {
                lastAsk = now;
                
                List<String> blogUids = characterIdToBlogUids.get(sessionId.getCharacterId());
                if (blogUids == null) {
                    return false;
                }
                StringBuilder builder = new StringBuilder();
                for (String blogUid : blogUids) {
                    String firstBlog = weiboService.getFirstBlogInfo(blogUid);
                    if (firstBlog != null) {
                        builder.append(firstBlog).append("\n");
                    }
                }
                if (builder.length() == 0) {
                    offlineConsole.sendToGroup(event.getBot(), event.getGroupId(), "现在还没有饼哦~");
                } else {
                    offlineConsole.sendToGroup(event.getBot(), event.getGroupId(), builder.toString());
                }
            } else {
                offlineConsole.sendToGroup(event.getBot(), event.getGroupId(), "刚刚已经看过了，晚点再来吧~");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList(SubFunction.WEIBO_SHOW_LATEST);
    }
    
}
