/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.hikaricp.controller;

import cn.slience.hikaricp.dao.TestDAO;
import cn.slience.hikaricp.service.TestService;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author breeze
 */
@Controller
public class TestController {
    
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    
    @Autowired 
    DataSource  dataSource;

    @Autowired
    private TestService testService;

    @RequestMapping(value = "test/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String test(@PathVariable(name = "id") int id) {

        return testService.getGroupCategoryNameById(id);
    }

}
