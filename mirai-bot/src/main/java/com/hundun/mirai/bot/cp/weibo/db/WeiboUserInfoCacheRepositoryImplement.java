package com.hundun.mirai.bot.cp.weibo.db;

import com.hundun.mirai.bot.cp.weibo.domain.WeiboUserInfoCache;
import com.hundun.mirai.bot.helper.db.BaseRepositoryImplement;
import com.hundun.mirai.bot.helper.db.CollectionSettings;

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
