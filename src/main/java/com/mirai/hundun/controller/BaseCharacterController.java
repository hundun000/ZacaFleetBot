package com.mirai.hundun.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.mirai.hundun.character.BaseCharacter;
import com.mirai.hundun.core.EventInfo;
import com.mirai.hundun.core.EventInfoFactory;
import com.mirai.hundun.parser.statement.Statement;

/**
 * @author hundun
 * Created on 2021/05/15
 */
public class BaseCharacterController {
    BaseCharacter character;
    
    public BaseCharacterController(BaseCharacter character) {
        this.character = character;
    }
    
    @RequestMapping(value="/testParse", method=RequestMethod.GET)
    public Statement testParse(@RequestParam("msg") String msg) {
        return character.testParse(msg);
    }
    
    @RequestMapping(value="/testOnGroupMessageEvent", method=RequestMethod.POST)
    public boolean testOnGroupMessageEventMessage(
            @RequestParam("groupId") long groupId,
            @RequestParam("senderId") long senderId,
            @RequestParam("targetId") long targetId,
            @RequestParam("message") String message
            ) throws Exception {
        
        EventInfo eventInfo = EventInfoFactory.get(groupId, senderId, targetId, message);
        
        return character.testOnGroupMessageEventMessage(eventInfo);
    }
}