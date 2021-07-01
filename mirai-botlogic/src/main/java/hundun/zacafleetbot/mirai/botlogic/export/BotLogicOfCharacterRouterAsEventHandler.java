package hundun.zacafleetbot.mirai.botlogic.export;

import hundun.zacafleetbot.mirai.botlogic.core.BaseBotLogic;
import hundun.zacafleetbot.mirai.botlogic.core.CharacterRouter;
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
