package com.mirai.hundun.character.function;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.character.Amiya;
import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.cp.penguin.domain.report.MatrixReport;
import com.mirai.hundun.cp.penguin.domain.report.MatrixReportNode;
import com.mirai.hundun.cp.quiz.QuizService;
import com.mirai.hundun.parser.statement.FunctionCallStatement;
import com.mirai.hundun.parser.statement.LiteralValueStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;
import com.mirai.hundun.service.file.FileService;
import com.zaca.stillstanding.domain.dto.AnswerType;
import com.zaca.stillstanding.domain.dto.EventType;
import com.zaca.stillstanding.domain.dto.MatchEvent;
import com.zaca.stillstanding.domain.dto.MatchSituationDTO;
import com.zaca.stillstanding.domain.dto.MatchState;
import com.zaca.stillstanding.domain.dto.QuestionDTO;
import com.zaca.stillstanding.domain.dto.ResourceType;
import com.zaca.stillstanding.domain.dto.TeamRuntimeInfoDTO;
import com.zaca.stillstanding.domain.dto.event.AnswerResultEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.utils.ExternalResource;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
@Component
public class QuizHandler implements IFunction {

    public String functionNameNextQuest = "出题";
    public String functionNameStartMatch = "开始无尽模式比赛";
    
    @Autowired
    QuizService quizService;
    
    @Autowired
    FileService fileService;
    
    @Autowired
    BotService botService;
    
    Map<String, SessionData> sessionDataMap = new HashMap<>();
    
    private static final long QUESTION_TIME_OUT = 3 * 60 * 1000;
    
    @Getter
    private class SessionData {
        
        MatchSituationDTO matchSituationDTO;
        File resource;
        long createTime;
    }
    
    

    @Override
    public boolean acceptStatement(String sessionId, EventInfo event, Statement statement) {
        
        synchronized (this) {
            
            SessionData sessionData = sessionDataMap.get(sessionId);
            if (sessionData == null) {
                sessionData = new SessionData();
                sessionDataMap.put(sessionId, sessionData);
            }
            long now = System.currentTimeMillis();
            
            if (statement instanceof FunctionCallStatement) {
                FunctionCallStatement functionCallStatement = (FunctionCallStatement)statement;
                if (functionCallStatement.getFunctionName().equals(functionNameNextQuest)) {
                    if (sessionData.matchSituationDTO == null) {
                        botService.sendToGroup(event.getGroupId(), "没有进行中的比赛");
                        return true;
                    } else if (sessionData.matchSituationDTO.getState() == MatchState.WAIT_GENERATE_QUESTION) {
                            sessionData.matchSituationDTO = quizService.nextQustion(sessionData.matchSituationDTO.getId());
                            QuestionDTO questionDTO = sessionData.matchSituationDTO.getQuestion();
                            if (questionDTO.getResource().getType() == ResourceType.IMAGE) {
                                String imageResourceId = questionDTO.getResource().getData();
                                sessionData.resource = fileService.downloadOrFromCache(imageResourceId, quizService);
                            } else {
                                sessionData.resource = null;
                            }
                            sessionData.createTime = now;
                            StringBuilder builder = new StringBuilder();
                            builder.append(questionDTO.getStem()).append("\n")
                            .append("A. ").append(questionDTO.getOptions().get(0)).append("\n")
                            .append("B. ").append(questionDTO.getOptions().get(1)).append("\n")
                            .append("C. ").append(questionDTO.getOptions().get(2)).append("\n")
                            .append("D. ").append(questionDTO.getOptions().get(3)).append("\n")
                            .append("\n")
                            .append("发送选项字母来回答");
                            MessageChain messageChain = new PlainText(builder.toString()).plus(new PlainText(""));
                            if (sessionData.resource != null) {
                                ExternalResource externalResource = ExternalResource.create(sessionData.resource);
                                Image image = botService.uploadImage(event.getGroupId(), externalResource);
                                messageChain = messageChain.plus(image);
                            }
                            botService.sendToGroup(event.getGroupId(), messageChain);
                            return true;
                    } else if (sessionData.matchSituationDTO.getState() == MatchState.WAIT_ANSWER) {
                            botService.sendToGroup(event.getGroupId(), "上一个问题还没回答哦~");
                            return true;
                    }
                } else if (functionCallStatement.getFunctionName().equals(functionNameStartMatch)) {
                    if (sessionData.matchSituationDTO == null) {
                        String questionPackageName = functionCallStatement.getArgs().get(0);
                        String teamName = functionCallStatement.getArgs().get(1);
                        sessionData.matchSituationDTO = quizService.createAndStartEndlessMatch(questionPackageName, teamName);
                        
                        if (sessionData.matchSituationDTO != null) {
                            botService.sendToGroup(event.getGroupId(), "开始比赛成功");
                            return true;
                        } else {
                            botService.sendToGroup(event.getGroupId(), "开始比赛失败");
                            return true;
                        }
                    } else {
                        botService.sendToGroup(event.getGroupId(), "目前已在比赛中");
                        return true;
                    }
                    
                }
            } else if (statement instanceof LiteralValueStatement) {
                
                if (sessionData.matchSituationDTO != null && sessionData.matchSituationDTO.getState() == MatchState.WAIT_ANSWER) {
                    String newMessage = ((LiteralValueStatement)statement).getValue();
                    if (newMessage.equals("A") || newMessage.equals("B") || newMessage.equals("C") || newMessage.equals("D")) {
                        String correctAnser = QuestionDTO.intToAnswerText(sessionData.matchSituationDTO.getQuestion().getAnswer());
                        sessionData.matchSituationDTO = quizService.answer(sessionData.matchSituationDTO.getId(), newMessage);
                        AnswerResultEvent answerResultEvent = sessionData.matchSituationDTO.getAnswerResultEvent();
                        if (answerResultEvent != null) {
                            
                            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
                            messageChainBuilder.add(new At(event.getSenderId()));
                            if (answerResultEvent.getResult() == AnswerType.CORRECT) {
                                messageChainBuilder.add(new PlainText("回答正确\n正确答案是" + correctAnser));
                            } else if (answerResultEvent.getResult() == AnswerType.WRONG) {
                                messageChainBuilder.add(new PlainText("回答错误QAQ\n正确答案是" + correctAnser));
                            } else {
                                messageChainBuilder.add(new PlainText("本题已跳过\n正确答案是" + correctAnser));
                            }
                            
                            StringBuilder teamInfoBuilder = new StringBuilder();
                            teamInfoBuilder.append("\n\n队伍状态:\n");
                            for (TeamRuntimeInfoDTO dto : sessionData.matchSituationDTO.getTeamRuntimeInfos()) {
                                teamInfoBuilder.append(dto.getName()).append(" ");
                                teamInfoBuilder.append(dto.getMatchScore()).append("分 ");
                                teamInfoBuilder.append("英雄:").append(dto.getRoleName()).append("\n");
                                for (Entry<String, Integer> entry : dto.getSkillRemainTimes().entrySet()) {
                                    teamInfoBuilder.append(entry.getKey()).append(":").append(entry.getValue()).append(" ");
                                }
                            }
                            messageChainBuilder.add(new PlainText(teamInfoBuilder.toString()));
                            
                            botService.sendToGroup(event.getGroupId(), messageChainBuilder.build());
                            return true;
                        }
                        
                    }
                    
                    
                }
            }
        }
        return false;
    }

}
