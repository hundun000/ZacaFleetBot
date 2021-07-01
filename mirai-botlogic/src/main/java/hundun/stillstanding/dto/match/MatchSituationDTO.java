package hundun.stillstanding.dto.match;
/**
 * @author hundun
 * Created on 2021/05/08
 */

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import hundun.stillstanding.dto.event.AnswerResultEvent;
import hundun.stillstanding.dto.event.FinishEvent;
import hundun.stillstanding.dto.event.SkillResultEvent;
import hundun.stillstanding.dto.event.StartMatchEvent;
import hundun.stillstanding.dto.event.SwitchQuestionEvent;
import hundun.stillstanding.dto.event.SwitchTeamEvent;
import hundun.stillstanding.dto.question.QuestionDTO;
import hundun.stillstanding.dto.team.TeamRuntimeInfoDTO;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchSituationDTO {
    String id;
    QuestionDTO question;
    int currentTeamIndex;
    List<TeamRuntimeInfoDTO> teamRuntimeInfos;
    MatchState state;
    
    AnswerResultEvent answerResultEvent;
    SkillResultEvent skillResultEvent;
    SwitchQuestionEvent switchQuestionEvent;
    SwitchTeamEvent switchTeamEvent;
    FinishEvent finishEvent;
    StartMatchEvent startMatchEvent;
}
