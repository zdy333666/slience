/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.gateway;

import cn.slience.gateway.filter.SimpleFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author slience
 */
@EnableDiscoveryClient
@EnableZuulProxy
@SpringBootApplication
@RestController
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }

    @Bean
    public SimpleFilter simpleFilter() {
        return new SimpleFilter();
    }

}
