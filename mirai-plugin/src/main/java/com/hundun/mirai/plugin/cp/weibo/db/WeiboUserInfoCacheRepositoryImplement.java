package com.hundun.mirai.plugin.cp.weibo.db;

import com.hundun.mirai.plugin.cp.weibo.domain.WeiboUserInfoCache;
import com.hundun.mirai.plugin.db.BaseRepositoryImplement;
import com.hundun.mirai.plugin.db.CollectionSettings;

/**
 * @author hundun
 * Created on 2021/06/01
 */
public class WeiboUserInfoCacheRepositoryImplement extends BaseRepositoryImplement<WeiboUserInfoCache>
        implements WeiboUserInfoCacheRepository {

    public WeiboUserInfoCacheRepositoryImplement(CollectionSettings<WeiboUserInfoCache> mongoSettings) {
        super(mongoSettings);
    }
}
