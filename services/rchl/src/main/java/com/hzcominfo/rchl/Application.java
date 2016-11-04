package com.hzcominfo.rchl;

import com.hzcominfo.rchl.filter.SimpleZuulFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
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
@SpringBootApplication
@EnableAutoConfiguration(exclude = SolrAutoConfiguration.class)
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }

    @Bean
    public SimpleZuulFilter simpleFilter() {
        return new SimpleZuulFilter();
    }
}
