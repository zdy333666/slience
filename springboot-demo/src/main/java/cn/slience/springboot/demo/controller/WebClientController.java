/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.springboot.demo.controller;

import cn.slience.springboot.demo.pojo.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author breeze
 */
@Controller
@RequestMapping(value = "web")
public class WebClientController {

    private static final Logger logger = LoggerFactory.getLogger(WebClientController.class);

    @Autowired
    private WebClient webClient;

    @RequestMapping(value = "test", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Mono<Greeting> test() {
        return webClient.get().uri("/greeting").retrieve().bodyToMono(Greeting.class
        );
    }

    @GetMapping("demo")
    @ResponseBody
    public Flux<Greeting> demo() {
        return webClient.get().uri("/greeting").retrieve().bodyToFlux(Greeting.class
        );
    }

}
