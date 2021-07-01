package hundun.zacafleetbot.mirai.botlogic.cp.weibo.domain;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/23
 */
@Data
@Document(collection = "weiboCardCache")
public class WeiboCardCache {
    @Id
    String itemid;
    
    String uid;
    String screenName;
    
    String mblog_id;
    LocalDateTime mblogCreatedDateTime;
    String mblog_text;
    String mblog_textDetail;
    
    
    List<String> picsLargeUrls;
    
    //File singlePicture;
}
