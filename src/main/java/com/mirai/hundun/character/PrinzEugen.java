package com.mirai.hundun.character;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.SessionId;
import com.mirai.hundun.function.PrinzEugenChatFunction;
import com.mirai.hundun.function.RepeatConsumer;
import com.mirai.hundun.function.SubFunction;
import com.mirai.hundun.function.WeiboFunction;
import com.mirai.hundun.function.reminder.ReminderFunction;
import com.mirai.hundun.parser.StatementType;
import com.mirai.hundun.parser.statement.SubFunctionCallStatement;
import com.mirai.hundun.parser.statement.Statement;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/28
 */
@Slf4j
@Component
public class PrinzEugen extends BaseCharacter {

    @Value("${character.prinzEugen.listenWeiboUids:}")
    public String[] listenWeiboUids;
    
    public PrinzEugen() {
        super("PrinzEugen");
    }

    @Autowired
    WeiboFunction weiboFunction;
    
    @Autowired
    PrinzEugenChatFunction chatFunction;
    
    @Autowired
    RepeatConsumer repeatConsumer;
    
    @Autowired
    ReminderFunction reminderFunction;
    
    @PostConstruct
    public void init() {
        weiboFunction.putCharacterToData(this.getId(), Arrays.asList(this.listenWeiboUids));
        
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                19, 0, "十九点到了。那么，晚饭就吃猪脚火锅吧。汤料的味道也全都浸了进去很不错对吧？最后再把饭加进去做成杂煮也很美味！"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                20, 0, "二十点到了。日本的重巡也，很充实呢。唔姆唔姆，嗯~原来如此……唔姆唔姆…"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                21, 0, "二十一点。诶，擅长什么…是吗？是啊，之前有过有过把舰炮压低，狠狠的揍了一顿战车群这回事儿。这个就很擅长！是！"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                22, 0, "完全入夜了呢，二十二点到了。Admiral，今天一天的，作战真是辛苦您了！"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                23, 0, "二十三点到了。嗯~差不多我也该休息了……Gute Nacht……诶，不行……？"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                0, 0, "呜哇！？正好0点！今天是，由本欧根·亲王来担当时报的工作呢。好的，知道啦！交给我吧！"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                1, 0, "凌晨一点到啦！这种感觉可以，吗？啊，是吗！太好啦！"));
    }
    
    @Override
    protected void initParser() {
        registerWakeUpKeyword("欧根");
        registerSubFunctionByCustomSetting(SubFunction.WEIBO_SHOW_LATEST, "查看镇守府情报");
        registerSubFunctionsByDefaultIdentifier(guideFunction.getSubFunctions());
        
        registerSyntaxs(SubFunctionCallStatement.syntaxs, StatementType.SUB_FUNCTION_CALL);
    }

    @Override
    public boolean onNudgeEvent(@NotNull EventInfo eventInfo) throws Exception {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onGroupMessageEvent(@NotNull EventInfo eventInfo) throws Exception {
        Statement statement;
        try {
            statement = parserSimpleParse(eventInfo.getMessage());
        } catch (Exception e) {
            log.error("Parse error: ", e);
            return false;
        }
        
        SessionId sessionId = new SessionId(this.getId(), eventInfo.getGroupId());

        boolean done = false;

        if (!done) {
            done = reminderFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by reminderFunction");
            }
        }
        if (!done) {
            done = weiboFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by weiboFunction");
            }
        }
        if (!done) {
            done = chatFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by chatFunction");
            }
        }
        if (!done) {
            done = repeatConsumer.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by repeatConsumer");
            }
        }
        if (!done) {
            done = guideFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by guideFunction");
            }
        }
        return done;
    }

}
