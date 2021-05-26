package com.hundun.mirai.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hundun.mirai.server.controller.BaseCharacterController;
import com.hundun.mirai.plugin.CustomBeanFactory;
import com.hundun.mirai.plugin.character.ZacaMusume;

/**
 * @author hundun
 * Created on 2021/04/26
 */
@RestController
@RequestMapping("/api/zacaMusume")
public class ZacaMusumeController extends BaseCharacterController {
    
    ZacaMusume zacaMusume;
    
    @Autowired
    public ZacaMusumeController() {
        super(CustomBeanFactory.getInstance().zacaMusume);
       this.zacaMusume = CustomBeanFactory.getInstance().zacaMusume;
    }
    
}
