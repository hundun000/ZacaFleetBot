package hundun.zacafleetbot.mirai.botlogic.cp.weibo.db;



import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import hundun.zacafleetbot.mirai.botlogic.cp.weibo.domain.WeiboCardCache;


/**
 * @author hundun
 * Created on 2019/12/08
 */
@Repository
public interface WeiboCardCacheRepository extends MongoRepository<WeiboCardCache, String> {

    //WeiboCardCache findFirstByUid(String uid);
    List<WeiboCardCache> findTop5ByUidOrderByBlogCreatedDateTimeDesc(String uid);


}
