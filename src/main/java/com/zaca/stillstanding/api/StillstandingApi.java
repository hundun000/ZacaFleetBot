package com.zaca.stillstanding.api;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.zaca.stillstanding.dto.ApiResult;
import com.zaca.stillstanding.dto.match.MatchConfigDTO;
import com.zaca.stillstanding.dto.match.MatchSituationDTO;
import com.zaca.stillstanding.dto.team.TeamConstInfoDTO;

/**
 * @author hundun
 * Created on 2021/05/10
 */
public interface StillstandingApi {

    @RequestMapping(
            value = "/createEndlessMatch", 
            method = RequestMethod.POST
            )
    ApiResult<MatchSituationDTO> createEndlessMatch(
            @RequestBody MatchConfigDTO matchConfigDTO
            );
    
    @RequestMapping(
            value = "/createPreMatch", 
            method = RequestMethod.POST
            )
    ApiResult<MatchSituationDTO> createPreMatch(
            @RequestBody MatchConfigDTO matchConfigDTO
            );
    
    @RequestMapping(
            value = "/createMainMatch", 
            method = RequestMethod.POST
            )
    ApiResult<MatchSituationDTO> createMainMatch(
            @RequestBody MatchConfigDTO matchConfigDTO
            );
    
    @RequestMapping(
            value = "/start", 
            method = RequestMethod.POST
    )
    ApiResult<MatchSituationDTO> start(@RequestParam(value = "sessionId") String sessionId);
    
    @RequestMapping(
            value = "/nextQustion", 
            method = RequestMethod.POST
    )
    ApiResult<MatchSituationDTO> nextQustion(
            @RequestParam(value = "sessionId") String sessionId
            );
    
    @RequestMapping(
            value = "/answer", 
            method = RequestMethod.POST
    )
    ApiResult<MatchSituationDTO> teamAnswer(
            @RequestParam(value = "sessionId") String sessionId,
            @RequestParam(value = "answer") String answer
            );
    
    @RequestMapping(value="/use-skill", method=RequestMethod.POST)
    ApiResult<MatchSituationDTO> teamUseSkill(
            @RequestParam(value = "sessionId") String sessionId,
            @RequestParam(value = "skillName") String skillName
            );
    
    @RequestMapping(
            value = "/listTeams", 
            method = RequestMethod.GET
            )
    ApiResult<List<TeamConstInfoDTO>> listTeams(
            
            );
    
    
    @RequestMapping(value="/updateTeam", method=RequestMethod.POST)
    ApiResult<List<TeamConstInfoDTO>> updateTeam(
            @RequestBody TeamConstInfoDTO teamConstInfoDTO
            );
}
