/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.springboot.demo.controller;

import cn.slience.springboot.demo.pojo.Demo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author breeze
 */
@Controller
@RequestMapping(value = "rest")
public class RestTemplateController {

    private static final Logger logger = LoggerFactory.getLogger(RestTemplateController.class);

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "test", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Demo test() {
        return restTemplate.getForObject("/greeting", Demo.class);
    }

    @RequestMapping(value = "compress", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String compress() {

        String str = new RestTemplate().getForObject("https://docs.spring.io/spring-boot/docs/2.0.0.RELEASE/reference/htmlsingle/#boot-features-cors", String.class);

        return str;
    }

}
