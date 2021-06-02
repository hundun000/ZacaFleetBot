package com.hundun.mirai.bot.cp.penguin.db;


import java.util.List;

import com.hundun.mirai.bot.cp.penguin.domain.Stage;

/**
 * @author hundun
 * Created on 2021/04/26
 */
public interface StageRepository {

    Stage findOneByCode(String code);

    void deleteAll();

    void saveAll(List<Stage> stages);

    Stage findById(String stageId);

    
}
