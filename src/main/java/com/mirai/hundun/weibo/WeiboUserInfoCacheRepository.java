package com.mirai.hundun.weibo;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mirai.hundun.weibo.domain.WeiboUserInfoCache;

import java.util.List;


/**
 * @author hundun
 * Created on 2019/12/08
 */
@Repository
public interface WeiboUserInfoCacheRepository extends MongoRepository<WeiboUserInfoCache, String> {

    
}
