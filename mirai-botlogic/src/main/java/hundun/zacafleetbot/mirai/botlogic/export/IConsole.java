package hundun.zacafleetbot.mirai.botlogic.export;

import java.io.File;
import java.util.Collection;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;
import net.mamoe.mirai.utils.MiraiLogger;

/**
 * @author hundun
 * Created on 2021/06/03
 */
public interface IConsole {
    
    public static final String RESOURCE_DOWNLOAD_FOLDER = "file_cache";
    
    default void sendToGroup(Bot bot, long groupId, String message) {
        sendToGroup(bot, groupId, new MessageChainBuilder().append(message).asMessageChain());
    }
    public void sendToGroup(Bot bot, long groupId, MessageChain messageChain);
    
    public Image uploadImage(Bot bot, long groupId, ExternalResource externalResource);
    public Voice uploadVoice(Bot bot, long groupId, ExternalResource externalResource);
    
    File resolveDataFile(String subPathName);
    File resolveConfigFile(String subPathName);
    default File resolveDataFileOfFileCache() {
        return resolveDataFile(RESOURCE_DOWNLOAD_FOLDER);
    }
    
    Collection<Bot> getBots();
    Bot getBotOrNull(long botId);
    MiraiLogger getLogger();
    
}
