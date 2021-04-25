package com.mirai.hundun.bot.amiya;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.bot.Amiya;
import com.mirai.hundun.bot.IAtHandler;
import com.mirai.hundun.bot.IPlainTextHandler;
import com.mirai.hundun.service.BotService;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
@Component
public class AmiyaTalkHandler implements IAtHandler, IPlainTextHandler {
    
    private List<String> talks = new ArrayList<>(Arrays.asList(
            "凯尔希医生教导过我，工作的时候一定要保持全神贯注......嗯，全神贯注。", 
            "罗德岛全舰正处于通常航行状态。博士，整理下航程信息吧？",
            "作为罗德岛的领导者我还有很多不成熟的地方，希望您能更多地为我指明前进的方向。"
            ));
    Random rand = new Random();
    
    private String cannotRelaxTalk = "博士，您还有许多事情需要处理。现在还不能休息哦。";
    
    @Autowired
    Amiya parent;


    @Override
    public boolean acceptAt(GroupMessageEvent event, At at) {
        if (at == null) {
            return false;
        }
        if (at.getTarget() == parent.getSelfAccount()) {
            event.getSubject().sendMessage(
                    //(new At(event.getSender().getId()))
                    (talks.get(rand.nextInt(talks.size())))
                    );
            return true;
        }
        return false;
    }


    @Override
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
    
}
