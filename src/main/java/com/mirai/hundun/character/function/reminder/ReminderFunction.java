package com.mirai.hundun.character.function.reminder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mirai.hundun.character.BaseCharacter;
import com.mirai.hundun.character.function.IFunction;
import com.mirai.hundun.character.function.SubFunction;
import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.GroupConfig;
import com.mirai.hundun.parser.statement.FunctionCallStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;
import com.mirai.hundun.service.CharacterRouter;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.At;

/**
 * @author hundun
 * Created on 2021/05/07
 */
@Slf4j
@Component
public class ReminderFunction implements IFunction {


    @Autowired
    RemiderTaskRepository taskRepository;


    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList(SubFunction.REMINDER_CREATE_TASK);
    }
    
    @Autowired
    BotService botService;
    
    @Autowired
    CharacterRouter characterRouter;
    
    Map<String, List<ReminderTask>> characterTasks = new HashMap<>();
    
    public ReminderTask createCharacterEverydayChatTask(
            int hourCondition,
            int minuteCondition,
            String text
            ) {
        return createTask(-1, -1, -1, hourCondition, minuteCondition, -1, text, -1);
    }
    
    public ReminderTask createTask(
            int monthCondition,
            int dayOfMonthCondition,
            int dayOfWeekCondition,
            int hourCondition,
            int minuteCondition,
            int count,
            String text,
            long targetGroup
            ) {
        ReminderTask task = new ReminderTask();
        try {
            task.monthCondition = monthCondition;
            task.dayOfMonthCondition = dayOfMonthCondition;
            task.dayOfWeekCondition = dayOfWeekCondition;
            task.hourCondition = hourCondition;
            task.minuteCondition = minuteCondition;
            task.count = count;
            task.text = text;
            task.targetGroup = targetGroup;
        } catch (Exception e) {
            log.error("addTask error:", e);
            return null;
        }
        
        
        return task;
    }
    
    
    @Override
    public boolean acceptStatement(String sessionId, EventInfo event, Statement statement) {
        if (statement instanceof FunctionCallStatement) {
            FunctionCallStatement functionCallStatement = (FunctionCallStatement)statement;
            if (functionCallStatement.getFunctionName() == SubFunction.REMINDER_CREATE_TASK) {
                if (event.getSenderId() != botService.getAdminAccount()) {
                    botService.sendToGroup(event.getGroupId(), (new At(event.getSenderId())).plus("你没有该操作的权限！"));
                    return true;
                }
                ReminderTask task = createTask(
                        Integer.valueOf(functionCallStatement.getArgs().get(0)), 
                        Integer.valueOf(functionCallStatement.getArgs().get(1)), 
                        Integer.valueOf(functionCallStatement.getArgs().get(2)), 
                        Integer.valueOf(functionCallStatement.getArgs().get(3)), 
                        Integer.valueOf(functionCallStatement.getArgs().get(4)),  
                        Integer.valueOf(functionCallStatement.getArgs().get(5)), 
                        functionCallStatement.getArgs().get(6), 
                        event.getGroupId()
                        );
                if (task != null) {
                    taskRepository.insert(task);
                    botService.sendToGroup(event.getGroupId(), (new At(event.getSenderId())).plus("创建成功"));
                } else {
                    botService.sendToGroup(event.getGroupId(), (new At(event.getSenderId())).plus("创建失败"));
                }
                
                return true;
            }
        }
        return false;
    }
    
    @Scheduled(cron = "0 0 * * * *")
    public void checkCharacterTasks() {
        log.info("checkCharacterTasks Scheduled arrival");
        LocalDateTime now = LocalDateTime.now();
        for (GroupConfig entry : characterRouter.getGroupConfigs().values()) {
            Long groupId = entry.getGroupId();
            List<String> characterIds = entry.getEnableCharacters();
            log.info("checkCharacterTasks for groupId = {}", groupId);
            for (String characterId : characterIds) {
                List<ReminderTask> tasks = characterTasks.get(characterId);
                if (tasks != null) {
                    for (ReminderTask task : tasks) {
                        boolean triggered = checkTask(task, now);
                        if (triggered) {
                            botService.sendToGroup(groupId, task.text);
                        }
                    }
                }
            }
            
        }
    }
    
    public boolean checkTask(ReminderTask task, LocalDateTime now) {
        
        if (task.monthCondition != -1 && task.monthCondition != now.getMonth().getValue()) {
            return false;
        }
        if (task.dayOfMonthCondition != -1 && task.dayOfMonthCondition != now.getDayOfMonth()) {
            return false;
        }
        if (task.dayOfWeekCondition != -1 && task.dayOfWeekCondition != now.getDayOfWeek().getValue()) {
            return false;
        }
        if (task.hourCondition != -1 && task.hourCondition != now.getHour()) {
            return false;
        }
        if (task.minuteCondition != -1 && task.minuteCondition != now.getMinute()) {
            return false;
        }
        return true;
    }
    
    @Scheduled(cron = "0 * * * * *")
    public void checkUserTasks() {
        log.info("checkUserTasks Scheduled arrival");
        LocalDateTime now = LocalDateTime.now();

        List<ReminderTask> tasks = taskRepository.findAll();
        for (ReminderTask task : tasks) {
            boolean triggered = checkTask(task, now);
            if (triggered) {
                botService.sendToGroup(task.targetGroup, task.text);
                if (task.count != -1) {
                    task.count--;
                    if (task.count == 0) {
                        taskRepository.delete(task);
                    } else {
                        taskRepository.save(task);
                    }
                }
            }
        }

    }

    
    public void addCharacterTasks(String characterId, ReminderTask task) {
        if (!characterTasks.containsKey(characterId)) {
            characterTasks.put(characterId, new ArrayList<>());
        }
        List<ReminderTask> tasks = characterTasks.get(characterId);
        tasks.add(task);
    }
    

}
