/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.springboot.demo.controller;

import cn.slience.springboot.demo.pojo.Demo;
import cn.slience.springboot.demo.pojo.SimpleModel;
import cn.slience.springboot.demo.service.SimpleService;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author breeze
 */
@Controller
public class SimpleController {

    private static final Logger logger = LoggerFactory.getLogger(SimpleController.class);

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private SimpleService simpleService;

    @RequestMapping(value = "/demo", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Demo demo(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Demo(counter.incrementAndGet(), String.format(template, name));
    }

    @RequestMapping(value = "/cache", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public SimpleModel testCache(@RequestParam(value = "name", defaultValue = "slience") String name) {
        try {

            return simpleService.testCache(name);

        } catch (InterruptedException ex) {
            logger.error("", ex);
            return null;
        }
    }

}
