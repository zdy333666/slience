/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.rest.controller;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Len
 */
@RestController
public class TestEchoController {

    @RequestMapping(value = "echo", method = {RequestMethod.GET})
    public String echo() throws InterruptedException {

        TimeUnit.MILLISECONDS.sleep(1000 + new Random().nextInt(1000));

        return "welcome to rest1";
    }

}
