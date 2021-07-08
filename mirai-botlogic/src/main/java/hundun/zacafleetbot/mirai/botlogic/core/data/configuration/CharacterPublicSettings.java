package hundun.zacafleetbot.mirai.botlogic.core.data.configuration;
/**
 * @author hundun
 * Created on 2021/06/23
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hundun.zacafleetbot.mirai.botlogic.cp.weibo.domain.WeiboViewFormat;
import lombok.Data;

@Data
public class CharacterPublicSettings {
    Map<String, WeiboViewFormat> weibo = new HashMap<>(0);
    Map<String, String> hourlyChats = new HashMap<>(0);
}
