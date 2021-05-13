package com.zaca.stillstanding.dto.match;
/**
 * @author hundun
 * Created on 2021/05/08
 */

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zaca.stillstanding.dto.event.AnswerResultEvent;
import com.zaca.stillstanding.dto.event.FinishEvent;
import com.zaca.stillstanding.dto.event.SkillResultEvent;
import com.zaca.stillstanding.dto.event.StartMatchEvent;
import com.zaca.stillstanding.dto.event.SwitchQuestionEvent;
import com.zaca.stillstanding.dto.event.SwitchTeamEvent;
import com.zaca.stillstanding.dto.question.QuestionDTO;
import com.zaca.stillstanding.dto.team.TeamRuntimeInfoDTO;

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
