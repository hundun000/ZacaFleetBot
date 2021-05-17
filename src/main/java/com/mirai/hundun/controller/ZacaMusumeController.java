package com.mirai.hundun.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mirai.hundun.character.ZacaMusume;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@RestController
@RequestMapping("/api/zacaMusume")
public class ZacaMusumeController extends BaseCharacterController {
    
    ZacaMusume zacaMusume;
    
    @Autowired
    public ZacaMusumeController(ZacaMusume zacaMusume) {
        super(zacaMusume);
       this.zacaMusume = zacaMusume;
    }
    
}
