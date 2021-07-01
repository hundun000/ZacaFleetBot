package hundun.zacafleetbot.mirai.botlogic.cp.weibo.db;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import hundun.zacafleetbot.mirai.botlogic.cp.weibo.domain.WeiboUserInfoCache;


/**
 * @author hundun
 * Created on 2019/12/08
 */
@Repository
public interface WeiboUserInfoCacheRepository extends MongoRepository<WeiboUserInfoCache, String> {

}
