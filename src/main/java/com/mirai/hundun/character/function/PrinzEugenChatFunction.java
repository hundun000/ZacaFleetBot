package com.mirai.hundun.character.function;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.parser.statement.AtStatement;
import com.mirai.hundun.parser.statement.LiteralValueStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/04/28
 */
@Slf4j
@Component
public class PrinzEugenChatFunction implements IFunction {

    @Autowired
    BotService botService;
    
    private File pupuImage;
    ExternalResource pupuExternalResource;
    
    public PrinzEugenChatFunction() {
        try {
            pupuImage = new File("./data/images/chat/噗噗.jpg");
            pupuExternalResource = ExternalResource.create(pupuImage);
        } catch (Exception e) {
            log.error("open cannotRelaxImage error: {}", e.getMessage());
        }
        
    }
    
    @Override
    public boolean acceptStatement(String sessionId, GroupMessageEvent event, Statement statement) {
        if (statement instanceof LiteralValueStatement) {
            String newMessage = ((LiteralValueStatement)statement).getValue();
            if (newMessage.contains("噗噗")) {
                botService.sendToEventSubject(event, 
                        new PlainText("")
                        .plus(event.getGroup().uploadImage(pupuExternalResource))
                        );
                return true;
            }
        }
        return false;
    }

}
