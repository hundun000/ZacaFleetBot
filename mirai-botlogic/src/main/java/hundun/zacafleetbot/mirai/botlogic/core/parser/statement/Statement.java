package hundun.zacafleetbot.mirai.botlogic.core.parser.statement;

import java.util.List;

import hundun.zacafleetbot.mirai.botlogic.core.parser.StatementType;
import hundun.zacafleetbot.mirai.botlogic.core.parser.Token;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/27
 */
@Data
public abstract class Statement {
    List<Token> tokens;
    StatementType type;
    String originMiraiCode;
}
