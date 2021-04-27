package com.mirai.hundun.bot.amiya;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.bot.Amiya;
import com.mirai.hundun.bot.IAtHandler;
import com.mirai.hundun.bot.IFunction;
import com.mirai.hundun.parser.statement.AtStatement;
import com.mirai.hundun.parser.statement.LiteralValueStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.parser.statement.amiya.AmiyaFunctionCallStatement;
import com.mirai.hundun.service.BotService;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
@Component
public class AmiyaTalkHandler implements IFunction {
    
    
    
    private List<String> talks = new ArrayList<>(Arrays.asList(
            "凯尔希医生教导过我，工作的时候一定要保持全神贯注......嗯，全神贯注。", 
            "罗德岛全舰正处于通常航行状态。博士，整理下航程信息吧？",
            "作为罗德岛的领导者我还有很多不成熟的地方，希望您能更多地为我指明前进的方向。"
            ));
    Random rand = new Random();
    
    private String cannotRelaxTalk = "博士，您还有许多事情需要处理。现在还不能休息哦。";
    private File cannotRelaxImage;
    ExternalResource cannotRelaxExternalResource;
    
    List<ExternalResource> faces = new ArrayList<>();
    
    
    @Autowired
    Amiya parent;

    public AmiyaTalkHandler() {
        try {
            cannotRelaxImage = new File("./data/images/talk/cannotRelax.png");
            cannotRelaxExternalResource = ExternalResource.create(cannotRelaxImage);
            int faceSize = 16;
            for (int i = 1; i <= faceSize; i++) {
                faces.add(ExternalResource.create(new File("./data/images/face/face" + i + ".png")));
            }
        } catch (Exception e) {
            log.error("open cannotRelaxImage error: {}", e.getMessage());
        }
        
    }

    public boolean acceptAt(GroupMessageEvent event, At at) {
        if (at == null) {
            return false;
        }
        if (at.getTarget() == parent.getSelfAccount()) {
            event.getSubject().sendMessage(
                    //(new At(event.getSender().getId()))
                    new PlainText(talks.get(rand.nextInt(talks.size())))
                    .plus(event.getGroup().uploadImage(cannotRelaxExternalResource))
                    );
            return true;
        }
        return false;
    }


    public boolean acceptPlainText(GroupMessageEvent event, PlainText plainText) {
        String newMessage = plainText != null ? plainText.contentToString() : null;
        
        if (newMessage == null) {
            return false;
        }
        
        if (newMessage.contains("下班")) {
            event.getSubject().sendMessage(cannotRelaxTalk);
            return true;
        }
        
        return false;
    }


    @Override
    public boolean acceptStatement(GroupMessageEvent event, Statement statement) {
        if (statement instanceof LiteralValueStatement) {
            String newMessage = ((LiteralValueStatement)statement).getValue();
            if (newMessage.contains("下班")) {
                parent.sendToEventSubject(event, 
                        new PlainText(cannotRelaxTalk)
                        .plus(event.getGroup().uploadImage(cannotRelaxExternalResource))
                        );
                return true;
            }
        } else if (statement instanceof AtStatement) {
            parent.sendToEventSubject(event, 
                    talks.get(rand.nextInt(talks.size()))
                    //.plus(event.getGroup().uploadImage(cannotRelaxExternalResource))
                    );
            return true;
        }
        return false;
    }

    public void acceptNudged(@NotNull NudgeEvent event) {
        parent.sendToEventSubject(event.getSubject(), 
                new At(event.getFrom().getId())
                .plus(event.getSubject().uploadImage(faces.get(rand.nextInt(faces.size()))))
                );
    }


    
}
