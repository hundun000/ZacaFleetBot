package com.mirai.hundun.bot.amiya;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mirai.hundun.bot.Amiya;
import com.mirai.hundun.bot.IPlainTextHandler;
import com.mirai.hundun.cp.weibo.WeiboService;
import com.mirai.hundun.cp.weibo.domain.WeiboCardCache;
import com.mirai.hundun.service.BotService;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
@Component
public class AmiyaWeiboHandler implements IPlainTextHandler{
    
    
    
    @Autowired
    WeiboService weiboService;
    
    @Autowired
    Amiya parent;
    
    
    public Long lastAsk = Long.valueOf(0);
    
    public int lastBlogHash = -1;
    
    @Scheduled(fixedDelay = 3 * 60 * 1000)
    public void checkNewBlog() {
        log.info("checkNewBlog");
        List<WeiboCardCache> newBlogs = weiboService.updateBlog(weiboService.yjUid);
        for (WeiboCardCache newBlog : newBlogs) {
            String firstBlog = "新饼！\n\n" + newBlog.getMblog_textDetail();
            parent.sendToArknightsGroup(firstBlog);
        }
    }



    @Override
    public boolean acceptPlainText(GroupMessageEvent event, PlainText plainText) {
        String newMessage = plainText != null ? plainText.contentToString() : null;
        
        if (newMessage == null) {
            return false;
        }
        
        if (newMessage.equals("看看饼")) {

            long now = System.currentTimeMillis();
            long time = now - lastAsk;
            if (time > 5 * 1000) {
                lastAsk = now;
                String firstBlog = weiboService.getFirstBlogInfo(weiboService.yjUid);
                if (firstBlog != null) {
                    event.getSubject().sendMessage(firstBlog);
                } else {
                    event.getSubject().sendMessage("现在还没有饼哦~");
                }
            } else {
                event.getSubject().sendMessage("刚刚已经看过了，晚点再来吧~");
            }
            return true;
        }
        
        return false;
    }
    
}
