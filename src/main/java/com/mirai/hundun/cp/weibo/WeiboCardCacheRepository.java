package com.mirai.hundun.cp.weibo;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mirai.hundun.cp.weibo.domain.WeiboCardCache;
import com.mirai.hundun.cp.weibo.domain.WeiboUserInfoCache;

import java.util.List;


/**
 * @author hundun
 * Created on 2019/12/08
 */
@Repository
public interface WeiboCardCacheRepository extends MongoRepository<WeiboCardCache, String> {

    //WeiboCardCache findFirstByUid(String uid);
    List<WeiboCardCache> findTop5ByUidOrderByMblogCreatedDateTimeDesc(String uid);
}
