package com.mirai.hundun.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mirai.hundun.cp.penguin.PenguinService;
import com.mirai.hundun.cp.penguin.domain.ResultMatrixNode;
import com.mirai.hundun.cp.penguin.domain.report.MatrixReport;
import com.mirai.hundun.cp.penguin.domain.report.MatrixReportNode;
import com.mirai.hundun.cp.penguin.domain.report.StageInfoReport;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@RestController
@RequestMapping("/api/data")
public class DataController {
    @Autowired
    PenguinService penguinService;
    
    @RequestMapping(value="/penguin/resetCache", method=RequestMethod.GET)
    public String updateItems() {
        penguinService.resetCache();
        return "OK";
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
