package com.hundun.mirai.bot;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.hundun.mirai.bot.character.Amiya;
import com.hundun.mirai.bot.character.Neko;
import com.hundun.mirai.bot.character.PrinzEugen;
import com.hundun.mirai.bot.character.ZacaMusume;
import com.hundun.mirai.bot.configuration.PrivateSettings;
import com.hundun.mirai.bot.configuration.PublicSettings;
import com.hundun.mirai.bot.cp.kcwiki.KancolleWikiService;
import com.hundun.mirai.bot.cp.kcwiki.feign.KcwikiApiFeignClient;
import com.hundun.mirai.bot.cp.penguin.PenguinService;
import com.hundun.mirai.bot.cp.penguin.db.ItemRepository;
import com.hundun.mirai.bot.cp.penguin.db.ItemRepositoryImplement;
import com.hundun.mirai.bot.cp.penguin.db.MatrixReportRepository;
import com.hundun.mirai.bot.cp.penguin.db.MatrixReportRepositoryImplement;
import com.hundun.mirai.bot.cp.penguin.db.StageInfoReportRepository;
import com.hundun.mirai.bot.cp.penguin.db.StageInfoReportRepositoryImplement;
import com.hundun.mirai.bot.cp.penguin.db.StageRepository;
import com.hundun.mirai.bot.cp.penguin.db.StageRepositoryImplement;
import com.hundun.mirai.bot.cp.penguin.domain.Item;
import com.hundun.mirai.bot.cp.penguin.domain.Stage;
import com.hundun.mirai.bot.cp.penguin.domain.report.MatrixReport;
import com.hundun.mirai.bot.cp.penguin.domain.report.StageInfoReport;
import com.hundun.mirai.bot.cp.penguin.feign.PenguinApiFeignClient;
import com.hundun.mirai.bot.cp.quiz.QuizService;
import com.hundun.mirai.bot.cp.weibo.WeiboService;
import com.hundun.mirai.bot.cp.weibo.db.WeiboCardCacheRepository;
import com.hundun.mirai.bot.cp.weibo.db.WeiboCardCacheRepositoryImplement;
import com.hundun.mirai.bot.cp.weibo.db.WeiboUserInfoCacheRepository;
import com.hundun.mirai.bot.cp.weibo.db.WeiboUserInfoCacheRepositoryImplement;
import com.hundun.mirai.bot.cp.weibo.domain.WeiboCardCache;
import com.hundun.mirai.bot.cp.weibo.domain.WeiboUserInfoCache;
import com.hundun.mirai.bot.cp.weibo.feign.WeiboApiFeignClient;
import com.hundun.mirai.bot.cp.weibo.feign.WeiboPictureApiFeignClient;
import com.hundun.mirai.bot.db.CollectionSettings;
import com.hundun.mirai.bot.function.AmiyaChatFunction;
import com.hundun.mirai.bot.function.GuideFunction;
import com.hundun.mirai.bot.function.JapaneseFunction;
import com.hundun.mirai.bot.function.KancolleWikiQuickSearchFunction;
import com.hundun.mirai.bot.function.MiraiCodeFunction;
import com.hundun.mirai.bot.function.PenguinFunction;
import com.hundun.mirai.bot.function.PrinzEugenChatFunction;
import com.hundun.mirai.bot.function.QuickSearchFunction;
import com.hundun.mirai.bot.function.QuizHandler;
import com.hundun.mirai.bot.function.RepeatConsumer;
import com.hundun.mirai.bot.function.WeiboFunction;
import com.hundun.mirai.bot.function.reminder.RemiderTaskRepository;
import com.hundun.mirai.bot.function.reminder.RemiderTaskRepositoryImplement;
import com.hundun.mirai.bot.function.reminder.ReminderFunction;
import com.hundun.mirai.bot.function.reminder.ReminderTask;
import com.hundun.mirai.bot.service.BotService;
import com.hundun.mirai.bot.service.CharacterRouter;
import com.hundun.mirai.bot.service.file.FileService;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.zaca.stillstanding.api.StillstandingApiFeignClient;

import feign.Feign;
import feign.Logger;
import feign.Logger.Level;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.extern.slf4j.Slf4j;

/**
 * 一步步把SpringBean替换掉
 * @author hundun
 * Created on 2021/05/25
 */
@Slf4j
public class CustomBeanFactory {

    
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
    
    public static void init(PrivateSettings privateSettings, PublicSettings publicSettings) {
        instance = new CustomBeanFactory();
        instance.initSelf(privateSettings, publicSettings);
        instance.initFields();
    }
    
    private void initFields () {
        StringBuilder namesBuilder = new StringBuilder();
        
        Class<?> clazz = CustomBeanFactory.class;   
        Field[] fields = clazz.getDeclaredFields();   
        for (Field field : fields) {
            if (IManualWired.class.isAssignableFrom(field.getType())) {
                try {
                    IManualWired bean = (IManualWired) field.get(instance);
                    this.beans.add(bean);
                    namesBuilder.append(field.getClass().getSimpleName()).append(", ");
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }    
        }
        
        log.info("Beans: {}", namesBuilder.toString());
        
        for (IManualWired bean : this.beans) {
            try {
                bean.manualWired();
            } catch (Exception e) {
                log.error("Beans manualWired error: ", e);
            }
        }
        
        for (IManualWired bean : instance.beans) {
            try {
                bean.afterManualWired();
            } catch (Exception e) {
                log.error("Beans afterManualWired error: ", e);
            }
        }
    }
    
    private void initSelf (PrivateSettings privateSettings, PublicSettings publicSettings) {
        this.privateSettings = privateSettings;
        this.publicSettings = publicSettings;
        
        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        this.mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("mirai").withCodecRegistry(pojoCodecRegistry);
        // ----- weibo -----
        this.weiboUserInfoCacheRepository = new WeiboUserInfoCacheRepositoryImplement(new CollectionSettings<>(
                database, 
                "weiboUserInfoCache", 
                WeiboUserInfoCache.class, 
                "uid", 
                (item -> item.getUid()),
                ((item, id) -> item.setUid(id))
                ));
        this.weiboCardCacheRepository = new WeiboCardCacheRepositoryImplement(new CollectionSettings<>(
                database, 
                "weiboCardCache", 
                WeiboCardCache.class, 
                "itemid", 
                (item -> item.getItemid()),
                ((item, id) -> item.setItemid(id))
                ));
        // ----- pengin -----
        this.matrixReportRepository = new MatrixReportRepositoryImplement(new CollectionSettings<>(
                database, 
                "matrixReport", 
                MatrixReport.class, 
                "id", 
                (item -> item.getId()),
                ((item, id) -> item.setId(id))
                ));
        this.penguinStageRepository = new StageRepositoryImplement(new CollectionSettings<>(
                database, 
                "penguinStage", 
                Stage.class, 
                "stageId", 
                (item -> item.getStageId()),
                ((item, id) -> item.setStageId(id))
                ));
        this.penguinItemRepository = new ItemRepositoryImplement(new CollectionSettings<>(
                database, 
                "penguinItem", 
                Item.class, 
                "itemId", 
                (item -> item.getItemId()),
                ((item, id) -> item.setItemId(id))
                ));
        this.stageInfoReportRepository = new StageInfoReportRepositoryImplement(new CollectionSettings<>(
                database, 
                "penguinStageInfoReport", 
                StageInfoReport.class, 
                "stageCode", 
                (item -> item.getStageCode()),
                ((item, id) -> item.setStageCode(id))
                ));
        this.matrixReportRepository = new MatrixReportRepositoryImplement(new CollectionSettings<>(
                database, 
                "matrixReport", 
                MatrixReport.class, 
                "id", 
                (item -> item.getId()),
                ((item, id) -> item.setId(id))
                ));
        // ----- reminder -----
        this.reminderTaskRepository = new RemiderTaskRepositoryImplement(new CollectionSettings<>(
                database, 
                "reminderTask", 
                ReminderTask.class, 
                "id", 
                (item -> item.getId()),
                ((item, id) -> item.setId(id))
                ));
        
        
        
        Level feignLogLevel = Level.NONE;
        Logger feignLogger = new FeignLogger("feign", "debug");
        Encoder feignEncoder = new JacksonEncoder();
        Decoder feignDecoder = new JacksonDecoder();
        this.kcwikiApiFeignClient = Feign.builder()
                .encoder(feignEncoder)
                .decoder(feignDecoder)
                .logger(feignLogger)
                .logLevel(feignLogLevel)
                .target(KcwikiApiFeignClient.class, "http://api.kcwiki.moe")
                ;
        this.weiboApiFeignClient = Feign.builder()
                .encoder(feignEncoder)
                .decoder(feignDecoder)
                .logger(feignLogger)
                .logLevel(feignLogLevel)
                .target(WeiboApiFeignClient.class, "https://m.weibo.cn")
                ;
        this.weiboPictureApiFeignClient = Feign.builder()
                .encoder(feignEncoder)
                .decoder(feignDecoder)
                .logger(feignLogger)
                .logLevel(feignLogLevel)
                .target(WeiboPictureApiFeignClient.class, "https://wx2.sinaimg.cn")
                ;
        this.penguinApiFeignClient = Feign.builder()
                .encoder(feignEncoder)
                .decoder(feignDecoder)
                .logger(feignLogger)
                .logLevel(feignLogLevel)
                .target(PenguinApiFeignClient.class, "https://penguin-stats.io/PenguinStats/api")
                ;
        
        this.stillstandingApiFeignClient = Feign.builder()
                .encoder(feignEncoder)
                .decoder(feignDecoder)
                .logger(feignLogger)
                .logLevel(feignLogLevel)
                .target(StillstandingApiFeignClient.class, "http://localhost:10100/api/game")
                ;
    }
    
    public PrivateSettings privateSettings;
    public PublicSettings publicSettings;

    MongoClient mongoClient;
    
    // ----- weibo -----
    public WeiboApiFeignClient weiboApiFeignClient;
    public WeiboPictureApiFeignClient weiboPictureApiFeignClient;

    public WeiboUserInfoCacheRepository weiboUserInfoCacheRepository;
    public WeiboCardCacheRepository weiboCardCacheRepository;

    // ----- Stillstanding -----
    public StillstandingApiFeignClient stillstandingApiFeignClient;

    // ----- Penguin -----
    public PenguinApiFeignClient penguinApiFeignClient;
    
    public StageRepository penguinStageRepository;
    public ItemRepository penguinItemRepository;
    public MatrixReportRepository matrixReportRepository; 
    public StageInfoReportRepository stageInfoReportRepository;
    // ----- Kcwiki -----
    public KcwikiApiFeignClient kcwikiApiFeignClient;

    
    
    public RemiderTaskRepository reminderTaskRepository;

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
