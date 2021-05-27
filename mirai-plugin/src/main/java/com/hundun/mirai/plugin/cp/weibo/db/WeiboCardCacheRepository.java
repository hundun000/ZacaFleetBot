package com.hundun.mirai.plugin.cp.weibo.db;



import com.hundun.mirai.plugin.cp.weibo.domain.WeiboCardCache;

import java.util.List;


/**
 * @author hundun
 * Created on 2019/12/08
 */
public interface WeiboCardCacheRepository {

    //WeiboCardCache findFirstByUid(String uid);
    List<WeiboCardCache> findTop5ByUidOrderByMblogCreatedDateTimeDesc(String uid);

    void save(WeiboCardCache cardCache);

    boolean existsById(String itemid);

    WeiboCardCache findById(String itemid);
}
