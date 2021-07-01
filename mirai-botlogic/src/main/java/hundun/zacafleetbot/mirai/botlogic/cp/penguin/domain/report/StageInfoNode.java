package hundun.zacafleetbot.mirai.botlogic.cp.penguin.domain.report;

import hundun.zacafleetbot.mirai.botlogic.cp.penguin.domain.DropType;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/29
 */
@Data
public class StageInfoNode {
    String itemName;
    DropType dropType;
    int boundsLower;
    int boundsUpper;
}
