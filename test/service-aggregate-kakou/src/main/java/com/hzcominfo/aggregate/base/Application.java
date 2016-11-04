package com.hzcominfo.aggregate.base;

import com.hzcominfo.aggregate.base.filter.SimpleZuulFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zdy
 */
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
@EnableAsync
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }

    @Bean
    public SimpleZuulFilter simpleFilter() {
        return new SimpleZuulFilter();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
