package hundun.zacafleetbot.mirai.botlogic.core.function.reminder;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


/**
 * @author hundun
 * Created on 2021/05/07
 */
@Repository
public interface RemiderTaskRepository extends MongoRepository<ReminderTask, String> {
    List<ReminderTask> findAllByTargetGroup(long targetGroup);
}
