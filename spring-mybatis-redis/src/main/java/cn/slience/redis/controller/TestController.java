/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.redis.controller;

import cn.slience.redis.service.TestService;
import cn.slience.redis.service.TestService2;
import cn.slience.redis.service.TestService3;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

    @Autowired
    private TestService testService;

    @Autowired
    private TestService2 testService2;

    @Autowired
    private TestService3 testService3;

    /**
     *
     * @return
     */
    @RequestMapping("/")
    @ResponseBody
    public String test() {

        String result = "test is ok";

        try {
            result = new StringBuilder(InetAddress.getLocalHost().toString()).append(" : ").append(result).toString();
        } catch (UnknownHostException ex) {
        }

        return result;
    }

    /**
     * 缓存+查库 实现一 （查库使用JdbcTemplate）
     *
     * @return
     */
    @RequestMapping(value = "test/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String test(@PathVariable(name = "id") int id) {

        return testService.getGroupCategoryNameById(id);
    }

    /**
     * 缓存+查库 实现二 （查库使用MyBatis注解）
     *
     * @return
     */
    @RequestMapping(value = "test2/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String test2(@PathVariable(name = "id") int id) {

        return testService2.getGroupCategoryNameById(id);
    }

    /**
     * 缓存+查库 实现三 （查库使用MyBatis配置文件+SqlSession）
     *
     * @return
     */
    @RequestMapping(value = "test3/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String test3(@PathVariable(name = "id") int id) {

        return testService3.getGroupCategoryNameById(id);
    }

}
