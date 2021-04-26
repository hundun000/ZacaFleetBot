package com.mirai.hundun.cp.quiz;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mirai.hundun.cp.quiz.feign.StillstandingApiService;
import com.mirai.hundun.cp.weibo.WeiboService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
@Service
public class QuizService {
    ObjectMapper mapper = new ObjectMapper();
    
    
    @Autowired
    StillstandingApiService stillstandingApiService;
    
    Integer matchId;
    
    public QuizService() {
        mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
    }
    
    public void createAndStartEndlessMatch() {
        String responseString = stillstandingApiService.createEndlessMatch();
        try {
            JsonNode responseJson = mapper.readTree(responseString);
            matchId = responseJson.at("/payload").asInt();
        } catch (Exception e) {
            log.warn("createEndlessMatch error: {}", responseString);
            return;
        }
        
        responseString = stillstandingApiService.start(matchId);
    }
    
    
    public Question getQuestion() {
        String responseString = stillstandingApiService.answer(matchId, "A");
        try {
            JsonNode responseJson = mapper.readTree(responseString);
            String payloadString = responseJson.at("/payload").asText();
            JsonNode payload = mapper.readTree(payloadString);
            String stem = payload.at("/question/stem").asText();
            JsonNode optionsNode = payload.at("/question/options");
            List<String> options = new ArrayList<>();
            for (final JsonNode option : optionsNode) {
                options.add(option.asText());
            }
            String answerChar = payload.at("/question/answerChar").asText();
            Question question = new Question(stem, options, answerChar);
            return question;
        } catch (Exception e) {
            log.warn("createEndlessMatch error: {}", responseString);
            return null;
        }
    }
    
    
}
