package com.mirai.hundun.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mirai.hundun.character.Amiya;
import com.mirai.hundun.character.BaseCharacter;
import com.mirai.hundun.character.PrinzEugen;
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
@RequestMapping("/api/prinzEugen")
public class PrinzEugenController extends BaseCharacterController {
    

    
    PrinzEugen prinzEugen;
    
    @Autowired
    public PrinzEugenController(PrinzEugen prinzEugen) {
        super(prinzEugen);
       this.prinzEugen = prinzEugen;
    }
    
}
