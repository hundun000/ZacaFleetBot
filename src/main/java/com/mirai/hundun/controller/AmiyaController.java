package com.mirai.hundun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mirai.hundun.character.Amiya;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@RestController
@RequestMapping("/api/amiya")
public class AmiyaController extends BaseCharacterController {
    

    
    Amiya amiya;
    
    @Autowired
    public AmiyaController(Amiya amiya) {
        super(amiya);
       this.amiya = amiya;
    }
    
}
