package com.zaca.stillstanding.dto.role;

import java.util.List;


import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/13
 */
@Data
public class RoleConstInfoDTO {
    String name;
    String description;
    List<String> skillNames;
    List<String> skillDescriptions;
    List<Integer> skillFullCounts;
}
