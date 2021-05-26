package com.zaca.stillstanding.api;

import java.util.List;


import com.zaca.stillstanding.dto.ApiResult;
import com.zaca.stillstanding.dto.match.MatchConfigDTO;
import com.zaca.stillstanding.dto.match.MatchSituationDTO;
import com.zaca.stillstanding.dto.team.TeamConstInfoDTO;

/**
 * @author hundun
 * Created on 2021/05/10
 */
public interface StillstandingApi {

    ApiResult<MatchSituationDTO> createEndlessMatch(
            MatchConfigDTO matchConfigDTO
            );
    
    ApiResult<MatchSituationDTO> createPreMatch(
            MatchConfigDTO matchConfigDTO
            );
    
    ApiResult<MatchSituationDTO> createMainMatch(
            MatchConfigDTO matchConfigDTO
            );
            
    
    ApiResult<MatchSituationDTO> start(String sessionId);
    
    ApiResult<MatchSituationDTO> nextQustion(
            String sessionId
            );
    
    ApiResult<MatchSituationDTO> teamAnswer(
            String sessionId,
            String answer
            );
    
    ApiResult<MatchSituationDTO> teamUseSkill(
            String sessionId,
            String skillName
            );
    
    ApiResult<List<TeamConstInfoDTO>> listTeams(
            
            );
    
    ApiResult<List<TeamConstInfoDTO>> updateTeam(
            TeamConstInfoDTO teamConstInfoDTO
            );
}
