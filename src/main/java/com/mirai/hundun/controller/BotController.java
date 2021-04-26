package com.mirai.hundun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mirai.hundun.cp.quiz.Question;
import com.mirai.hundun.cp.quiz.QuizService;
import com.mirai.hundun.cp.weibo.WeiboService;
import com.mirai.hundun.service.BotService;

/**
 * @author hundun
 * Created on 2021/04/21
 */
@RestController
@RequestMapping("/api")
public class BotController {
    
    @Autowired
    BotService botService;
    
    @Autowired
    WeiboService weiboService;
    
    @Autowired
    QuizService quizService;
    
    @RequestMapping(value="/getFirstBlog", method=RequestMethod.GET)
    public String getFirstBlog() {
        return weiboService.getFirstBlogInfo(weiboService.yjUid);
    }
    
    @RequestMapping(value="/updateContainerid", method=RequestMethod.GET)
    public String updateContainerid() {
        weiboService.updateContainerid(weiboService.yjUid);
        return "OK";
    }
    
    @RequestMapping(value="/updateBlog", method=RequestMethod.GET)
    public String updateBlog() {
        weiboService.updateBlog(weiboService.yjUid);
        return "OK";
    }
    
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login() {
        botService.login();
        return "OK";
    }
    
    @RequestMapping(value="/getQuestion", method=RequestMethod.GET)
    public Question getQuestion() {
        Question question = quizService.getQuestion();
        return question;
    }
    
    @RequestMapping(value="/createAndStartEndlessMatch", method=RequestMethod.GET)
    public String createAndStartEndlessMatch() {
        quizService.createAndStartEndlessMatch();
        return "OK";
    }
}
