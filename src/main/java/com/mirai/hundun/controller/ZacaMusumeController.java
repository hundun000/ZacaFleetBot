package com.mirai.hundun.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mirai.hundun.character.Amiya;
import com.mirai.hundun.character.ZacaMusume;
import com.mirai.hundun.cp.penguin.PenguinService;
import com.mirai.hundun.cp.penguin.domain.MatrixReport;
import com.mirai.hundun.cp.penguin.domain.MatrixReportNode;
import com.mirai.hundun.cp.penguin.domain.ResultMatrixNode;
import com.mirai.hundun.parser.statement.Statement;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@RestController
@RequestMapping("/api/zaca-musume")
public class ZacaMusumeController {
    @Autowired
    ZacaMusume zacaMusume;
    
    @RequestMapping(value="/testParse", method=RequestMethod.GET)
    public Statement testParse(@RequestParam("msg") String msg) {
        return zacaMusume.testParse(msg);
    }
    
    
}
