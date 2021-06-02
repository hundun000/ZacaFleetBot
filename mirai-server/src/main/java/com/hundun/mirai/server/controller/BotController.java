package com.hundun.mirai.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hundun.mirai.bot.CustomBeanFactory;
import com.hundun.mirai.bot.cp.quiz.QuizService;
import com.hundun.mirai.bot.cp.weibo.WeiboService;
import com.hundun.mirai.bot.service.BotService;

/**
 * @author hundun
 * Created on 2021/04/21
 */
@RestController
@RequestMapping("/api")
public class BotController {
    
    BotService botService = CustomBeanFactory.getInstance().botService;
    
    WeiboService weiboService = CustomBeanFactory.getInstance().weiboService;
    
    QuizService quizService = CustomBeanFactory.getInstance().quizService;
    
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
        botService.login();
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
