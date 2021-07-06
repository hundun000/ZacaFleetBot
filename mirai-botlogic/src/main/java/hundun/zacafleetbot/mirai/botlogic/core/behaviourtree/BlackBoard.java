package hundun.zacafleetbot.mirai.botlogic.core.behaviourtree;

import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
import lombok.Data;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/07/06
 */
@Data
public class BlackBoard {
    
    SessionId sessionId;
    EventInfo event;
    Statement statement;
    
}
