package com.mirai.hundun.cp.quiz;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
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
import com.mirai.hundun.service.file.FileService;
import com.mirai.hundun.service.file.IFileProvider;

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
    StillstandingApiService stillstandingApiService;
    
    
    public QuizService() {
        mapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
        
        
    }
    
    public Integer createAndStartEndlessMatch(String questionPackageName) {
        String responseString = stillstandingApiService.createEndlessMatch(questionPackageName);
        Integer matchId;
        try {
            JsonNode responseJson = mapper.readTree(responseString);
            matchId = responseJson.at("/payload").asInt();
        } catch (Exception e) {
            log.warn("createEndlessMatch error: {}", responseString);
            return null;
        }
        
        responseString = stillstandingApiService.start(matchId);
        return matchId;
    }
    
    
    public Question getQuestion(int matchId) {
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
            String resourceType = payload.at("/question/resource/type").asText();
            File file = null;
            if (resourceType.equals("IMAGE")) {
                String imageResourceId = payload.at("/question/resource/data").asText();
                file = fileService.downloadOrFromCache(imageResourceId, this);
            }
            Question question = new Question(stem, options, answerChar, file);
            return question;
        } catch (Exception e) {
            log.warn("createEndlessMatch error: {}", responseString);
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
