package hundun.zacafleetbot.mirai.botlogic.core.data.configuration.quiz;

import java.util.ArrayList;
import java.util.List;

import hundun.quizgame.core.model.team.TeamPrototype;
import lombok.Data;

/**
 * @author hundun
 * Created on 2021/07/14
 */
@Data
public class QuizConfig {
    List<String> builtInTeamNames = new ArrayList<>();
}
