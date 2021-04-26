package com.mirai.hundun.cp.weibo.domain;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/23
 */
@Data
@Document
public class WeiboCardCache {
    @Id
    String itemid;
    @Indexed
    String uid;
    String mblog_id;
    LocalDateTime mblogCreatedDateTime;
    String mblog_text;
    String mblog_textDetail;
}
