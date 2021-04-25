package com.mirai.hundun.bot;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/21
 */
@Slf4j
public class RepeatConsumer implements IPlainTextHandler {

    
    CountNode countNode;
    
    //private final long groupId;

    
    public class CountNode {
        public String message = "";
        public int count = 0;
    }
    public class A {
        
    }
    
    public RepeatConsumer(long groupId) {
        countNode = new CountNode();
        //this.groupId = groupId;
    }

    @Override
    public boolean acceptPlainText(GroupMessageEvent event, PlainText plainText) {
        String newMessage = plainText != null ? plainText.contentToString() : null;
        
        if (newMessage == null) {
            return false;
        }
        
        
            
        if (countNode.message.equals(newMessage)) {
            countNode.count++;
        } else {
            countNode.count = 1;
            countNode.message = newMessage;
        }

        
        if (countNode.count == 3) {
            //countNode.count = 0;
            event.getSubject().sendMessage(countNode.message);
            //log.info("RepeatConsumer update sendMessage, groupId = {}, sendMessage = {}", groupId, countNode.message);
        }
            
            
            //log.info("RepeatConsumer update Count, groupId = {}, sneder = {}, newCount = {}", groupId, countNode.count);
        return true;
    }

}
