/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.test.controller;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zdy
 */
@Controller
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(TestController.class);

    /**
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public Object handGet(@RequestParam(required = false, defaultValue = "Tom") String name) {

        Map<String, Object> result = new HashMap<>();
        result.put("name", name);

        return result;
    }

    /**
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "test", method = RequestMethod.POST)
    @ResponseBody
    public Object handPost(@RequestBody String name) {

        Map<String, Object> result = new HashMap<>();
        result.put("name", name);

        return result;
    }

}
