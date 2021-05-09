package com.zaca.stillstanding.domain.dto.event;

import java.util.List;

import com.zaca.stillstanding.domain.dto.AnswerType;
import com.zaca.stillstanding.domain.dto.MatchEvent;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class SkillResultEvent extends MatchEvent {
    String teamName;
    String roleName;
    String skillName;
    String skillDesc;
    int skillRemainTime;
    List<String> args;
}
