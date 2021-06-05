package com.hundun.mirai.plugin;

import com.hundun.mirai.bot.configuration.PrivateSettings;
import com.hundun.mirai.bot.service.CharacterRouter;
import com.hundun.mirai.bot.service.IConsole;

import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.Voice;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/06/03
 */
@Slf4j
public class ConsoleAdapter implements IConsole {

    private static final String offLineImageFakeId = "{01E9451B-70ED-EAE3-B37C-101F1EEBF5B5}.jpg";
    
    boolean isBotOnline = false;
    
    PrivateSettings privateSettings;
    
    CharacterRouter characterRouter;
    
    public ConsoleAdapter(PrivateSettings privateSettings) {
        this.privateSettings = privateSettings;
    }
    
    public void lateInitCharacterRouter(CharacterRouter characterRouter) {
        this.characterRouter = characterRouter;
    }
    
    
    @Override
    public void sendToGroup(Long groupId, String message) {
        if (isBotOnline) {
            // TODO
        } else {
            log.info("[offline mode]sendToGroup groupId = {}, message = {}", groupId, message);
        }
    }

    @Override
    public void sendToGroup(long groupId, MessageChain messageChain) {
        if (isBotOnline) {
            // TODO
        } else {
            log.info("[offline mode]sendToGroup groupId = {}, messageChain = {}", groupId, messageChain.serializeToMiraiCode());
        }
    }

    @Override
    public Image uploadImage(Long groupId, ExternalResource externalResource) {
        if (isBotOnline) {
            // TODO
            return null;
        } else {
            log.info("[offline mode]uploadImage groupId = {}", groupId);
            return Image.fromId(offLineImageFakeId);
        }
    }

    @Override
    public Voice uploadVoice(Long groupId, ExternalResource externalResource) {
        if (isBotOnline) {
         // TODO
            return null;
        } else {
            log.info("[offline mode]uploadVoice groupId = {}", groupId);
            return new Voice("", new byte[1], 0, 0, "");
        }
    }

    @Override
    public long getSelfAccount() {
        return privateSettings.getBotAccount();
    }

    @Override
    public Long getAdminAccount() {
        return privateSettings.getBotAccount();
    }

}
