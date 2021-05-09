package com.mirai.hundun.cp.quiz;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mirai.hundun.cp.weibo.WeiboService;
import com.mirai.hundun.service.file.FileService;
import com.mirai.hundun.service.file.IFileProvider;
import com.zaca.stillstanding.api.StillstandingApiFeignClient;
import com.zaca.stillstanding.domain.dto.ApiResult;
import com.zaca.stillstanding.domain.dto.MatchConfigDTO;
import com.zaca.stillstanding.domain.dto.MatchSituationDTO;
import com.zaca.stillstanding.domain.dto.QuestionDTO;

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
    
    public MatchSituationDTO createAndStartEndlessMatch(String questionPackageName, String teamName) {
        MatchConfigDTO matchConfigDTO = new MatchConfigDTO();
        matchConfigDTO.setTeamNames(Arrays.asList(teamName));
        matchConfigDTO.setQuestionPackageName(questionPackageName);
        ApiResult apiResult = stillstandingApiService.createEndlessMatch(matchConfigDTO);
        String sessionId;
        try {
            MatchSituationDTO matchSituationDTO = mapper.readValue(apiResult.getPayload(), MatchSituationDTO.class);
            sessionId = matchSituationDTO.getId();
        } catch (Exception e) {
            log.warn("createEndlessMatch error, apiResult = {}", apiResult);
            log.warn("createEndlessMatch error, e = ", e);
            return null;
        }
        
        apiResult = stillstandingApiService.start(sessionId);
        try {
            MatchSituationDTO matchSituationDTO = mapper.readValue(apiResult.getPayload(), MatchSituationDTO.class);
            return matchSituationDTO;
        } catch (Exception e) {
            log.warn("createEndlessMatch error, apiResult = {}", apiResult);
            log.warn("createEndlessMatch error, e = ", e);
            return null;
        }
    }
    
    public MatchSituationDTO answer(String sessionId, String answerChar) {
        ApiResult apiResult = stillstandingApiService.teamAnswer(sessionId, answerChar);
        try {
            MatchSituationDTO matchSituationDTO = mapper.readValue(apiResult.getPayload(), MatchSituationDTO.class);
            return matchSituationDTO;
        } catch (Exception e) {
            log.warn("createEndlessMatch error: {}", apiResult);
            return null;
        }
    }
    
    public MatchSituationDTO useSkill(String sessionId, String skillName) {
        ApiResult apiResult = stillstandingApiService.teamUseSkill(sessionId, skillName);
        try {
            MatchSituationDTO matchSituationDTO = mapper.readValue(apiResult.getPayload(), MatchSituationDTO.class);
            return matchSituationDTO;
        } catch (Exception e) {
            log.warn("createEndlessMatch error: {}", apiResult);
            return null;
        }
    }
    
    public MatchSituationDTO nextQustion(String sessionId) {
        ApiResult apiResult = stillstandingApiService.nextQustion(sessionId);
        try {
            MatchSituationDTO matchSituationDTO = mapper.readValue(apiResult.getPayload(), MatchSituationDTO.class);
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
    
}
