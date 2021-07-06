package hundun.zacafleetbot.mirai.botlogic.core.behaviourtree;

import hundun.zacafleetbot.mirai.botlogic.core.function.JapaneseFunction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/07/06
 */
@AllArgsConstructor
@Getter
public class ProcessResult {
    BaseBTNode processor;
    boolean done;
    
}
