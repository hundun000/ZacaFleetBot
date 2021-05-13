package com.zaca.stillstanding.dto.team;

import java.util.List;

import lombok.Data;

/**
 * @author hundun
 * Created on 2021/05/08
 */
@Data
public class TeamConstInfoDTO {
    private String name;
    private List<String> pickTags;
    private List<String> banTags;
    private String roleName;
}
