package com.hundun.mirai.bot.core.function.reminder;

import java.util.List;

import org.bson.conversions.Bson;

import com.hundun.mirai.bot.helper.db.BaseRepositoryImplement;
import com.hundun.mirai.bot.helper.db.CollectionSettings;
import com.mongodb.client.model.Filters;

/**
 * @author hundun
 * Created on 2021/06/02
 */
public class RemiderTaskRepositoryImplement extends BaseRepositoryImplement<ReminderTask> implements RemiderTaskRepository {

    public RemiderTaskRepositoryImplement(CollectionSettings<ReminderTask> mongoSettings) {
        super(mongoSettings);
    }

    @Override
    public List<ReminderTask> findAllByTargetGroup(long targetGroup) {
        Bson filter = Filters.eq("targetGroup", targetGroup);
        return findAllByFilter(filter, null);
    }



}
