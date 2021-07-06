package hundun.zacafleetbot.mirai.botlogic.core.function;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.ActionBTNode;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.BaseBTNode;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
import hundun.zacafleetbot.mirai.botlogic.export.IConsole;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Component
public abstract class BaseFunction extends ActionBTNode {
    
    @Autowired
    protected IConsole console;
    
    //public abstract boolean acceptStatement(SessionId sessionId, EventInfo eventinfo, Statement statement);
    
    public abstract List<SubFunction> getSubFunctions();
    
    
}
