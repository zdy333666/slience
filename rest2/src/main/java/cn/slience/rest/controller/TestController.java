/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.rest.controller;

import cn.slience.rest.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Len
 */
@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping(value = "echo", method = {RequestMethod.GET})
    public String echo() {

        return "rest2-->" + testService.echo();
    }

}
