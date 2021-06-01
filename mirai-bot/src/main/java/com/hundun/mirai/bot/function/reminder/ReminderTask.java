package com.hundun.mirai.bot.function.reminder;


import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/07
 */
@Data
public class ReminderTask {
    String id;
    int count;
    long targetGroup;
    String text;
    int monthCondition;
    int dayOfMonthCondition;
    int dayOfWeekCondition;
    int hourCondition;
    int minuteCondition;
}
