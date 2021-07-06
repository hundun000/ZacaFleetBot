package hundun.zacafleetbot.mirai.botlogic.core.function;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.BlackBoard;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.ProcessResult;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.LiteralValueStatement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
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
@Component
public class PrinzEugenChatFunction extends BaseFunction {
        

    @PostConstruct
    public void postConsoleBind() {

        initExternalResource();
    }

    ExternalResource pupuExternalResource;
    ExternalResource xiuXiuXiuVoiceExternalResource;
    
    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList();
    }
    
    private void initExternalResource() {
        try {
            pupuExternalResource = ExternalResource.create(console.resolveDataFile("images/prinz_eugen_chat/噗噗.jpg"));
            xiuXiuXiuVoiceExternalResource = ExternalResource.create(console.resolveDataFile("voices/prinz_eugen_chat/咻咻咻.amr"));
        } catch (Exception e) {
            console.getLogger().error("open cannotRelaxImage error: " + e.getMessage());
        }
    }
    
    
    public PrinzEugenChatFunction() {
        
        
    }
    
    @Override
    public ProcessResult process(BlackBoard blackBoard) {
        SessionId sessionId = blackBoard.getSessionId(); 
        EventInfo event = blackBoard.getEvent(); 
        Statement statement = blackBoard.getStatement();
        if (statement instanceof LiteralValueStatement) {
            String newMessage = ((LiteralValueStatement)statement).getValue();
            if (newMessage.contains("噗噗")) {
                Image image = console.uploadImage(event.getBot(), event.getGroupId(), pupuExternalResource);
                console.sendToGroup(event.getBot(), event.getGroupId(), 
                        new PlainText("")
                        .plus(image)
                        );
                return new ProcessResult(this, true);
            } else if (newMessage.contains("咻咻咻") || newMessage.contains("西姆咻")) {
                Voice voice = console.uploadVoice(event.getBot(), event.getGroupId(), xiuXiuXiuVoiceExternalResource);
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.add(voice);
                MessageChain messageChain = builder.build();

                console.sendToGroup(event.getBot(), event.getGroupId(), 
                        messageChain
                        );
                return new ProcessResult(this, true);
            }
        }
        return new ProcessResult(this, false);
    }

}
