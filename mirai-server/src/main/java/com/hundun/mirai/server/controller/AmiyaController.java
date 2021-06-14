package com.hundun.mirai.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hundun.mirai.bot.core.CustomBeanFactory;
import com.hundun.mirai.bot.core.character.Amiya;


/**
 * @author hundun
 * Created on 2021/04/26
 */
@RestController
@RequestMapping("/api/amiya")
public class AmiyaController extends BaseCharacterController {
    

    
    Amiya amiya = CustomBeanFactory.getInstance().amiya;
    

    
    @Autowired
    public AmiyaController() {
        super(CustomBeanFactory.getInstance().amiya);
       this.amiya = CustomBeanFactory.getInstance().amiya;
    }
    
}
