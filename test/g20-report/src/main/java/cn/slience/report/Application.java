/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.report;

import cn.slience.report.filter.SimpleFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
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
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }

    @Bean
    public SimpleFilter simpleFilter() {
        return new SimpleFilter();
    }

}
