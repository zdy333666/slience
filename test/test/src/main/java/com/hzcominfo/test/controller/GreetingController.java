/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.test.controller;

import com.hzcominfo.test.pojo.Greeting;
import com.hzcominfo.test.pojo.HelloMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author cominfo4
 */
@Controller
public class GreetingController {

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {

        Thread.sleep(3000);
        return new Greeting("Hello, " + message.getName() + "!");
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public Greeting greeting2(String message) throws Exception {

        Thread.sleep(3000);
        
        return new Greeting("Hello, " + message + "!");
        
        //template.convertAndSend("/topic/greetings", new Greeting("Hello, " + message + "!"));
    }

}
