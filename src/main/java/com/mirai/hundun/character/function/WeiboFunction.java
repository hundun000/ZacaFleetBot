package com.mirai.hundun.character.function;

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
import net.mamoe.mirai.message.data.PlainText;

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
    
    //@Autowired
    //Amiya parent;
    
    @Autowired
    BotService botService;
    
    
    Map<Long, SessionData> groupIdToData = new HashMap<>();
    
    @Getter
    private class SessionData {
        List<String> blogUids = new ArrayList<>();
    }
    
    
    public Long lastAsk = Long.valueOf(0);
    
    public int lastBlogHash = -1;
    
    public void putGroupData(Long groupId, List<String> blogUids) {
        SessionData sessionData = new SessionData();
        sessionData.blogUids = blogUids;
        this.groupIdToData.put(groupId, sessionData);
    }
    
    @Scheduled(fixedDelay = 3 * 60 * 1000)
    public void checkNewBlog() {
        log.info("checkNewBlog");
        
        for (Entry<Long, SessionData> entry : groupIdToData.entrySet()) {
            Long groupId = entry.getKey();
            List<String> blogUids = entry.getValue().getBlogUids();
            for (String blogUid : blogUids) {
                List<WeiboCardCache> newBlogs = weiboService.updateBlog(blogUid);
                for (WeiboCardCache newBlog : newBlogs) {
                    String firstBlog = "新饼！来自：" + newBlog.getScreenName() + "\n\n" + newBlog.getMblog_textDetail();
                    botService.sendToGroup(groupId, firstBlog);
                }
            }
        }
        
    }




    @Override
    public boolean acceptStatement(GroupMessageEvent event, Statement statement) {
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
                SessionData sessionData = groupIdToData.get(groupId);
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
