package com.hundun.mirai.server;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;

import com.hundun.mirai.bot.core.BaseBotLogic;
import com.hundun.mirai.bot.core.CustomBeanFactory;
import com.hundun.mirai.bot.core.data.configuration.AppPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.BotPrivateSettings;
import com.hundun.mirai.bot.core.data.configuration.PublicSettings;
import com.hundun.mirai.bot.export.BotLogicOfCharacterRouterAsEventHandler;
import com.hundun.mirai.bot.export.IConsole;
import com.hundun.mirai.bot.helper.Utils;
import com.hundun.mirai.bot.helper.file.FileOperationDelegate;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.ExternalResource;
import net.mamoe.mirai.utils.MiraiLogger;
import net.mamoe.mirai.utils.MiraiLoggerPlatformBase;

/**
 * @author hundun
 * Created on 2021/06/03
 */
@Slf4j
public class SpringConsole implements IConsole, ListenerHost {
    public static final String MIRAI_CONSOLE_CONFIG_FOLDER = "mirai_console_config/";
    public static final String MIRAI_CONSOLE_DATA_FOLDER = "mirai_console_data/";
    public static final String MIRAI_CONSOLE_BOTS_FOLDER = "mirai_console_bots/";
    
    private static final String offLineImageFakeId = "{01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.jpg";

    
    //private Bot miraiBot;
    

    
    BaseBotLogic botLogic;
    
    
  //设备认证信息文件
    //private final String deviceInfoPath = "device.json";
    
    
    public SpringConsole() {
        
        
        try {
            this.botLogic = new BotLogicOfCharacterRouterAsEventHandler(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        
    }
    
    
    private class BotThread extends Thread {
        BotPrivateSettings botPrivateSettings;
        public BotThread(BotPrivateSettings botPrivateSettings) {
            this.botPrivateSettings = botPrivateSettings;
        }
        
        @Override
        public void run(){
            String accountWorkDirPathName = Utils.checkFolder(String.valueOf(botPrivateSettings.getBotAccount()), MIRAI_CONSOLE_BOTS_FOLDER);
            File accountWorkDir = new File(accountWorkDirPathName);
            String deviceInfoFileName = "device.json";
            Bot miraiBot = BotFactory.INSTANCE.newBot(botPrivateSettings.getBotAccount(), botPrivateSettings.getBotPwd(), new BotConfiguration() {
                {
                    setWorkingDir(accountWorkDir);
                    fileBasedDeviceInfo(deviceInfoFileName);
                    setProtocol(MiraiProtocol.ANDROID_PHONE);
                    enableContactCache();
                }
            });
            miraiBot.login();

            miraiBot.join();

        }
    }
    
    public String logout(long botAccount) {
        

        Bot.getInstance(botAccount).close();
        
        return "OK";
    }
    
    public String login(long botAccount) {
        Collection<BotPrivateSettings> allBotPrivateSettings = botLogic.appPrivateSettings.getBotPrivateSettingsList();
        BotPrivateSettings targetBotPrivateSettings = null;
        for (BotPrivateSettings botPrivateSettings : allBotPrivateSettings) {
            if (botPrivateSettings.getBotAccount() == botAccount) {
                targetBotPrivateSettings = botPrivateSettings;
                break;
            }
        }
        
        if (targetBotPrivateSettings == null) {
            log.warn("登录时 botAccount = {} 未找到 BotPrivateSettings", botAccount);
            return "botAccount未找到 BotPrivateSettings";
        }
        
        
        
        
        // the new thread will blocked by Bot.join()
        Thread thread = new BotThread(targetBotPrivateSettings);
        thread.start();
        
        return "OK";
    }

    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull NudgeEvent event) throws Exception { 
        return botLogic.onMessage(event);
    }
    
    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull GroupMessageEvent event) throws Exception { 
        return botLogic.onMessage(event);
    }


    @Override
    public void sendToGroup(Bot bot, long groupId, MessageChain messageChain) {
        if (bot != null) {
            bot.getGroupOrFail(groupId).sendMessage(messageChain);
        } else {
            log.info("[offline mode]sendToGroup groupId = {}, messageChain = {}", groupId, messageChain.serializeToMiraiCode());
        }
    }

    @Override
    public Image uploadImage(Bot bot, long groupId, ExternalResource externalResource) {
        if (bot != null) {
            return bot.getGroupOrFail(groupId).uploadImage(externalResource);
        } else {
            log.info("[offline mode]uploadImage groupId = {}", groupId);
            return Image.fromId(offLineImageFakeId);
        }
    }

    @Override
    public Voice uploadVoice(Bot bot, long groupId, ExternalResource externalResource) {
        if (bot != null) {
            return bot.getGroupOrFail(groupId).uploadVoice(externalResource);
        } else {
            log.info("[offline mode]uploadVoice groupId = {}", groupId);
            return new Voice("", new byte[1], 0, 0, "");
        }
    }

    
    @Override
    public Collection<Bot> getBots() {
        return Bot.getInstances();
    }

    @Override
    public Bot getBotOrNull(long botId) {
        return Bot.getInstanceOrNull(botId);
    }

    @Override
    public File resolveDataFile(String subPathName) {
        return new File(MIRAI_CONSOLE_DATA_FOLDER + subPathName);
    }

    @Override
    public File resolveConfigFile(String subPathName) {
        return new File(MIRAI_CONSOLE_CONFIG_FOLDER + subPathName);
    }

    @Override
    public MiraiLogger getLogger() {
        return MyLogger.INSTANCE;
    }
    
    static class MyLogger extends MiraiLoggerPlatformBase {

        static MyLogger INSTANCE = new MyLogger();

        @Override
        public String getIdentity() {
            return "SpringConsoleLogger";
        }

        @Override
        protected void debug0(String arg0, Throwable arg1) {
            log.debug(arg0, arg1);
        }

        @Override
        protected void error0(String arg0, Throwable arg1) {
            log.error(arg0, arg1);
        }

        @Override
        protected void info0(String arg0, Throwable arg1) {
            log.info(arg0, arg1);
        }

        @Override
        protected void verbose0(String arg0, Throwable arg1) {
            log.info(arg0, arg1);
        }

        @Override
        protected void warning0(String arg0, Throwable arg1) {
            log.warn(arg0, arg1);
        }
        
        
        
    }
}
