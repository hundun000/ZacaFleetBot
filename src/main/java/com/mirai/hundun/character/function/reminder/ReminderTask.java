package com.mirai.hundun.character.function.reminder;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/07
 */
@Data
@Document
public class ReminderTask {
    int count;
    long targetGroup;
    String text;
    int monthCondition;
    int dayOfMonthCondition;
    int dayOfWeekCondition;
    int hourCondition;
    int minuteCondition;
}
