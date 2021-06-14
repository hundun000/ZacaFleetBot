package com.hundun.mirai.bot.cp.weibo.db;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bson.conversions.Bson;

import com.hundun.mirai.bot.cp.weibo.domain.WeiboCardCache;
import com.hundun.mirai.bot.helper.db.BaseRepositoryImplement;
import com.hundun.mirai.bot.helper.db.CollectionSettings;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;

/**
 * @author hundun
 * Created on 2021/05/31
 */
public class WeiboCardCacheRepositoryImplement extends BaseRepositoryImplement<WeiboCardCache> implements WeiboCardCacheRepository {

    public WeiboCardCacheRepositoryImplement(CollectionSettings<WeiboCardCache> mongoSettings) {
        super(mongoSettings);
    }

    @Override
    public List<WeiboCardCache> findTop5ByUidOrderByMblogCreatedDateTimeDesc(String uid) {
        Bson filter = Filters.eq("uid", uid);
        Bson sorter = Sorts.descending("mblogCreatedDateTime");
        List<WeiboCardCache> cardCaches = findAllByFilter(filter, sorter, 5);
        return cardCaches;
    }

    

}
