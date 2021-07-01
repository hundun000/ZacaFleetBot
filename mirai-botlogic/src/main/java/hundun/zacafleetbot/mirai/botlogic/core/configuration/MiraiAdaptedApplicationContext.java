package hundun.zacafleetbot.mirai.botlogic.core.configuration;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author hundun
 * Created on 2021/07/01
 */
public class MiraiAdaptedApplicationContext extends AnnotationConfigApplicationContext {
    
    public MiraiAdaptedApplicationContext(boolean lateRefresh) {
        super();
        this.setClassLoader(this.getClass().getClassLoader());
        this.scan("hundun.zacafleetbot.mirai.botlogic");
        if (!lateRefresh) {
            this.refresh();
        }
    }
}
