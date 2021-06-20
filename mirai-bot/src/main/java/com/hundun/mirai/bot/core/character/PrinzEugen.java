package com.hundun.mirai.bot.core.character;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;

import com.hundun.mirai.bot.core.CustomBeanFactory;
import com.hundun.mirai.bot.core.data.EventInfo;
import com.hundun.mirai.bot.core.data.SessionId;
import com.hundun.mirai.bot.core.function.KancolleWikiQuickSearchFunction;
import com.hundun.mirai.bot.core.function.MiraiCodeFunction;
import com.hundun.mirai.bot.core.function.PrinzEugenChatFunction;
import com.hundun.mirai.bot.core.function.RepeatConsumer;
import com.hundun.mirai.bot.core.function.SubFunction;
import com.hundun.mirai.bot.core.function.WeiboFunction;
import com.hundun.mirai.bot.core.function.reminder.ReminderFunction;
import com.hundun.mirai.bot.core.parser.StatementType;
import com.hundun.mirai.bot.core.parser.statement.QuickSearchStatement;
import com.hundun.mirai.bot.core.parser.statement.Statement;
import com.hundun.mirai.bot.core.parser.statement.SubFunctionCallStatement;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/28
 */
@Slf4j
public class PrinzEugen extends BaseCharacter {

    //@Value("${character.prinzEugen.listenWeiboUids:}")
    public String[] listenWeiboUids = new String[0];
    
    public PrinzEugen() {
        super("CHARACTER_PRINZ_EUGEN");
    }

    WeiboFunction weiboFunction;

    PrinzEugenChatFunction chatFunction;
    
    RepeatConsumer repeatConsumer;
    
    ReminderFunction reminderFunction;
    
    MiraiCodeFunction miraiCodeFunction;
    
    KancolleWikiQuickSearchFunction kancolleWikiQuickSearchFunction;
    
    @Override
    public void manualWired() {
        super.manualWired();
        
        this.listenWeiboUids = CustomBeanFactory.getInstance().publicSettings.valueOrDefault(getId());
        
        this.weiboFunction = CustomBeanFactory.getInstance().weiboFunction;
        this.chatFunction = CustomBeanFactory.getInstance().prinzEugenChatFunction;
        this.repeatConsumer = CustomBeanFactory.getInstance().repeatConsumer;
        this.reminderFunction = CustomBeanFactory.getInstance().reminderFunction;
        this.miraiCodeFunction = CustomBeanFactory.getInstance().miraiCodeFunction;
        this.kancolleWikiQuickSearchFunction = CustomBeanFactory.getInstance().kancolleWikiQuickSearchFunction;
    }
    
    @Override
    public void afterManualWired() {
        super.afterManualWired();

        weiboFunction.putCharacterToData(this.getId(), Arrays.asList(this.listenWeiboUids));
        
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                9, 0, "早上九点到了。嗯？啊，是长门！喂！长~门！……嗯？在哪儿遇见过？那当然是！……等……奇怪？那个……是在哪儿来着……？"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                10, 0, "十点到了。战舰？ 当然！要是和俾斯麦姐姐一起的话，轻轻松松就能击沉！交给我吧！"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                11, 0, "到上午11了，马上就要到中午了呢。今天，在外面吃午饭也不错呢。"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                12, 0, "啊啊，都已经到中午了！现在是正午！午饭，在外面吃奶酪和面包可以吗？心情不错所以来点啤酒可以吗？啊哈，果然不行么…"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                13, 0, "到十三点了。午后的作战也要开始了呢，努力上吧！"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                14, 0, "十四点了。诶？什么什么，酒匂？啊啊我知道，是那个可爱的轻巡呢。"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                15, 0, "十五点到了。啊啊，这个吗？，是，这个是地狱犬作战时候穿的舰装。怎么样？合适吗~？"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                16, 0, "到十六点了。哇，吓了我一跳啊，俾斯麦姐姐大人！请让我与你一同出击！是，务必！"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                17, 0, "十七点。到傍晚了呢。差不多，今天也要日落了呢。……啊，天空真美……。啊，当然俾斯麦姐姐是最美的。"));
        reminderFunction.addCharacterTasks(this.getId(), reminderFunction.createCharacterEverydayChatTask(
                18, 0, "十八点到了。要准备晚饭了呢。今天，吃凉菜可以吗？诶，不要？那热菜可以吗？"));
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
        registerQuickQueryKeyword(".");
        registerSubFunctionByCustomSetting(SubFunction.WEIBO_SHOW_LATEST, "查看镇守府情报");
        registerSubFunctionsByDefaultIdentifier(guideFunction.getSubFunctions());
        registerSubFunctionsByDefaultIdentifier(miraiCodeFunction.getSubFunctions());
        
        registerSyntaxs(SubFunctionCallStatement.syntaxs, StatementType.SUB_FUNCTION_CALL);
        registerSyntaxs(QuickSearchStatement.syntaxs, StatementType.QUICK_SEARCH);
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
            done = miraiCodeFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by decodeFunction");
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
        if (!done) {
            done = kancolleWikiQuickSearchFunction.acceptStatement(sessionId, eventInfo, statement);
            if (done) {
                log.info("done by kancolleWikiQuickSearchFunction");
            }
        }
        return done;
    }

    

}
