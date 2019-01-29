/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.rest.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 *
 * @author Len
 */
@RestController
public class TestEchoController {

    @RequestMapping(value = "echo", method = {RequestMethod.GET, RequestMethod.POST})
    public Mono<String> echo(ServerHttpRequest request) {

        System.out.println(Thread.currentThread().getName() + "--request from-->" + request.getRemoteAddress() + "   at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        return Mono.just("welcome to rest1");
    }

}
