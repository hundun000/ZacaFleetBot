package com.mirai.hundun.bot.amiya;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mirai.hundun.bot.Amiya;
import com.mirai.hundun.bot.IPlainTextHandler;
import com.mirai.hundun.quiz.Question;
import com.mirai.hundun.quiz.QuizService;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.At;
import net.mamoe.mirai.message.data.PlainText;

/**
 * @author hundun
 * Created on 2021/04/25
 */
@Component
public class QuizHandler implements IPlainTextHandler {

    @Autowired
    QuizService quizService;
    
    @Autowired
    Amiya parent;
    
    Question currentQuestion;
    
    @Override
    public boolean acceptPlainText(GroupMessageEvent event, PlainText plainText) {
        String newMessage = plainText != null ? plainText.contentToString() : null;
        
        if (newMessage == null) {
            return false;
        }
        
        synchronized (this) {
            if (currentQuestion == null) {
                if (newMessage.equals("阿米娅出题")) {
                    currentQuestion = quizService.getQuestion();
                    StringBuilder builder = new StringBuilder();
                    builder.append(currentQuestion.getStem()).append("\n")
                    .append("A. ").append(currentQuestion.getOptions().get(0)).append("\n")
                    .append("B. ").append(currentQuestion.getOptions().get(1)).append("\n")
                    .append("C. ").append(currentQuestion.getOptions().get(2)).append("\n")
                    .append("D. ").append(currentQuestion.getOptions().get(3)).append("\n")
                    .append("\n")
                    .append("发送选项字母来回答");
                    event.getSubject().sendMessage(builder.toString());
                    
                    return true;
                }
            } else {
                if (newMessage.equals("阿米娅出题")) {
                    event.getSubject().sendMessage("上一个问题还没回答哦~");
                    return true;
                } else if (currentQuestion.getAnswerChar().equals(newMessage)) {
                    event.getSubject().sendMessage(
                            (new At(event.getSender().getId()))
                            .plus("回答正确\n正确答案是" + currentQuestion.getAnswerChar())
                            );
                    currentQuestion = null;
                    return true;
                } else if (newMessage.equals("A") || newMessage.equals("B") || newMessage.equals("C") || newMessage.equals("D")) {
                    event.getSubject().sendMessage(
                            (new At(event.getSender().getId()))
                            .plus("回答错误QAQ\n正确答案是" + currentQuestion.getAnswerChar())
                            );
                    currentQuestion = null;
                    return true;
                }
            }
        }
        return false;
    }

}
