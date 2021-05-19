package com.mirai.hundun.function;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.SessionId;
import com.mirai.hundun.parser.statement.LiteralValueStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.Voice;
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
    

    ExternalResource pupuExternalResource;
    ExternalResource xiuXiuXiuVoiceExternalResource;
    
    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList();
    }
    
    public PrinzEugenChatFunction() {
        try {
            
            pupuExternalResource = ExternalResource.create(new File("./data/images/chat/噗噗.jpg"));
            xiuXiuXiuVoiceExternalResource = ExternalResource.create(new File("./data/voices/prinz_eugen_chat/咻咻咻.amr"));
        } catch (Exception e) {
            log.error("open cannotRelaxImage error: {}", e.getMessage());
        }
        
    }
    
    @Override
    public boolean acceptStatement(SessionId sessionId, EventInfo event, Statement statement) {
        if (statement instanceof LiteralValueStatement) {
            String newMessage = ((LiteralValueStatement)statement).getValue();
            if (newMessage.contains("噗噗")) {
                Image image = botService.uploadImage(event.getGroupId(), pupuExternalResource);
                botService.sendToGroup(event.getGroupId(), 
                        new PlainText("")
                        .plus(image)
                        );
                return true;
            } else if (newMessage.contains("咻咻咻") || newMessage.contains("西姆咻")) {
                Voice voice = botService.uploadVoice(event.getGroupId(), xiuXiuXiuVoiceExternalResource);
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.add(voice);
                MessageChain messageChain = builder.build();

                botService.sendToGroup(event.getGroupId(), 
                        messageChain
                        );
                return true;
            }
        }
        return false;
    }

}
