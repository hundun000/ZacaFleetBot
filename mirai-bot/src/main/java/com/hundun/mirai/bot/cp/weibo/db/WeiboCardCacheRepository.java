package com.hundun.mirai.bot.cp.weibo.db;



import java.util.List;

import com.hundun.mirai.bot.cp.weibo.domain.WeiboCardCache;


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
