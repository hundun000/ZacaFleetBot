package com.mirai.hundun.cp.weibo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/04/23
 */
@Data
@Document
public class WeiboUserInfoCache {
    @Id
    String id;
    String screen_name;
    String weibo_containerid;
}
