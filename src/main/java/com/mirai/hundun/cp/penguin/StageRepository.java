package com.mirai.hundun.cp.penguin;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mirai.hundun.cp.penguin.domain.Stage;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@Repository
public interface StageRepository extends MongoRepository<Stage, String> {

    Stage findOneByCode(String stageCode);

    
}
