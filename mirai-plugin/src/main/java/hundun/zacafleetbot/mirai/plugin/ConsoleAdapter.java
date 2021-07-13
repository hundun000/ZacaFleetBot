package hundun.zacafleetbot.mirai.plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import hundun.zacafleetbot.mirai.botlogic.core.BaseBotLogic;
import hundun.zacafleetbot.mirai.botlogic.export.IConsole;
import hundun.zacafleetbot.mirai.plugin.export.MyPlugin;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListenerHost;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;
import net.mamoe.mirai.utils.MiraiLogger;

/**
 * @author hundun
 * Created on 2021/06/03
 */
@Slf4j
public class ConsoleAdapter implements IConsole, ListenerHost {

    private static final String offLineImageFakeId = "{01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.jpg";
    
    /**
     * 为null表示插件未启用
     */
    @Nullable
    BaseBotLogic botLogic;
    
    JvmPlugin plugin;
    
    
    public ConsoleAdapter(MyPlugin myPlugin) {
        this.plugin = myPlugin;
        
    }
    
    public void enablePluginAndSetBotLogic(BaseBotLogic botLogic) {
        this.botLogic = botLogic;
    }
    
    public void disablePluginAndClearBotLogic() {
        if (botLogic != null) {
            botLogic.onDisable();
            botLogic = null;
        }
    }
    

    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull NudgeEvent event) throws Exception { 
        if (botLogic == null) {
            return ListeningStatus.LISTENING;
        }
        return botLogic.onMessage(event);
    }
    
    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull GroupMessageEvent event) throws Exception { 
        if (botLogic == null) {
            return ListeningStatus.LISTENING;
        }
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
            Image image = bot.getGroupOrFail(groupId).uploadImage(externalResource);
            try {
                externalResource.close();
            } catch (IOException e) {
                getLogger().error("Image externalResource.close error" + e.getMessage());
            }
            return image;
        } else {
            log.info("[offline mode]uploadImage groupId = {}", groupId);
            return Image.fromId(offLineImageFakeId);
        }
    }

    @Override
    public Voice uploadVoice(Bot bot, long groupId, ExternalResource externalResource) {
        if (bot != null) {
            Voice voice = bot.getGroupOrFail(groupId).uploadVoice(externalResource);
            try {
                externalResource.close();
            } catch (IOException e) {
                getLogger().error("Voice externalResource.close error" + e.getMessage());
            }
            return voice;
        } else {
            log.info("[offline mode]uploadVoice groupId = {}", groupId);
            return new Voice("", new byte[1], 0, 0, "");
        }
    }
    
    @Override
    public List<Bot> getBots() {
        return Bot.getInstances();
    }

    @Override
    public Bot getBotOrNull(long botId) {
        return Bot.getInstanceOrNull(botId);
    }

    @Override
    public File resolveDataFile(String subPathName) {
        return plugin.resolveDataFile(subPathName);
    }

    @Override
    public File resolveConfigFile(String subPathName) {
        return plugin.resolveConfigFile(subPathName);
    }

    @Override
    public MiraiLogger getLogger() {
        return plugin.getLogger();
    }



}
