package com.hzcominfo.photo;

import com.hzcominfo.photo.filter.SimpleFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

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
//@EnableCircuitBreaker
//@EnableAsync
//@EnableScheduling
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }

    @Bean
    public SimpleFilter simpleFilter() {
        return new SimpleFilter();
    }

}
