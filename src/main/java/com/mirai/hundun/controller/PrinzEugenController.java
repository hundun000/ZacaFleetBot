package com.mirai.hundun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mirai.hundun.character.PrinzEugen;

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
