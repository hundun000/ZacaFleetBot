package hundun.zacafleetbot.mirai.botlogic.core.function;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.SettingManager;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.BlackBoard;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.ProcessResult;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.LiteralValueStatement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.SubFunctionCallStatement;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;

/**
 * @author hundun
 * Created on 2021/05/19
 */
@Component
public class MiraiCodeFunction extends BaseFunction {
        
    
    @Autowired
    SettingManager settingManager;


    Map<String, SessionData> sessionDataMap = new HashMap<>();
    
    public class SessionData {
        public String messageMiraiCode = "";
        
    }
    
    
    @Override
    public ProcessResult process(BlackBoard blackBoard) {
        SessionId sessionId = blackBoard.getSessionId(); 
        EventInfo event = blackBoard.getEvent(); 
        Statement statement = blackBoard.getStatement();
        if (statement instanceof SubFunctionCallStatement) {
            SubFunctionCallStatement subFunctionCallStatement = (SubFunctionCallStatement)statement;
            if (subFunctionCallStatement.getSubFunction() == SubFunction.DECODE_MIRAI_CODE) {
                if (event.getSenderId() == settingManager.getAdminAccount(event.getBot().getId())) {
                    String miraiCode = subFunctionCallStatement.getArgs().get(0);
                    console.getLogger().info("build MessageChain by miraiCode = " + miraiCode);
                    MessageChain chain = MiraiCode.deserializeMiraiCode(miraiCode);
                    console.sendToGroup(event.getBot(), event.getGroupId(), chain);
                    return new ProcessResult(this, true);
                } else {
                    console.sendToGroup(event.getBot(), event.getGroupId(), (new At(event.getSenderId())).plus("你没有该操作的权限！"));
                    return new ProcessResult(this, true);
                }     
            } else if (subFunctionCallStatement.getSubFunction() == SubFunction.ENCODE_LAST_TO_MIRAI_CODE) {
                SessionData sessionData = sessionDataMap.get(sessionId.id());
                if (sessionData == null) {
                    sessionData = new SessionData();
                    sessionDataMap.put(sessionId.id(), sessionData);
                } 
                String miraiCode = sessionDataMap.get(sessionId.id()).messageMiraiCode;
                console.sendToGroup(event.getBot(), event.getGroupId(), miraiCode);
                return new ProcessResult(this, true);
            }
        } else if (statement instanceof LiteralValueStatement) {
            SessionData sessionData = sessionDataMap.get(sessionId.id());
            if (sessionData == null) {
                sessionData = new SessionData();
                sessionDataMap.put(sessionId.id(), sessionData);
            } 
            sessionDataMap.get(sessionId.id()).messageMiraiCode = statement.getOriginMiraiCode();
        }
        return new ProcessResult(this, false);
    }

    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList(SubFunction.DECODE_MIRAI_CODE,
                SubFunction.ENCODE_LAST_TO_MIRAI_CODE);
    }

}
