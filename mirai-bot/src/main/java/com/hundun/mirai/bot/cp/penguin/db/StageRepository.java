package com.hundun.mirai.bot.cp.penguin.db;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hundun.mirai.bot.cp.penguin.domain.Stage;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@Repository
public interface StageRepository extends MongoRepository<Stage, String> {

    Stage findOneByCode(String code);



    
}
