package com.mirai.hundun.cp.quiz;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mirai.hundun.service.file.FileService;
import com.mirai.hundun.service.file.IFileProvider;
import com.zaca.stillstanding.api.StillstandingApiFeignClient;
import com.zaca.stillstanding.dto.ApiResult;
import com.zaca.stillstanding.dto.match.MatchConfigDTO;
import com.zaca.stillstanding.dto.match.MatchSituationDTO;
import com.zaca.stillstanding.dto.team.TeamConstInfoDTO;

import feign.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
@Service
public class QuizService implements IFileProvider {
    ObjectMapper mapper = new ObjectMapper();
    
    
    @Autowired
    FileService fileService;
    
    @Autowired
    StillstandingApiFeignClient stillstandingApiService;
    
    
    public QuizService() {
        mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
        
        
    }
    
    public enum MatchType {
        ENDLESS,
        PRE, MAIN,
        ;
    }
    
    public MatchSituationDTO createAndStartMatch(String questionPackageName, List<String> teamNames, MatchType type) {
        MatchConfigDTO matchConfigDTO = new MatchConfigDTO();
        matchConfigDTO.setTeamNames(teamNames);
        matchConfigDTO.setQuestionPackageName(questionPackageName);
        ApiResult<MatchSituationDTO> apiResult;
        if (type == MatchType.ENDLESS) {
            apiResult = stillstandingApiService.createEndlessMatch(matchConfigDTO);
        } else if (type == MatchType.PRE) {
            apiResult = stillstandingApiService.createPreMatch(matchConfigDTO);
        } else if (type == MatchType.MAIN) {
            apiResult = stillstandingApiService.createMainMatch(matchConfigDTO);
        } else {
            log.warn("cannot handle type = {}", type);
            return null;
        }
        
        String sessionId;
        try {
            MatchSituationDTO matchSituationDTO = apiResult.getPayload();
            sessionId = matchSituationDTO.getId();
        } catch (Exception e) {
            log.warn("createEndlessMatch error, apiResult = {}", apiResult);
            log.warn("createEndlessMatch error, e = ", e);
            return null;
        }
        
        apiResult = stillstandingApiService.start(sessionId);
        try {
            MatchSituationDTO matchSituationDTO = apiResult.getPayload();
            return matchSituationDTO;
        } catch (Exception e) {
            log.warn("createEndlessMatch error, apiResult = {}", apiResult);
            log.warn("createEndlessMatch error, e = ", e);
            return null;
        }
    }
    
    public MatchSituationDTO answer(String sessionId, String answerChar) {
        ApiResult<MatchSituationDTO> apiResult = stillstandingApiService.teamAnswer(sessionId, answerChar);
        try {
            MatchSituationDTO matchSituationDTO = apiResult.getPayload();
            return matchSituationDTO;
        } catch (Exception e) {
            log.warn("createEndlessMatch error: {}", apiResult);
            return null;
        }
    }
    
    public MatchSituationDTO useSkill(String sessionId, String skillName) {
        ApiResult<MatchSituationDTO> apiResult = stillstandingApiService.teamUseSkill(sessionId, skillName);
        try {
            MatchSituationDTO matchSituationDTO = apiResult.getPayload();
            return matchSituationDTO;
        } catch (Exception e) {
            log.warn("createEndlessMatch error: {}", apiResult);
            return null;
        }
    }
    
    public MatchSituationDTO nextQustion(String sessionId) {
        ApiResult<MatchSituationDTO> apiResult = stillstandingApiService.nextQustion(sessionId);
        try {
            MatchSituationDTO matchSituationDTO = apiResult.getPayload();
            return matchSituationDTO;
        } catch (Exception e) {
            log.warn("createEndlessMatch error: {}", apiResult);
            return null;
        }
    }
    
    
    
    


    @Override
    public InputStream download(String fileId) {
        try {
            final Response response = stillstandingApiService.pictures(fileId);
            final Response.Body body = response.body();
            final InputStream inputStream = body.asInputStream();
            return inputStream;
        } catch (Exception e) {
            log.info("download image faild {} {}", fileId, e);
            return null;
        }
    }

    @Override
    public String getCacheSubFolerName() {
        return "quiz";
    }

    public List<TeamConstInfoDTO> updateTeam(String sessionId, TeamConstInfoDTO teamConstInfoDTO) {
        ApiResult<List<TeamConstInfoDTO>> apiResult = stillstandingApiService.updateTeam(teamConstInfoDTO);
        try {
            List<TeamConstInfoDTO> payload = apiResult.getPayload();
            return payload;
        } catch (Exception e) {
            log.warn("updateTeam error: {}", apiResult);
            return null;
        }
    }
    
}
