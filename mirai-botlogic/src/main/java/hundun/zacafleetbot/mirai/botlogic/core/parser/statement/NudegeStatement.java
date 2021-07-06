package hundun.zacafleetbot.mirai.botlogic.core.parser.statement;

import java.util.List;

import hundun.zacafleetbot.mirai.botlogic.core.parser.Token;

/**
 * @author hundun
 * Created on 2021/07/06
 */
public class NudegeStatement extends Statement {
    public final long senderId;
    public final long targetId;
    public NudegeStatement(long senderId, long targetId) {
        this.senderId = senderId;
        this.targetId = targetId;
    }
}
