package hundun.zacafleetbot.mirai.botlogic.core.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hundun
 * Created on 2021/05/17
 */
@Getter
@AllArgsConstructor
public class SessionId {
    final String characterId;
    final long userId;
    
    public String id() {
        return this.characterId + "@" + this.userId;
    }
}
