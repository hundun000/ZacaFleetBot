package com.mirai.hundun.character.function;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.character.Amiya;
import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.cp.penguin.domain.report.MatrixReport;
import com.mirai.hundun.cp.penguin.domain.report.MatrixReportNode;
import com.mirai.hundun.cp.quiz.Question;
import com.mirai.hundun.cp.quiz.QuizService;
import com.mirai.hundun.parser.statement.FunctionCallStatement;
import com.mirai.hundun.parser.statement.LiteralValueStatement;
import com.mirai.hundun.parser.statement.Statement;
import com.mirai.hundun.service.BotService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
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
    BotService botService;
    
    Map<String, SessionData> sessionDataMap = new HashMap<>();
    
    private static final long QUESTION_TIME_OUT = 3 * 60 * 1000;
    
    @Getter
    private class SessionData {
        Integer matchId;
        Question question;
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
                    if (sessionData.matchId == null) {
                        botService.sendToGroup(event.getGroupId(), "没有进行中的比赛");
                        return true;
                    } else if (sessionData.question == null) {
                            sessionData.question = quizService.getQuestion(sessionData.matchId);
                            sessionData.createTime = now;
                            StringBuilder builder = new StringBuilder();
                            builder.append(sessionData.question.getStem()).append("\n")
                            .append("A. ").append(sessionData.question.getOptions().get(0)).append("\n")
                            .append("B. ").append(sessionData.question.getOptions().get(1)).append("\n")
                            .append("C. ").append(sessionData.question.getOptions().get(2)).append("\n")
                            .append("D. ").append(sessionData.question.getOptions().get(3)).append("\n")
                            .append("\n")
                            .append("发送选项字母来回答");
                            MessageChain messageChain = new PlainText(builder.toString()).plus(new PlainText(""));
                            if (sessionData.question.getResourceImage() != null) {
                                ExternalResource externalResource = ExternalResource.create(sessionData.question.getResourceImage());
                                Image image = botService.uploadImage(event.getGroupId(), externalResource);
                                messageChain = messageChain.plus(image);
                            }
                            botService.sendToGroup(event.getGroupId(), messageChain);
                            return true;
                    } else {
                            botService.sendToGroup(event.getGroupId(), "上一个问题还没回答哦~");
                            return true;
                    }
                } else if (functionCallStatement.getFunctionName().equals(functionNameStartMatch)) {
                    if (sessionData.matchId == null) {
                        String questionPackageName = functionCallStatement.getArgs().get(0);
                        Integer newMatchId = quizService.createAndStartEndlessMatch(questionPackageName);
                        if (newMatchId != null) {
                            sessionData.matchId = newMatchId;
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
                

                if (sessionData.question != null) {
                    String newMessage = ((LiteralValueStatement)statement).getValue();
                    if (sessionData.question.getAnswerChar().equals(newMessage)) {
                        botService.sendToGroup(event.getGroupId(),
                                (new At(event.getSenderId()))
                                .plus("回答正确\n正确答案是" + sessionData.question.getAnswerChar())
                                );
                        sessionData.question = null;
                        return true;
                    } else if (newMessage.equals("A") || newMessage.equals("B") || newMessage.equals("C") || newMessage.equals("D")) {
                        botService.sendToGroup(event.getGroupId(),
                                (new At(event.getSenderId()))
                                .plus("回答错误QAQ\n正确答案是" + sessionData.question.getAnswerChar())
                                );
                        sessionData.question = null;
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
