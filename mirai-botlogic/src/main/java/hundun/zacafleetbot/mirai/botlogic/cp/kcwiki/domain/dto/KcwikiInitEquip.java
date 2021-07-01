package hundun.zacafleetbot.mirai.botlogic.cp.kcwiki.domain.dto;

import java.util.List;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/24
 */
@Data
public class KcwikiInitEquip {
    int id;
    String name;
    List<String> slots;
    List<String> slot_names;
}
