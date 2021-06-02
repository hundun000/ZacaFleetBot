package com.hundun.mirai.server.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hundun.mirai.bot.CustomBeanFactory;
import com.hundun.mirai.bot.cp.quiz.QuizService;
import com.hundun.mirai.bot.cp.weibo.WeiboService;
import com.hundun.mirai.server.SpringConsole;
import com.hundun.mirai.server.configuration.SpringConsoleLoader;

/**
 * @author hundun
 * Created on 2021/04/21
 */
@RestController
@RequestMapping("/api")
public class BotController {
    
    @Autowired
    SpringConsoleLoader springConsoleLoader;
    
    
    WeiboService weiboService = CustomBeanFactory.getInstance().weiboService;
    
    QuizService quizService = CustomBeanFactory.getInstance().quizService;
    
    @PostConstruct
    public void postConstruct() {
        
    }
    
//    @RequestMapping(value="/getFirstBlog", method=RequestMethod.GET)
//    public String getFirstBlog() {
//        return weiboService.getFirstBlogInfo(weiboService.yjUid);
//    }
//    
//    @RequestMapping(value="/updateContainerid", method=RequestMethod.GET)
//    public String updateContainerid() {
//        weiboService.updateUserInfoCache(weiboService.yjUid);
//        return "OK";
//    }
//    
//    @RequestMapping(value="/updateBlog", method=RequestMethod.GET)
//    public String updateBlog() {
//        weiboService.updateBlog(weiboService.yjUid);
//        return "OK";
//    }
    
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login() {
        springConsoleLoader.springConsole.login();
        return "OK";
    }
    
//    @RequestMapping(value="/getQuestion", method=RequestMethod.GET)
//    public Question getQuestion() {
//        Question question = quizService.getQuestion();
//        return question;
//    }
//    
//    @RequestMapping(value="/createAndStartEndlessMatch", method=RequestMethod.GET)
//    public String createAndStartEndlessMatch() {
//        return quizService.createAndStartEndlessMatch();
//        return "OK";
//    }
}
