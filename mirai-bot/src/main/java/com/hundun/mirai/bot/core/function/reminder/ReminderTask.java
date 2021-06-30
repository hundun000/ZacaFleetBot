package com.hundun.mirai.bot.core.function.reminder;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/07
 */
@Data
@Document(collection = "reminderTask")
public class ReminderTask {
    @Id
    String id;
    long botId;
    int count;
    long targetGroup;
    String text;
    int monthCondition;
    int dayOfMonthCondition;
    int dayOfWeekCondition;
    int hourCondition;
    int minuteCondition;
}
