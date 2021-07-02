package hundun.zacafleetbot.mirai.botlogic.core.data.configuration;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/28
 */
@Data
public class AppPublicSettings {
    Map<String, CharacterPublicSettings> characterIdToPublicSettings = new HashMap<>(0);
}