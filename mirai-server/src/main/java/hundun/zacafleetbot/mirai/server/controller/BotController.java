package hundun.zacafleetbot.mirai.server.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hundun.zacafleetbot.mirai.botlogic.cp.weibo.WeiboService;
import hundun.zacafleetbot.mirai.server.SpringConsole;
import hundun.zacafleetbot.mirai.server.configuration.SpringConsoleLoader;

/**
 * @author hundun
 * Created on 2021/04/21
 */
@RestController
@RequestMapping("/api")
public class BotController {
    
    @Autowired
    SpringConsoleLoader springConsoleLoader;
    

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
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String login(
            @RequestParam("botAccount") long botAccount
            ) {
        springConsoleLoader.console.login(botAccount);
        return "OK";
    }
    
    @RequestMapping(value="/logout", method=RequestMethod.POST)
    public String logout(
            @RequestParam("botAccount") long botAccount
            ) {
        springConsoleLoader.console.logout(botAccount);
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
