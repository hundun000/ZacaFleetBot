package com.mirai.hundun.character.function;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.character.Amiya;
import com.mirai.hundun.cp.penguin.domain.MatrixReport;
import com.mirai.hundun.cp.penguin.domain.MatrixReportNode;
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
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Slf4j
@Component
public class QuizHandler implements IFunction {

    public String functionName = "出题";
    
    @Autowired
    QuizService quizService;
    
    
    @Autowired
    BotService botService;
    
    Map<Long, SessionData> groupIdToData = new HashMap<>();
    
    private static final long QUESTION_TIME_OUT = 3 * 60 * 1000;
    
    @Getter
    private class SessionData {
        Question question;
        long createTime;
    }
    
    

    @Override
    public boolean acceptStatement(GroupMessageEvent event, Statement statement) {
        
        synchronized (this) {
            long groupId = event.getGroup().getId();
            SessionData sessionData = groupIdToData.get(groupId);
            if (sessionData == null) {
                sessionData = new SessionData();
                groupIdToData.put(groupId, sessionData);
            }
            long now = System.currentTimeMillis();
            
            if (statement instanceof FunctionCallStatement) {
                FunctionCallStatement functionCallStatement = (FunctionCallStatement)statement;
                if (functionCallStatement.getFunctionName().equals(functionName)) {
                    if (sessionData.question == null) {
                            sessionData.question = quizService.getQuestion();
                            sessionData.createTime = now;
                            StringBuilder builder = new StringBuilder();
                            builder.append(sessionData.question.getStem()).append("\n")
                            .append("A. ").append(sessionData.question.getOptions().get(0)).append("\n")
                            .append("B. ").append(sessionData.question.getOptions().get(1)).append("\n")
                            .append("C. ").append(sessionData.question.getOptions().get(2)).append("\n")
                            .append("D. ").append(sessionData.question.getOptions().get(3)).append("\n")
                            .append("\n")
                            .append("发送选项字母来回答");
                            botService.sendToEventSubject(event, builder.toString());
                            return true;
                    } else {
                            botService.sendToEventSubject(event, "上一个问题还没回答哦~");
                            return true;
                    }
                }
            } else if (statement instanceof LiteralValueStatement) {
                

                if (sessionData.question != null) {
                    String newMessage = ((LiteralValueStatement)statement).getValue();
                    if (sessionData.question.getAnswerChar().equals(newMessage)) {
                        event.getSubject().sendMessage(
                                (new At(event.getSender().getId()))
                                .plus("回答正确\n正确答案是" + sessionData.question.getAnswerChar())
                                );
                        sessionData.question = null;
                        return true;
                    } else if (newMessage.equals("A") || newMessage.equals("B") || newMessage.equals("C") || newMessage.equals("D")) {
                        botService.sendToEventSubject(event, (
                                (new At(event.getSender().getId()))
                                .plus("回答错误QAQ\n正确答案是" + sessionData.question.getAnswerChar())
                                ));
                        sessionData.question = null;
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
