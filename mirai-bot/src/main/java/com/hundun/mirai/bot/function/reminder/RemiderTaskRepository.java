package com.hundun.mirai.bot.function.reminder;

import java.util.List;


/**
 * @author hundun
 * Created on 2021/05/07
 */
public interface RemiderTaskRepository {
    List<ReminderTask> findAllByTargetGroup(long targetGroup);

    boolean existsById(String targetId);

    void deleteById(String targetId);

    List<ReminderTask> findAll();

    void delete(ReminderTask task);

    void save(ReminderTask task);
}
