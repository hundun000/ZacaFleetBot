package com.mirai.hundun.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mirai.hundun.character.Amiya;
import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.EventInfoFactory;
import com.mirai.hundun.cp.penguin.PenguinService;
import com.mirai.hundun.cp.penguin.domain.ResultMatrixNode;
import com.mirai.hundun.cp.penguin.domain.report.MatrixReport;
import com.mirai.hundun.cp.penguin.domain.report.MatrixReportNode;
import com.mirai.hundun.parser.statement.Statement;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@RestController
@RequestMapping("/api/amiya")
public class AmiyaController {
    @Autowired
    Amiya amiya;
    
    @RequestMapping(value="/testParse", method=RequestMethod.GET)
    public Statement testParse(@RequestParam("msg") String msg) {
        return amiya.testParse(msg);
    }
    
    @RequestMapping(value="/testOnGroupMessageEvent", method=RequestMethod.POST)
    public boolean testOnGroupMessageEventMessage(
            @RequestParam("groupId") long groupId,
            @RequestParam("senderId") long senderId,
            @RequestParam("targetId") long targetId,
            @RequestParam("message") String message
            ) throws Exception {
        
        EventInfo eventInfo = EventInfoFactory.get(groupId, senderId, targetId, message);
        
        return amiya.testOnGroupMessageEventMessage(eventInfo);
    }
    
}
