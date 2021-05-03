package com.mirai.hundun.character.function;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mirai.hundun.cp.quiz.Question;
import com.mirai.hundun.cp.weibo.WeiboService;
import com.mirai.hundun.cp.weibo.domain.WeiboCardCache;
import com.mirai.hundun.parser.statement.FunctionCallStatement;
import com.mirai.hundun.parser.statement.LiteralValueStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;

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
    
    public String functionName = "看看饼";
    
    @Autowired
    WeiboService weiboService;
    
    
    @Autowired
    BotService botService;
    
    Map<Long, String> groupIdToCharacterId = new HashMap<>();
    Map<String, SessionData> characterIdToData = new HashMap<>();
    
    @Getter
    private class SessionData {
        List<String> blogUids = new ArrayList<>();
    }
    
    
    public Long lastAsk = Long.valueOf(0);
    
    public int lastBlogHash = -1;
    
    public void putGroupIdToCharacter(Long groupId, String characterId) {
        this.groupIdToCharacterId.put(groupId, characterId);
        log.info("groupId = {} use characterId = {}", groupId, characterId);
    }
    
    public void putCharacterToData(String characterId, List<String> blogUids) {
        SessionData sessionData = new SessionData();
        sessionData.blogUids = blogUids;
        this.characterIdToData.put(characterId, sessionData);
        log.info("characterId = {} listening: {}", characterId, blogUids);
    }
    
    private SessionData getDataByGroupId(Long groupId) {
        String characterId = groupIdToCharacterId.get(groupId);
        if (characterId != null) {
            SessionData sessionData = characterIdToData.get(characterId);
            return sessionData;
        }
        return null;
    }
    
    @Scheduled(fixedDelay = 3 * 60 * 1000)
    public void checkNewBlog() {
        log.info("checkNewBlog");
        
        for (Entry<Long, String> entry : groupIdToCharacterId.entrySet()) {
            Long groupId = entry.getKey();
            String characterId = entry.getValue();
            SessionData sessionData = characterIdToData.get(characterId);
            if (sessionData != null) {
                List<String> blogUids = sessionData.getBlogUids();
                for (String blogUid : blogUids) {
                    List<WeiboCardCache> newBlogs = weiboService.updateBlog(blogUid);
                    for (WeiboCardCache newBlog : newBlogs) {
                        MessageChain chain = MessageUtils.newChain();
                        
                        chain = chain.plus(new PlainText("新饼！来自：" + newBlog.getScreenName() + "\n"));
                        
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
            }
        }
        
    }




    @Override
    public boolean acceptStatement(String sessionId, GroupMessageEvent event, Statement statement) {
        if (statement instanceof FunctionCallStatement) {
            FunctionCallStatement functionCallStatement = (FunctionCallStatement)statement;
            if (!functionCallStatement.getFunctionName().equals(this.functionName)) {
                return false;
            }

            long now = System.currentTimeMillis();
            long time = now - lastAsk;
            if (time > 5 * 1000) {
                lastAsk = now;
                Long groupId = event.getGroup().getId();
                SessionData sessionData = getDataByGroupId(groupId);
                if (sessionData == null) {
                    return false;
                }
                StringBuilder builder = new StringBuilder();
                for (String blogUid : sessionData.getBlogUids()) {
                    String firstBlog = weiboService.getFirstBlogInfo(blogUid);
                    if (firstBlog != null) {
                        builder.append(firstBlog).append("\n");
                    }
                }
                if (builder.length() == 0) {
                    botService.sendToEventSubject(event, "现在还没有饼哦~");
                } else {
                    botService.sendToEventSubject(event, builder.toString());
                }
            } else {
                botService.sendToEventSubject(event, "刚刚已经看过了，晚点再来吧~");
            }
            return true;
        }
        return false;
    }
    
}
