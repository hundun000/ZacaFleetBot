package com.mirai.hundun.function.reminder;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.mirai.hundun.cp.weibo.domain.WeiboCardCache;

/**
 * @author hundun
 * Created on 2021/05/07
 */
@Repository
public interface RemiderTaskRepository extends MongoRepository<ReminderTask, String> {
    List<ReminderTask> findAllByTargetGroup(long targetGroup);
}
