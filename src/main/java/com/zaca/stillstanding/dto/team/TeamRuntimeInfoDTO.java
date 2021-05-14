package com.zaca.stillstanding.dto.team;

import java.util.List;
import java.util.Map;

import com.zaca.stillstanding.dto.buff.BuffDTO;

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
    List<BuffDTO> buffs;
    boolean alive;
    int health;
}
