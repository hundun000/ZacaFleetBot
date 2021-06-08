package com.hundun.mirai.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hundun.mirai.bot.character.PrinzEugen;
import com.hundun.mirai.bot.export.CustomBeanFactory;
import com.hundun.mirai.server.controller.BaseCharacterController;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@RestController
@RequestMapping("/api/prinzEugen")
public class PrinzEugenController extends BaseCharacterController {
    

    
    PrinzEugen prinzEugen;
    
    @Autowired
    public PrinzEugenController() {
        super(CustomBeanFactory.getInstance().prinzEugen);
       this.prinzEugen = CustomBeanFactory.getInstance().prinzEugen;
    }
    
}
