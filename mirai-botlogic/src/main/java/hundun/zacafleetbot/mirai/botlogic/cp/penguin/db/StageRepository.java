package hundun.zacafleetbot.mirai.botlogic.cp.penguin.db;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import hundun.zacafleetbot.mirai.botlogic.cp.penguin.domain.Stage;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@Repository
public interface StageRepository extends MongoRepository<Stage, String> {

    Stage findOneByCode(String code);



    
}
