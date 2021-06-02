package com.zaca.stillstanding.dto.event;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/13
 */
@Data
public class SwitchQuestionEvent extends MatchEvent {
    int time;
}
