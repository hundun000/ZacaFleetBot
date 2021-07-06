package hundun.zacafleetbot.mirai.botlogic.core.function.reminder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import hundun.zacafleetbot.mirai.botlogic.core.SettingManager;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.BlackBoard;
import hundun.zacafleetbot.mirai.botlogic.core.behaviourtree.ProcessResult;
import hundun.zacafleetbot.mirai.botlogic.core.data.EventInfo;
import hundun.zacafleetbot.mirai.botlogic.core.data.SessionId;
import hundun.zacafleetbot.mirai.botlogic.core.data.configuration.GroupConfig;
import hundun.zacafleetbot.mirai.botlogic.core.function.BaseFunction;
import hundun.zacafleetbot.mirai.botlogic.core.function.SubFunction;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.Statement;
import hundun.zacafleetbot.mirai.botlogic.core.parser.statement.SubFunctionCallStatement;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.MessageChain;

/**
 * @author hundun
 * Created on 2021/05/07
 */
@Slf4j
@Component
public class ReminderFunction extends BaseFunction {
    @Autowired
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
        
    @Autowired
    SettingManager settingManager;

    @PostConstruct
    public void manualWired() {

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
        return createTask(-1, -1, -1, hourCondition, minuteCondition, -1, text, -1, -1);
    }
    
    public ReminderTask createTask(
            int monthCondition,
            int dayOfMonthCondition,
            int dayOfWeekCondition,
            int hourCondition,
            int minuteCondition,
            int count,
            String text,
            long targetGroup,
            long botId
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
            task.botId = botId;
        } catch (Exception e) {
            log.error("addTask error:", e);
            return null;
        }
        
        
        return task;
    }
    
    @Override
    public ProcessResult process(BlackBoard blackBoard) {
        SessionId sessionId = blackBoard.getSessionId(); 
        EventInfo event = blackBoard.getEvent(); 
        Statement statement = blackBoard.getStatement();
        
        if (statement instanceof SubFunctionCallStatement) {
            SubFunctionCallStatement subFunctionCallStatement = (SubFunctionCallStatement)statement;
            if (subFunctionCallStatement.getSubFunction() == SubFunction.REMINDER_CREATE_TASK) {
                if (event.getSenderId() != settingManager.getAdminAccount(event.getBot().getId())) {
                    console.sendToGroup(event.getBot(), event.getGroupId(), (new At(event.getSenderId())).plus("你没有该操作的权限！"));
                    return new ProcessResult(this, true);
                }
                ReminderTask task = createTask(
                        Integer.valueOf(subFunctionCallStatement.getArgs().get(0)), 
                        Integer.valueOf(subFunctionCallStatement.getArgs().get(1)), 
                        Integer.valueOf(subFunctionCallStatement.getArgs().get(2)), 
                        Integer.valueOf(subFunctionCallStatement.getArgs().get(3)), 
                        Integer.valueOf(subFunctionCallStatement.getArgs().get(4)),  
                        Integer.valueOf(subFunctionCallStatement.getArgs().get(5)), 
                        subFunctionCallStatement.getArgs().get(6), 
                        event.getGroupId(),
                        event.getBot().getId()
                        );
                if (task != null) {
                    taskRepository.save(task);
                    console.sendToGroup(event.getBot(), event.getGroupId(), (new At(event.getSenderId())).plus("创建成功"));
                } else {
                    console.sendToGroup(event.getBot(), event.getGroupId(), (new At(event.getSenderId())).plus("创建失败"));
                }
                
                return new ProcessResult(this, true);
            } else if (subFunctionCallStatement.getSubFunction() == SubFunction.REMINDER_REMOVE_TASK) {
                if (event.getSenderId() != settingManager.getAdminAccount(event.getBot().getId())) {
                    console.sendToGroup(event.getBot(), event.getGroupId(), (new At(event.getSenderId())).plus("你没有该操作的权限！"));
                    return new ProcessResult(this, true);
                }
                String targetId = subFunctionCallStatement.getArgs().get(0);
                if (taskRepository.existsById(targetId)) {
                    taskRepository.deleteById(targetId);
                    console.sendToGroup(event.getBot(), event.getGroupId(), (new At(event.getSenderId())).plus("删除成功！"));
                    return new ProcessResult(this, true);
                } else {
                    console.sendToGroup(event.getBot(), event.getGroupId(), (new At(event.getSenderId())).plus("删除失败，不存在该数据！"));
                    return new ProcessResult(this, true);
                }
                
            } else if (subFunctionCallStatement.getSubFunction() == SubFunction.REMINDER_LIST_TASK) {
                List<ReminderTask> tasks = taskRepository.findAllByTargetGroup(event.getGroupId());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("提醒任务列表：\n");
                tasks.forEach(task -> stringBuilder.append(reminderTaskDescroption(task)).append("\n------\n"));
                console.sendToGroup(event.getBot(), event.getGroupId(), stringBuilder.toString());
                return new ProcessResult(this, true);
            }
        }
        return new ProcessResult(this, false);
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
        Collection<Bot> bots = console.getBots();
        for (Bot bot: bots) {
            for (GroupConfig entry : settingManager.getGroupConfigsOrEmpty(bot.getId())) {
                Long groupId = entry.getGroupId();
                List<String> characterIds = entry.getEnableCharacters();
                log.info("checkCharacterTasks for groupId = {}", groupId);
                for (String characterId : characterIds) {
                    List<ReminderTask> tasks = characterTasks.get(characterId);
                    if (tasks != null) {
                        for (ReminderTask task : tasks) {
                            boolean triggered = checkTask(task, now);
                            if (triggered) {
                                console.sendToGroup(bot, groupId, task.text);
                            }
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
                String miraiCode = task.text;
                log.info("build MessageChain by miraiCode = {}", miraiCode);
                MessageChain chain = MiraiCode.deserializeMiraiCode(miraiCode);
                
                Bot bot = console.getBotOrNull(task.getBotId());
                if (bot != null) {
                    console.sendToGroup(bot, task.targetGroup, chain);
                }
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

    public void addAllCharacterTasks(String characterId, Map<String, String> hourlyChats) {
        for (Entry<String, String> entry : hourlyChats.entrySet()) {
            try {
                int hour = Integer.valueOf(entry.getKey());
                String chat = entry.getValue();
                this.addCharacterTasks(characterId, this.createCharacterEverydayChatTask(hour, 0, chat));
            } catch (Exception e) {
                console.getLogger().warning("addAllCharacterTasks fail: " + entry.toString() + ", e = " + e.getMessage());
            }
            
        }
    }
    

}
