package com.zaca.stillstanding.dto.event;

import com.zaca.stillstanding.dto.match.AnswerType;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class AnswerResultEvent extends MatchEvent {
    AnswerType result;
    int addScore;
    String addScoreTeamName;
}
