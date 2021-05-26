package com.hundun.mirai.plugin.function.reminder;

import java.util.List;


/**
 * @author hundun
 * Created on 2021/05/07
 */
public interface RemiderTaskRepository {
    List<ReminderTask> findAllByTargetGroup(long targetGroup);

    void insert(ReminderTask task);

    boolean existsById(String targetId);

    void deleteById(String targetId);

    List<ReminderTask> findAll();

    void delete(ReminderTask task);

    void save(ReminderTask task);
}
