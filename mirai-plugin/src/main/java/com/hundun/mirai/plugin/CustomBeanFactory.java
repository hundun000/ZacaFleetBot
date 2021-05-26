package com.hundun.mirai.plugin;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.hundun.mirai.plugin.character.Amiya;
import com.hundun.mirai.plugin.character.Neko;
import com.hundun.mirai.plugin.character.PrinzEugen;
import com.hundun.mirai.plugin.character.ZacaMusume;
import com.hundun.mirai.plugin.configuration.PrivateSettings;
import com.hundun.mirai.plugin.cp.kcwiki.KancolleWikiService;
import com.hundun.mirai.plugin.cp.kcwiki.feign.KcwikiApiFeignClient;
import com.hundun.mirai.plugin.cp.penguin.ItemRepository;
import com.hundun.mirai.plugin.cp.penguin.MatrixReportRepository;
import com.hundun.mirai.plugin.cp.penguin.PenguinService;
import com.hundun.mirai.plugin.cp.penguin.StageInfoReportRepository;
import com.hundun.mirai.plugin.cp.penguin.StageRepository;
import com.hundun.mirai.plugin.cp.penguin.feign.PenguinApiService;
import com.hundun.mirai.plugin.cp.quiz.QuizService;
import com.hundun.mirai.plugin.cp.weibo.WeiboCardCacheRepository;
import com.hundun.mirai.plugin.cp.weibo.WeiboService;
import com.hundun.mirai.plugin.cp.weibo.WeiboUserInfoCacheRepository;
import com.hundun.mirai.plugin.cp.weibo.feign.WeiboApiService;
import com.hundun.mirai.plugin.cp.weibo.feign.WeiboPictureApiService;
import com.hundun.mirai.plugin.function.AmiyaChatFunction;
import com.hundun.mirai.plugin.function.GuideFunction;
import com.hundun.mirai.plugin.function.JapaneseFunction;
import com.hundun.mirai.plugin.function.KancolleWikiQuickSearchFunction;
import com.hundun.mirai.plugin.function.MiraiCodeFunction;
import com.hundun.mirai.plugin.function.PenguinFunction;
import com.hundun.mirai.plugin.function.PrinzEugenChatFunction;
import com.hundun.mirai.plugin.function.QuickSearchFunction;
import com.hundun.mirai.plugin.function.QuizHandler;
import com.hundun.mirai.plugin.function.RepeatConsumer;
import com.hundun.mirai.plugin.function.WeiboFunction;
import com.hundun.mirai.plugin.function.reminder.RemiderTaskRepository;
import com.hundun.mirai.plugin.function.reminder.ReminderFunction;
import com.hundun.mirai.plugin.service.BotService;
import com.hundun.mirai.plugin.service.CharacterRouter;
import com.hundun.mirai.plugin.service.file.FileService;
import com.zaca.stillstanding.api.StillstandingApiFeignClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 一步步把SpringBean替换掉
 * @author hundun
 * Created on 2021/05/25
 */
@Slf4j
public class CustomBeanFactory {
    
    public static void main(String[] args) {
        
    }
    
    private static CustomBeanFactory instance;

    public static CustomBeanFactory getInstance() {
        return instance;
    }
    
    private List<IManualWired> beans = new ArrayList<>();
    
    public static void afterInit() {
        for (IManualWired bean : instance.beans) {
            try {
                bean.afterManualWired();
            } catch (Exception e) {
                log.error("Beans afterManualWired error: ", e);
            }
            
        }
    }
    
    static {
        instance = new CustomBeanFactory();
        
        StringBuilder namesBuilder = new StringBuilder();
        
        Class<?> clazz = CustomBeanFactory.class;   
        Field[] fields = clazz.getDeclaredFields();   
        for (Field field : fields) {
            if (IManualWired.class.isAssignableFrom(field.getType())) {
                try {
                    IManualWired bean = (IManualWired) field.get(instance);
                    instance.beans.add(bean);
                    namesBuilder.append(field.getClass().getSimpleName()).append(", ");
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }    
        }
        
        log.info("Beans: {}", namesBuilder.toString());
        
        for (IManualWired bean : instance.beans) {
            try {
                bean.manualWired();
            } catch (Exception e) {
                log.error("Beans manualWired error: ", e);
            }
        }
        
        

    }

    
    
    // TODO implement these interfaces
    public WeiboApiService weiboApiService;

    public WeiboPictureApiService weiboPictureApiService;

    public WeiboUserInfoCacheRepository userInfoCacheRepository;

    public WeiboCardCacheRepository cardCacheRepository;

    public StillstandingApiFeignClient stillstandingApiService;

    public StageRepository stageRepository;

    public ItemRepository itemRepository;

    public PenguinApiService penguinApiService;

    public MatrixReportRepository matrixReportRepository; 

    public StageInfoReportRepository stageInfoReportRepository;

    public KcwikiApiFeignClient apiFeignClient;

    public PrivateSettings privateSettings;

    public RemiderTaskRepository taskRepository;

    public Amiya amiya = new Amiya();
    public ZacaMusume zacaMusume = new ZacaMusume();
    public PrinzEugen prinzEugen = new PrinzEugen();
    public Neko neko = new Neko();
    
    public WeiboFunction weiboFunction = new WeiboFunction();
    public AmiyaChatFunction amiyaChatFunction = new AmiyaChatFunction();
    public QuizHandler quizHandler = new QuizHandler();
    public PenguinFunction penguinFunction = new PenguinFunction();
    public RepeatConsumer repeatConsumer = new RepeatConsumer();
    public ReminderFunction reminderFunction = new ReminderFunction();
    public QuickSearchFunction quickSearchFunction = new QuickSearchFunction();
    public MiraiCodeFunction miraiCodeFunction = new MiraiCodeFunction();
    public PrinzEugenChatFunction prinzEugenChatFunction = new PrinzEugenChatFunction();
    public KancolleWikiQuickSearchFunction kancolleWikiQuickSearchFunction = new KancolleWikiQuickSearchFunction();
    public JapaneseFunction japaneseFunction = new JapaneseFunction();
    public GuideFunction guideFunction = new GuideFunction();
    
    public CharacterRouter characterRouter = new CharacterRouter();
    public BotService botService = new BotService();
    public FileService fileService = new FileService();
    public WeiboService weiboService = new WeiboService();
    public QuizService quizService = new QuizService();
    public PenguinService penguinService = new PenguinService();
    public KancolleWikiService kancolleWikiService = new KancolleWikiService();

    

    

    
}
