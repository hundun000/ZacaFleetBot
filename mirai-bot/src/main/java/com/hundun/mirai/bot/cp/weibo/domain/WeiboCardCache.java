package com.hundun.mirai.bot.cp.weibo.domain;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/23
 */
@Data
public class WeiboCardCache {

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
