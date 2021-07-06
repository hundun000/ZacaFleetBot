package hundun.zacafleetbot.mirai.botlogic.core.function;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.BlackBoard;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.ProcessResult;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.LiteralValueStatement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
import net.mamoe.mirai.message.code.MiraiCode;

/**
 * @author hundun
 * Created on 2021/04/21
 */
@Component
public class RepeatConsumer extends BaseFunction {
        

    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList();
    }
    //private final long groupId;
    Map<String, SessionData> sessionDataMap = new HashMap<>();
    
    public class SessionData {
        public String messageMiraiCode = "";
        public int count = 0;
    }
    
    public RepeatConsumer() {
        //countNode = new CountNode();
        //this.groupId = groupId;
    }

    

    @Override
    public ProcessResult process(BlackBoard blackBoard) {
        SessionId sessionId = blackBoard.getSessionId(); 
        EventInfo event = blackBoard.getEvent(); 
        Statement statement = blackBoard.getStatement();
        if (statement instanceof LiteralValueStatement) {
            String newMessageMiraiCode = ((LiteralValueStatement)statement).getOriginMiraiCode();


            SessionData sessionData = sessionDataMap.get(sessionId.id());
            if (sessionData == null) {
                sessionData = new SessionData();
                sessionDataMap.put(sessionId.id(), sessionData);
            } 
            
                
            if (sessionData.messageMiraiCode.equals(newMessageMiraiCode)) {
                sessionData.count++;
            } else {
                sessionData.count = 1;
                sessionData.messageMiraiCode = newMessageMiraiCode;
            }

            
            if (sessionData.count == 3) {
                console.sendToGroup(event.getBot(), event.getGroupId(), MiraiCode.deserializeMiraiCode(sessionData.messageMiraiCode));
                return new ProcessResult(this, true);
            }
        }
        return new ProcessResult(this, false);
    }

}
