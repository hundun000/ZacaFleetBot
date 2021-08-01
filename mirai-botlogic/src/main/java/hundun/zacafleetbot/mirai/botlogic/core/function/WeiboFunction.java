package hundun.zacafleetbot.mirai.botlogic.core.function;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.SettingManager;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.BlackBoard;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.ProcessResult;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.CharacterPublicSettings;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.GroupConfig;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.SubFunctionCallStatement;
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.WeiboService;
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.WeiboService.WeiboCardCacheAndImage;
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.domain.WeiboCardCache;
import hundun.zacafleetbot.mirai.botlogic.cp.weibo.domain.WeiboViewFormat;
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
    
    //Map<String, List<String>> characterIdToBlogUids = new HashMap<>();
    Map<Long, SessionData> sessionDataMap = new HashMap<>();
    
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    @PostConstruct
    public void manualWired() {
        
        registerSchedule();
    }
    
    @Data
    private class SessionData {
        Map<String, LocalDateTime> lastCheckTimes = new HashMap<>();
          
    }
    
    
    public Long lastAsk = Long.valueOf(0);
    
    public int lastBlogHash = -1;
    
    

    
    private Map<String, WeiboViewFormat> getWeiboSettingByGroupId(List<String> characterIds) {
        Map<String, WeiboViewFormat> allData = new HashMap<>();
        
        for (String characterId : characterIds) {
            CharacterPublicSettings settings = settingManager.getCharacterPublicSettings(characterId);
            if (settings != null) {
                Map<String, WeiboViewFormat> dataOfCharacter = settings.getWeibo();
                if (dataOfCharacter != null) {
                    allData.putAll(dataOfCharacter);
                }
            }
        }
        
        return allData;
    }
    
    private ProcessResult sendSumaryToBot(Collection<String> blogUids, Bot bot, long groupId) {

        StringBuilder builder = new StringBuilder();
        for (String blogUid : blogUids) {
            String firstBlog = weiboService.getFirstBlogInfo(blogUid);
            if (firstBlog != null) {
                builder.append(firstBlog).append("\n");
            }
        }
        if (builder.length() == 0) {
            console.sendToGroup(bot, groupId, "现在还没有饼哦~");
        } else {
            console.sendToGroup(bot, groupId, builder.toString());
        }
        return new ProcessResult(this, true);
    }
    
    private void sendBlogToBot(WeiboCardCacheAndImage newCardCacheAndImage, Bot bot, long groupId) {
        if (newCardCacheAndImage == null) {
            console.sendToGroup(bot, groupId, "现在还没有饼哦~");
            return;
        }
        
        WeiboCardCache newBlog = newCardCacheAndImage.getWeiboCardCache();
        MessageChain chain = MessageUtils.newChain();
        
        chain = chain.plus(new PlainText("新饼！来自：" + newBlog.getScreenName() + "\n\n"));
        
        if (newBlog.getBlogTextDetail() != null) {
            chain = chain.plus(new PlainText(newBlog.getBlogTextDetail()));   
        }
        
        for (File imageFile : newCardCacheAndImage.getImages()) {
            ExternalResource externalResource = ExternalResource.create(imageFile);
            Image image = console.uploadImage(bot, groupId, externalResource);
            chain = chain.plus(image);
        }
        
        if (!newCardCacheAndImage.getImageUrls().isEmpty()) {
            StringBuilder builder = new StringBuilder();
            builder.append("\n以及" + newCardCacheAndImage.getImageUrls().size() + "张图片。");
//            for (String url : newBlog.getPicsLargeUrls()) {
//                builder.append(url).append("\n");
//            }
            chain = chain.plus(new PlainText(builder.toString()));       
        }

        console.sendToGroup(bot, groupId, chain);
    }
    
    
    private int forceUpdateUserCacheCount = 0;
    
    public void registerSchedule() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                console.getLogger().info("checkNewBlog Scheduled arrival");
                try {
                    Collection<Bot> bots = console.getBots();
                    for (Bot bot: bots) {
                        for (GroupConfig entry : settingManager.getGroupConfigsOrEmpty(bot.getId())) {
                            Long groupId = entry.getGroupId();
                            Map<String, WeiboViewFormat> listenDataMap = getWeiboSettingByGroupId(entry.getEnableCharacters());
                            if (!sessionDataMap.containsKey(groupId)) {
                                sessionDataMap.put(groupId, new SessionData());
                            }
                            SessionData sessionData = sessionDataMap.get(groupId);
                            
                            File cacheFolder = console.resolveDataFileOfFileCache();
                            for (Entry<String, WeiboViewFormat> listenDataEntry : listenDataMap.entrySet()) {
                                String blogUid = listenDataEntry.getKey();
                                LocalDateTime lastCheckTimeOfThisBlogUid = sessionData.getLastCheckTimes().get(blogUid);
                                if (lastCheckTimeOfThisBlogUid == null) {
                                    lastCheckTimeOfThisBlogUid = LocalDateTime.now();
                                }
                                WeiboViewFormat format = listenDataEntry.getValue();
                                
                                forceUpdateUserCacheCount++;
                                if (forceUpdateUserCacheCount % 200 == 0) {
                                    weiboService.updateAndGetUserInfoCache(blogUid, true);
                                }
                                
                                List<WeiboCardCacheAndImage> cardCacheAndImages = weiboService.updateAndGetTopBlog(blogUid, cacheFolder, format);
                                List<WeiboCardCacheAndImage> newCardCacheAndImages = new ArrayList<>();
                                List<LocalDateTime> oldCardTimes = new ArrayList<>();
                                for (WeiboCardCacheAndImage cardCacheAndImage : cardCacheAndImages) {
                                    boolean isNew = cardCacheAndImage.getWeiboCardCache().getBlogCreatedDateTime().isAfter(lastCheckTimeOfThisBlogUid);
                                    //boolean isNew = true;
                                    if (isNew) {
                                        newCardCacheAndImages.add(cardCacheAndImage);
                                    } else {
                                        oldCardTimes.add(cardCacheAndImage.getWeiboCardCache().getBlogCreatedDateTime());
                                    }
                                }
//                                console.getLogger().info("blogUid = " + blogUid
//                                        + "has " + newCardCacheAndImages.size()
//                                        + " newCards, has oldCards: " + oldCardTimes.toString());
                                console.getLogger().debug("groupId = " + groupId 
                                        + ", checkingBlogUid = " + blogUid 
                                        + ", lastCheckTimeOfThisBlogUid = " + lastCheckTimeOfThisBlogUid
                                        + ", newCard.size = " + newCardCacheAndImages.size() 
                                        );
                                sessionData.getLastCheckTimes().put(blogUid, LocalDateTime.now());
                                for (WeiboCardCacheAndImage newCardCacheAndImage : newCardCacheAndImages) {
                                    sendBlogToBot(newCardCacheAndImage, bot, groupId);
                                }
                                
                                
                            }
                            
                        }
                    }
                } catch (Exception e) {
                    console.getLogger().error("checkNewBlog Scheduled error: ", e);
                }
            }
        }, 5, 5, TimeUnit.MINUTES);
        
        
        
    }




    @Override
    public ProcessResult process(BlackBoard blackBoard) {
        SessionId sessionId = blackBoard.getSessionId(); 
        EventInfo event = blackBoard.getEvent(); 
        Statement statement = blackBoard.getStatement();
        if (statement instanceof SubFunctionCallStatement) {
            SubFunctionCallStatement subFunctionCallStatement = (SubFunctionCallStatement)statement;
            if (subFunctionCallStatement.getSubFunction() == SubFunction.WEIBO_SHOW_LATEST) {
                long now = System.currentTimeMillis();
                long time = now - lastAsk;
                long limit = 10 * 1000;
                if (time > limit) {
                    lastAsk = now;
                    Map<String, WeiboViewFormat> listenDataMap = getWeiboSettingByGroupId(Collections.singletonList(sessionId.getCharacterId()));
                    //List<String> blogUids = characterIdToBlogUids.get(sessionId.getCharacterId());
//                    if (blogUids == null) {
//                        return new ProcessResult(this, false);
//                    }
                    if (subFunctionCallStatement.getArgs().size() > 0) {
                        String targetUserName = subFunctionCallStatement.getArgs().get(0);
                        String targetUid = weiboService.getUidByUserName(targetUserName);
                        if (targetUid == null || !listenDataMap.containsKey(targetUid)) {
                            console.sendToGroup(event.getBot(), event.getGroupId(), "你还没有订阅：" + targetUserName);
                        } else {
                            File cacheFolder = console.resolveDataFileOfFileCache();
                            WeiboViewFormat format = listenDataMap.get(targetUid);
                            
                            WeiboCardCacheAndImage cardCacheAndImage = weiboService.getFirstBlogByUserName(targetUserName, 0, cacheFolder, format);
                            sendBlogToBot(cardCacheAndImage, event.getBot(), event.getGroupId());
                        }
                        
                    } else {
                        sendSumaryToBot(listenDataMap.keySet(), event.getBot(), event.getGroupId());
                    }
                } else {
                    console.sendToGroup(event.getBot(), event.getGroupId(), "刚刚已经看过了，晚点再来吧~");
                }
                return new ProcessResult(this, true);
                
                
                
                
            } else if (subFunctionCallStatement.getSubFunction() == SubFunction.WEIBO_RESET_CHECK_TIME_FOR_DEBUG) {
                long hourOffset = Integer.valueOf(subFunctionCallStatement.getArgs().get(0));
                SessionData sessionData = sessionDataMap.get(event.getGroupId());
                for (Entry<String, LocalDateTime> entry : sessionData.getLastCheckTimes().entrySet()) {
                    entry.setValue(entry.getValue().plusHours(hourOffset));
                }
                console.sendToGroup(event.getBot(), event.getGroupId(), "已修改");
                return new ProcessResult(this, true);
            }


        }
        return new ProcessResult(this, false);
    }

    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList(SubFunction.WEIBO_SHOW_LATEST, SubFunction.WEIBO_RESET_CHECK_TIME_FOR_DEBUG);
    }
    
}
