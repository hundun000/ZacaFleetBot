package com.mirai.hundun.function;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.GroupConfig;
import com.mirai.hundun.cp.weibo.WeiboService;
import com.mirai.hundun.cp.weibo.domain.WeiboCardCache;
import com.mirai.hundun.parser.statement.FunctionCallStatement;
import com.mirai.hundun.parser.statement.LiteralValueStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;
import com.mirai.hundun.service.CharacterRouter;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
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
@Component
public class WeiboFunction implements IFunction {
    
    
    
    @Autowired
    WeiboService weiboService;
    
    
    @Autowired
    BotService botService;
    
    @Autowired
    CharacterRouter characterRouter;
    
    Map<String, List<String>> characterIdToBlogUids = new HashMap<>();
    Map<Long, SessionData> groupIdToSessionData = new HashMap<>();
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
    
    @Scheduled(fixedDelay = 5 * 60 * 1000)
    public void checkNewBlog() {
        log.info("checkNewBlog Scheduled arrival");
        
        for (GroupConfig entry : characterRouter.getGroupConfigs().values()) {
            Long groupId = entry.getGroupId();
            Set<String> blogUids = getDataByGroupId(entry.getEnableCharacters());
            if (!groupIdToSessionData.containsKey(groupId)) {
                groupIdToSessionData.put(groupId, new SessionData());
            }
            SessionData sessionData = groupIdToSessionData.get(groupId);
            log.info("groupId = {}, LastUpdateTime = {}, checkNewBlog: {}", groupId, sessionData.getLastUpdateTime(), blogUids);
            
            for (String blogUid : blogUids) {
                List<WeiboCardCache> topBlogs = weiboService.updateAndGetTopBlog(blogUid);
                List<WeiboCardCache> newBlogs = new ArrayList<>(0);
                for (WeiboCardCache blog : topBlogs) {
                    if (blog.getMblogCreatedDateTime().isAfter(sessionData.getLastUpdateTime())) {
                        newBlogs.add(blog);
                    }
                }
                
                for (WeiboCardCache newBlog : newBlogs) {

                    MessageChain chain = MessageUtils.newChain();
                    
                    chain = chain.plus(new PlainText("新饼！来自：" + newBlog.getScreenName() + "\n\n"));
                    
                    if (newBlog.getMblog_textDetail() != null) {
                        chain = chain.plus(new PlainText(newBlog.getMblog_textDetail()));   
                    }
                    
                    if (newBlog.getSinglePicture() != null) {
                        ExternalResource externalResource = ExternalResource.create(newBlog.getSinglePicture());
                        Image image = botService.uploadImage(groupId, externalResource);
                        chain = chain.plus(image);
                    } else if (newBlog.getPicsLargeUrls() != null && !newBlog.getPicsLargeUrls().isEmpty()) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("\n图片资源：\n");
                        for (String url : newBlog.getPicsLargeUrls()) {
                            builder.append(url).append("\n");
                        }
                        chain = chain.plus(new PlainText(builder.toString()));       
                    }
                    
                    
                        
                        
                    botService.sendToGroup(groupId, chain);

                }

            }
            
            sessionData.setLastUpdateTime(LocalDateTime.now());
            
        }
        
    }




    @Override
    public boolean acceptStatement(String sessionId, EventInfo event, Statement statement) {
        if (statement instanceof FunctionCallStatement) {
            FunctionCallStatement functionCallStatement = (FunctionCallStatement)statement;
            if (functionCallStatement.getSubFunction() != SubFunction.WEIBO_SHOW_LATEST) {
                return false;
            }

            long now = System.currentTimeMillis();
            long time = now - lastAsk;
            if (time > 5 * 1000) {
                lastAsk = now;
                
                List<String> blogUids = characterIdToBlogUids.get(event.getCharacterId());
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
                    botService.sendToGroup(event.getGroupId(), "现在还没有饼哦~");
                } else {
                    botService.sendToGroup(event.getGroupId(), builder.toString());
                }
            } else {
                botService.sendToGroup(event.getGroupId(), "刚刚已经看过了，晚点再来吧~");
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
