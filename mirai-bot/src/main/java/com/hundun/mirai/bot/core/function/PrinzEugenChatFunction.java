package com.hundun.mirai.bot.core.function;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.hundun.mirai.bot.core.CustomBeanFactory;
import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.SessionId;
import com.hundun.mirai.bot.core.parser.statement.LiteralValueStatement;
import com.hundun.mirai.bot.core.parser.statement.Statement;
import com.hundun.mirai.bot.export.IConsole;

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
public class PrinzEugenChatFunction implements IFunction {

    IConsole offlineConsole;
    @Override
    public void manualWired() {
        this.offlineConsole = CustomBeanFactory.getInstance().console;
    }

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
                Image image = offlineConsole.uploadImage(event.getBot(), event.getGroupId(), pupuExternalResource);
                offlineConsole.sendToGroup(event.getBot(), event.getGroupId(), 
                        new PlainText("")
                        .plus(image)
                        );
                return true;
            } else if (newMessage.contains("咻咻咻") || newMessage.contains("西姆咻")) {
                Voice voice = offlineConsole.uploadVoice(event.getBot(), event.getGroupId(), xiuXiuXiuVoiceExternalResource);
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.add(voice);
                MessageChain messageChain = builder.build();

                offlineConsole.sendToGroup(event.getBot(), event.getGroupId(), 
                        messageChain
                        );
                return true;
            }
        }
        return false;
    }

}
