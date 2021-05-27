package com.hundun.mirai.plugin.function;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


import com.hundun.mirai.plugin.CustomBeanFactory;
import com.hundun.mirai.plugin.core.EventInfo;
import com.hundun.mirai.plugin.core.SessionId;
import com.hundun.mirai.plugin.core.UserTag;
import com.hundun.mirai.plugin.parser.statement.AtStatement;
import com.hundun.mirai.plugin.parser.statement.LiteralValueStatement;
import com.hundun.mirai.plugin.parser.statement.Statement;
import com.hundun.mirai.plugin.service.BotService;
import com.hundun.mirai.plugin.service.CharacterRouter;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
public class AmiyaChatFunction implements IFunction {
    
    CharacterRouter characterRouter;
    
    BotService botService;
    @Override
    public void manualWired() {
        this.botService = CustomBeanFactory.getInstance().botService;
        this.characterRouter = CustomBeanFactory.getInstance().characterRouter;
    }
    
    private List<String> talks = new ArrayList<>(Arrays.asList(
            "凯尔希医生教导过我，工作的时候一定要保持全神贯注......嗯，全神贯注。", 
            "罗德岛全舰正处于通常航行状态。博士，整理下航程信息吧？",
            "作为罗德岛的领导者我还有很多不成熟的地方，希望您能更多地为我指明前进的方向。"
            ));
    Random rand = new Random();
    
    private String cannotRelaxTalk = "博士，您还有许多事情需要处理。现在还不能休息哦。";
    private String canRelaxTalk = "博士，辛苦了！累了的话请休息一会儿吧。";
    ExternalResource cannotRelaxExternalResource;
    ExternalResource canRelaxExternalResource;
    ExternalResource damedaneVoiceExternalResource;
    
    int todayIsHoliday;
    int todayIsWorkday;
    
    List<ExternalResource> faces = new ArrayList<>();
    
    ExternalResource ceoboNodgeResource;
    ExternalResource angelinaNodgeResource;
    
    
    public AmiyaChatFunction() {
        try {
            ceoboNodgeResource = ExternalResource.create(new File("./data/images/amiya_chat/yukiNodge.png"));
            angelinaNodgeResource = ExternalResource.create(new File("./data/images/amiya_chat/angelinaNodge.png"));
            cannotRelaxExternalResource = ExternalResource.create(new File("./data/images/amiya_chat/cannotRelax.png"));
            canRelaxExternalResource = ExternalResource.create(new File("./data/images/amiya_chat/canRelax.png"));
            damedaneVoiceExternalResource = ExternalResource.create(new File("./data/voices/amiya_chat/damedane.amr"));
            int faceSize = 16;
            for (int i = 1; i <= faceSize; i++) {
                if (i == 3) {
                    continue;
                }
                faces.add(ExternalResource.create(new File("./data/images/face/face" + i + ".png")));
            }
        } catch (Exception e) {
            log.error("open cannotRelaxImage error: {}", e.getMessage());
        }
        
    }
    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList();
    }


    private boolean canRelax() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int weekDay = now.getDayOfWeek().getValue();
        
        if (now.getDayOfYear() == todayIsHoliday) {
            return true;
        }
        
        if (now.getDayOfYear() == todayIsWorkday || weekDay < 6) {
            if (hour < 9 || hour >= 17) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }

        
    }

    @Override
    public boolean acceptStatement(SessionId sessionId, EventInfo event, Statement statement) {
        if (statement instanceof LiteralValueStatement) {
            String newMessage = ((LiteralValueStatement)statement).getValue();
            if (newMessage.replace(" ", "").equals("阿米娅今天放假") && event.getSenderId() == botService.getAdminAccount()) {
                todayIsHoliday = LocalDateTime.now().getDayOfYear();
                todayIsWorkday = -1;
                botService.sendToGroup(event.getGroupId(), "好耶");
                return true;
            } else if (newMessage.replace(" ", "").equals("阿米娅今天上班") && event.getSenderId() == botService.getAdminAccount()) {
                todayIsHoliday = -1;
                todayIsWorkday = LocalDateTime.now().getDayOfYear();
                botService.sendToGroup(event.getGroupId(), "哼");
                return true;
            } else if (newMessage.contains("下班")) {
                boolean canRelax = canRelax();
                if (canRelax) {
                    Image image = botService.uploadImage(event.getGroupId(), canRelaxExternalResource);
                    botService.sendToGroup(event.getGroupId(), 
                            new PlainText(canRelaxTalk)
                            .plus(image)
                            );
                } else {
                    Image image = botService.uploadImage(event.getGroupId(), cannotRelaxExternalResource);
                    botService.sendToGroup(event.getGroupId(), 
                            new PlainText(cannotRelaxTalk)
                            .plus(image)
                            );
                }
                return true;
            } else if (newMessage.contains("damedane")) {
                Voice voice = botService.uploadVoice(event.getGroupId(), damedaneVoiceExternalResource);
                MessageChainBuilder builder = new MessageChainBuilder();
                builder.add(voice);
                MessageChain messageChain = builder.build();

                botService.sendToGroup(event.getGroupId(), 
                        messageChain
                        );
                return true;
            }
        } else if (statement instanceof AtStatement) {
            if (((AtStatement)statement).getTarget() == botService.getSelfAccount()) {
                botService.sendToGroup(event.getGroupId(), 
                        talks.get(rand.nextInt(talks.size()))
                        //.plus(event.getGroup().uploadImage(cannotRelaxExternalResource))
                        );
            }
            return true;
        }
        return false;
    }

    public boolean acceptNudged(EventInfo eventInfo) {
        Image image = null;
        if (eventInfo.getTargetId() == botService.getSelfAccount()) {
            image = botService.uploadImage(eventInfo.getGroupId(), faces.get(rand.nextInt(faces.size())));
        } else {
            List<UserTag> tags = characterRouter.getUserTags(eventInfo.getTargetId());
            if (tags.contains(UserTag.CEOBE) && ceoboNodgeResource != null) {
                image = botService.uploadImage(eventInfo.getGroupId(), ceoboNodgeResource);
            } else if (tags.contains(UserTag.ANGELINA) && angelinaNodgeResource != null) {
                image = botService.uploadImage(eventInfo.getGroupId(), angelinaNodgeResource);
            } 

        }
            
            
            
        
        if (image != null) {
            botService.sendToGroup(eventInfo.getGroupId(), 
                    new At(eventInfo.getSenderId())
                    .plus(image)
                    );
            return true;
        }
        return false;
    }


    
}