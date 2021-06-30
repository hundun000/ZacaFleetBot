package com.hundun.mirai.bot.export;

import com.hundun.mirai.bot.core.BaseBotLogic;
import com.hundun.mirai.bot.core.CharacterRouter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/06/09
 */
@Slf4j
public class BotLogicOfCharacterRouterAsEventHandler extends BaseBotLogic {
    
    public BotLogicOfCharacterRouterAsEventHandler(
            IConsole console
            ) throws Exception {
        super(console, CharacterRouter.class);

    }


    
}
