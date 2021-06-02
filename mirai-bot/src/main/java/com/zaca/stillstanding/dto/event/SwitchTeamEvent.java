package com.zaca.stillstanding.dto.event;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/10
 */
@Data
public class SwitchTeamEvent extends MatchEvent {
    String fromTeamName;
    String toTeamName;
}
