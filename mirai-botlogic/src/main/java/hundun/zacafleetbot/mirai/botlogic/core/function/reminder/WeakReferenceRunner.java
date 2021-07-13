package hundun.zacafleetbot.mirai.botlogic.core.function.reminder;

import java.lang.ref.WeakReference;

/**
 * @author hundun
 * Created on 2021/07/14
 */
public class WeakReferenceRunner implements Runnable {

    WeakReference<ReminderFunction> parentReference;
    
    public WeakReferenceRunner(ReminderFunction parent) {
        this.parentReference = new WeakReference<>(parent);
    }
    
    @Override
    public void run() {
        parentReference.get().checkCharacterTasks();
        parentReference.get().checkUserTasks();
        //parentReference.get().testGetConsole().getLogger().info("one second pass");
    }
    
}
