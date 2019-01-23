/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.dao.controller;

import cn.slience.dao.model.Test;
import cn.slience.dao.service.TestService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author Len
 */
@RestController
public class TestController {

    private final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestService testService;

    @RequestMapping(value = "test", method = RequestMethod.POST)
    public Mono<String> test(ServerHttpRequest request) {

        System.out.println(Thread.currentThread().getName() + "--request from-->" + request.getRemoteAddress() + "   at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        int result = 0;
        try {
            result = testService.test();
        } catch (Exception e) {
            logger.error("error:", e);
        }

        return Mono.just("result:" + result);
    }

    @RequestMapping(value = "query", method = RequestMethod.GET)
    public Flux<Test> query(ServerHttpRequest request) {

        System.out.println(Thread.currentThread().getName() + "--request from-->" + request.getRemoteAddress() + "   at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Flux result = Flux.empty();
        try {
            result = Flux.fromIterable(testService.query());
        } catch (Exception e) {
            logger.error("error:", e);
        }

        return result;
    }

    @RequestMapping(value = "queryById", method = RequestMethod.GET)
    public Mono<Test> queryById(ServerHttpRequest request) {

        System.out.println(Thread.currentThread().getName() + "--request from-->" + request.getRemoteAddress() + "   at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        Mono result = Mono.empty();
        try {
            result = Mono.just(testService.queryById());
        } catch (Exception e) {
            logger.error("error:", e);
        }

        return result;
    }

}
