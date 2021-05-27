package com.hundun.mirai.plugin.function.reminder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hundun.mirai.plugin.CustomBeanFactory;
import com.hundun.mirai.plugin.core.EventInfo;
import com.hundun.mirai.plugin.core.GroupConfig;
import com.hundun.mirai.plugin.core.SessionId;
import com.hundun.mirai.plugin.function.IFunction;
import com.hundun.mirai.plugin.function.SubFunction;
import com.hundun.mirai.plugin.parser.statement.Statement;
import com.hundun.mirai.plugin.parser.statement.SubFunctionCallStatement;
import com.hundun.mirai.plugin.service.BotService;
import com.hundun.mirai.plugin.service.CharacterRouter;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.At;

/**
 * @author hundun
 * Created on 2021/05/07
 */
@Slf4j
public class ReminderFunction implements IFunction {

    RemiderTaskRepository taskRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    
    @Override
    public List<SubFunction> getSubFunctions() {
        return Arrays.asList(
                SubFunction.REMINDER_CREATE_TASK,
                SubFunction.REMINDER_LIST_TASK,
                SubFunction.REMINDER_REMOVE_TASK
                
                );
    }
    
    BotService botService;
    
    CharacterRouter characterRouter;
    
    @Override
    public void manualWired() {
        this.botService = CustomBeanFactory.getInstance().botService;
        this.characterRouter = CustomBeanFactory.getInstance().characterRouter;
        this.taskRepository = CustomBeanFactory.getInstance().taskRepository;
    }
    
    @Override
    public void afterManualWired() {
        registerClockSchedule();
    }
    
    Map<String, List<ReminderTask>> characterTasks = new HashMap<>();
    
    private String reminderTaskDescroption(ReminderTask task) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id:").append(task.getId()).append(" ");
        if (task.getMonthCondition() != -1) {
            stringBuilder.append(task.getMonthCondition()).append("月");
        }
        if (task.getDayOfMonthCondition() != -1) {
            stringBuilder.append(task.getDayOfMonthCondition()).append("日");
        }
        if (task.getDayOfWeekCondition() != -1) {
            stringBuilder.append("周").append(task.getDayOfWeekCondition());
        }
        if (task.getHourCondition() != -1) {
            stringBuilder.append(task.getHourCondition()).append("时");
        }
        if (task.getMinuteCondition() != -1) {
            stringBuilder.append(task.getMinuteCondition()).append("分");
        }
        if (task.getCount() != -1) {
            stringBuilder.append(task.getCount()).append("次");
        } else {
            stringBuilder.append("无限次");
        }
        stringBuilder.append(" ").append(task.getText());
        return stringBuilder.toString();
    }
    
    
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
    public boolean acceptStatement(SessionId sessionId, EventInfo event, Statement statement) {
        if (statement instanceof SubFunctionCallStatement) {
            SubFunctionCallStatement subFunctionCallStatement = (SubFunctionCallStatement)statement;
            if (subFunctionCallStatement.getSubFunction() == SubFunction.REMINDER_CREATE_TASK) {
                if (event.getSenderId() != botService.getAdminAccount()) {
                    botService.sendToGroup(event.getGroupId(), (new At(event.getSenderId())).plus("你没有该操作的权限！"));
                    return true;
                }
                ReminderTask task = createTask(
                        Integer.valueOf(subFunctionCallStatement.getArgs().get(0)), 
                        Integer.valueOf(subFunctionCallStatement.getArgs().get(1)), 
                        Integer.valueOf(subFunctionCallStatement.getArgs().get(2)), 
                        Integer.valueOf(subFunctionCallStatement.getArgs().get(3)), 
                        Integer.valueOf(subFunctionCallStatement.getArgs().get(4)),  
                        Integer.valueOf(subFunctionCallStatement.getArgs().get(5)), 
                        subFunctionCallStatement.getArgs().get(6), 
                        event.getGroupId()
                        );
                if (task != null) {
                    taskRepository.insert(task);
                    botService.sendToGroup(event.getGroupId(), (new At(event.getSenderId())).plus("创建成功"));
                } else {
                    botService.sendToGroup(event.getGroupId(), (new At(event.getSenderId())).plus("创建失败"));
                }
                
                return true;
            } else if (subFunctionCallStatement.getSubFunction() == SubFunction.REMINDER_REMOVE_TASK) {
                if (event.getSenderId() != botService.getAdminAccount()) {
                    botService.sendToGroup(event.getGroupId(), (new At(event.getSenderId())).plus("你没有该操作的权限！"));
                    return true;
                }
                String targetId = subFunctionCallStatement.getArgs().get(0);
                if (taskRepository.existsById(targetId)) {
                    taskRepository.deleteById(targetId);
                    botService.sendToGroup(event.getGroupId(), (new At(event.getSenderId())).plus("删除成功！"));
                    return true;
                } else {
                    botService.sendToGroup(event.getGroupId(), (new At(event.getSenderId())).plus("删除失败，不存在该数据！"));
                    return true;
                }
                
            } else if (subFunctionCallStatement.getSubFunction() == SubFunction.REMINDER_LIST_TASK) {
                List<ReminderTask> tasks = taskRepository.findAllByTargetGroup(event.getGroupId());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("提醒任务列表：\n");
                tasks.forEach(task -> stringBuilder.append(reminderTaskDescroption(task)).append("\n------\n"));
                botService.sendToGroup(event.getGroupId(), stringBuilder.toString());
                return true;
            }
        }
        return false;
    }
    
    public void registerClockSchedule() {
        scheduler.scheduleAtFixedRate(new Runnable() {
            
            @Override
            public void run() {
                
                checkCharacterTasks();
                checkUserTasks();
            }
        }, 1, 1, TimeUnit.MINUTES);
    }
    
    
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