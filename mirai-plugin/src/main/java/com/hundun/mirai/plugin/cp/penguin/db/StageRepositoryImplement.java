package com.hundun.mirai.plugin.cp.penguin.db;

import org.bson.conversions.Bson;

import com.hundun.mirai.plugin.cp.penguin.domain.Stage;
import com.hundun.mirai.plugin.db.BaseRepositoryImplement;
import com.hundun.mirai.plugin.db.CollectionSettings;
import com.mongodb.client.model.Filters;

/**
 * @author hundun
 * Created on 2021/05/31
 */
public class StageRepositoryImplement extends BaseRepositoryImplement<Stage> implements StageRepository {

    public StageRepositoryImplement(CollectionSettings<Stage> mongoSettings) {
        super(mongoSettings);

    }

    @Override
    public Stage findOneByCode(String code) {
        Bson filter = Filters.eq("code", code);
        return collection.find(filter).first();
    }

    

}
