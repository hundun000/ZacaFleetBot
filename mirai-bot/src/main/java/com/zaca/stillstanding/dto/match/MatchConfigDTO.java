package com.zaca.stillstanding.dto.match;

import java.util.List;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/08
 */
@Data
public class MatchConfigDTO {
    List<String> teamNames;
    String questionPackageName;
}
