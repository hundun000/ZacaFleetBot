package com.hundun.mirai.plugin.cp.weibo;


import com.hundun.mirai.plugin.cp.weibo.domain.WeiboUserInfoCache;


/**
 * @author hundun
 * Created on 2019/12/08
 */

public interface WeiboUserInfoCacheRepository {

    boolean existsById(String uid);

    WeiboUserInfoCache findById(String uid);

    void save(WeiboUserInfoCache userInfoCacahe);

    
}
