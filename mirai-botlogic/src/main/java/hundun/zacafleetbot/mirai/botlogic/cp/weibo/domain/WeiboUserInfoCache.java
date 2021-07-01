package hundun.zacafleetbot.mirai.botlogic.cp.weibo.domain;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/23
 */
@Data
@Document(collection = "weiboUserInfoCache")
public class WeiboUserInfoCache {
    @Id
    String uid;
    String screen_name;
    String weibo_containerid;
}
