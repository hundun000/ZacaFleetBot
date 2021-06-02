package com.hundun.mirai.bot.cp.weibo.db;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bson.conversions.Bson;

import com.hundun.mirai.bot.cp.weibo.domain.WeiboCardCache;
import com.hundun.mirai.bot.db.BaseRepositoryImplement;
import com.hundun.mirai.bot.db.CollectionSettings;
import com.mongodb.client.model.Filters;

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
        List<WeiboCardCache> cardCaches = findAllByFilter(filter, 5);
        Collections.sort(cardCaches, new Comparator<WeiboCardCache>() {
            @Override
            public int compare(WeiboCardCache o1, WeiboCardCache o2) {
                return -1 * o1.getMblogCreatedDateTime().compareTo(o2.getMblogCreatedDateTime());
            }
        });
        return cardCaches;
    }

    

}
