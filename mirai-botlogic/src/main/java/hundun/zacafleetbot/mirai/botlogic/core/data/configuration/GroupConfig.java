package hundun.zacafleetbot.mirai.botlogic.core.data.configuration;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/28
 */
@Data
public class GroupConfig {
    Long groupId;
    String groupDescription;
    List<String> enableCharacters = new ArrayList<>();
    //List<String> observeBlogUids = new ArrayList<>(); 
}
