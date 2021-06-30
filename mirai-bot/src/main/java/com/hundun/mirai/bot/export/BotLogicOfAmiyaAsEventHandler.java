package com.hundun.mirai.bot.export;

import com.hundun.mirai.bot.core.BaseBotLogic;
import com.hundun.mirai.bot.core.character.Amiya;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/06/09
 */
@Slf4j
public class BotLogicOfAmiyaAsEventHandler extends BaseBotLogic {
    
    public BotLogicOfAmiyaAsEventHandler(
            IConsole console
            ) throws Exception {
        super(console, Amiya.class);
        
        
    }
   

    
}
