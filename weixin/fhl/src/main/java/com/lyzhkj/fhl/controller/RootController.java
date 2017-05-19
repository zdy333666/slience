/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author breeze
 */
@Controller
public class RootController {
    
    @RequestMapping("/")
    @ResponseBody
    public String getPage() {

        String result ="test is ok";
        System.out.println(result);
        
        return result;
    }
    
}
