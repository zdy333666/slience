/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author slience
 */
//@EnableDiscoveryClient
//@EnableZuulProxy
@SpringBootApplication()
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }
//
//    @Bean
//    public SimpleFilter simpleFilter() {
//        return new SimpleFilter();
//    }
//
//    @Bean
//    ClientHttpRequestFactory clientHttpRequestFactory() {
//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
//        factory.setReadTimeout(5000);
//        factory.setConnectTimeout(5000);
//
//        return factory;
//    }

//    @LoadBalanced
//    @Bean
//    RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    @RequestMapping("/hi")
//    public String hi(@RequestParam(value = "name", defaultValue = "Artaban") String name) {
//        String greeting = this.restTemplate.getForObject("http://service-hello/greeting", String.class);
//        return String.format("%s, %s!", greeting, name);
//    }
}

@RefreshScope
@RestController
class MessageRestController {

    @Value("${message}")
    private String message;

    @RequestMapping("/message")
    String getMessage() {
        return this.message;
    }
}
