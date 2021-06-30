package com.hundun.mirai.bot.core.function;

import java.io.File;
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

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundun.mirai.bot.core.SettingManager;
import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.SessionId;
import com.hundun.mirai.bot.core.data.configuration.GroupConfig;
import com.hundun.mirai.bot.core.parser.statement.Statement;
import com.hundun.mirai.bot.core.parser.statement.SubFunctionCallStatement;
import com.hundun.mirai.bot.cp.weibo.WeiboService;
import com.hundun.mirai.bot.cp.weibo.WeiboService.WeiboCardCacheAndImage;
import com.hundun.mirai.bot.cp.weibo.domain.WeiboCardCache;
import lombok.Data;
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
@Component
public class WeiboFunction extends BaseFunction {
    
    @Autowired
    WeiboService weiboService;
        
    @Autowired
    SettingManager settingManager;
    
    Map<String, List<String>> characterIdToBlogUids = new HashMap<>();
    Map<Long, SessionData> groupIdToSessionData = new HashMap<>();
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    @PostConstruct
    public void manualWired() {

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
        //console.getLogger().info("characterId = " + characterId + " listening: " + blogUids);
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
            Image image = console.uploadImage(bot, groupId, externalResource);
            chain = chain.plus(image);
        } else if (newBlog.getPicsLargeUrls() != null && !newBlog.getPicsLargeUrls().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("\n图片资源：\n");
            for (String url : newBlog.getPicsLargeUrls()) {
                builder.append(url).append("\n");
            }
            chain = chain.plus(new PlainText(builder.toString()));       
        }

        console.sendToGroup(bot, groupId, chain);
    }
    
    public void registerSchedule() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                console.getLogger().info("checkNewBlog Scheduled arrival");
                
                Collection<Bot> bots = console.getBots();
                for (Bot bot: bots) {
                    for (GroupConfig entry : settingManager.getGroupConfigsOrEmpty(bot.getId())) {
                        Long groupId = entry.getGroupId();
                        Set<String> blogUids = getDataByGroupId(entry.getEnableCharacters());
                        if (!groupIdToSessionData.containsKey(groupId)) {
                            groupIdToSessionData.put(groupId, new SessionData());
                        }
                        SessionData sessionData = groupIdToSessionData.get(groupId);
                        console.getLogger().info("groupId = " + groupId + ", LastUpdateTime = " + sessionData.getLastUpdateTime() + ", checkNewBlog: " + blogUids);
                        File cacheFolder = console.resolveDataFileOfFileCache();
                        for (String blogUid : blogUids) {
                            
                            List<WeiboCardCacheAndImage> cardCacheAndImages = weiboService.updateAndGetTopBlog(blogUid, cacheFolder);
                            List<WeiboCardCacheAndImage> newCardCacheAndImages = new ArrayList<>();
                            List<LocalDateTime> oldCardTimes = new ArrayList<>();
                            for (WeiboCardCacheAndImage cardCacheAndImage : cardCacheAndImages) {
                                boolean isNew = cardCacheAndImage.getWeiboCardCache().getMblogCreatedDateTime().isAfter(sessionData.getLastUpdateTime());
                                //boolean isNew = true;
                                if (isNew) {
                                    newCardCacheAndImages.add(cardCacheAndImage);
                                } else {
                                    oldCardTimes.add(cardCacheAndImage.getWeiboCardCache().getMblogCreatedDateTime());
                                }
                            }
                            console.getLogger().info("blogUid = " + blogUid
                                    + "has " + newCardCacheAndImages.size()
                                    + " newCards, has oldCards: " + oldCardTimes.toString());
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
                    console.sendToGroup(event.getBot(), event.getGroupId(), "现在还没有饼哦~");
                } else {
                    console.sendToGroup(event.getBot(), event.getGroupId(), builder.toString());
                }
            } else {
                console.sendToGroup(event.getBot(), event.getGroupId(), "刚刚已经看过了，晚点再来吧~");
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
