package com.zaca.stillstanding.domain.dto;

import java.util.Map;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class TeamRuntimeInfoDTO {
    String name;
    String roleName;
    int matchScore;
    Map<String, Integer> skillRemainTimes;
    Map<String, Integer> buffs;
    boolean alive;
}
