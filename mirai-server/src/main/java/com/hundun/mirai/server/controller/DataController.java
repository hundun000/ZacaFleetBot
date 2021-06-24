package com.hundun.mirai.server.controller;

import java.io.File;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hundun.mirai.bot.core.CustomBeanFactory;
import com.hundun.mirai.bot.cp.kcwiki.KancolleWikiService;
import com.hundun.mirai.bot.cp.penguin.PenguinService;
import com.hundun.mirai.bot.cp.penguin.domain.report.MatrixReport;
import com.hundun.mirai.bot.cp.penguin.domain.report.StageInfoReport;
import com.hundun.mirai.bot.cp.weibo.WeiboService;
import com.hundun.mirai.bot.cp.weibo.WeiboService.WeiboCardCacheAndImage;
import com.hundun.mirai.bot.cp.weibo.db.WeiboCardCacheRepository;
import com.hundun.mirai.bot.cp.weibo.domain.WeiboCardCache;
import com.hundun.mirai.bot.export.IConsole;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@RestController
@RequestMapping("/api/data")
public class DataController {
    
    PenguinService penguinService = CustomBeanFactory.getInstance().penguinService;
    
    KancolleWikiService kancolleWikiService = CustomBeanFactory.getInstance().kancolleWikiService;
    
    WeiboService weiboService = CustomBeanFactory.getInstance().weiboService;
    
    WeiboCardCacheRepository cardCacheRepository = CustomBeanFactory.getInstance().weiboCardCacheRepository;

    private IConsole console = CustomBeanFactory.getInstance().console;;
    
    @RequestMapping(value="/weibo/updateAndGetTopBlog", method=RequestMethod.GET)
    public List<WeiboCardCacheAndImage> updateAndGetTopBlog(
            @RequestParam("uid") String uid) {
        File cacheFolder = console.resolveDataFileOfFileCache();
        return weiboService.updateAndGetTopBlog(uid, cacheFolder);
    }
    
    @RequestMapping(value="/weibo/findTop5ByUidOrderByMblogCreatedDateTimeDesc", method=RequestMethod.GET)
    public List<WeiboCardCache> findTop5ByUidOrderByMblogCreatedDateTimeDesc(
            @RequestParam("uid") String uid) {
        return cardCacheRepository.findTop5ByUidOrderByMblogCreatedDateTimeDesc(uid);
    }
    
    @RequestMapping(value="/penguin/resetCache", method=RequestMethod.GET)
    public String updateItems() {
        penguinService.resetCache();
        return "OK";
    }
    
    @RequestMapping(value="/kcw/getShipDetail", method=RequestMethod.GET)
    public String getShipDetail(
            @RequestParam("fuzzyName") String fuzzyName) {
        return kancolleWikiService.getShipDetail(fuzzyName).toString();
    }

    
    @RequestMapping(value="/penguin/getTopResultNode", method=RequestMethod.GET)
    public MatrixReport getTopResultNode(
            @RequestParam("fuzzyName") String fuzzyName,
            @RequestParam("topSize") int topSize
            ) {
        return penguinService.getTopResultNode(fuzzyName, topSize);
    }
    
    @RequestMapping(value="/penguin/getStageInfoReport", method=RequestMethod.GET)
    public StageInfoReport getStageInfoReport(
            @RequestParam("stageCode") String stageCode
            ) {
        return penguinService.getStageInfoReport(stageCode);
    }
}
