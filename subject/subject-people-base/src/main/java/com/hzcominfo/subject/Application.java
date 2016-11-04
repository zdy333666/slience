package com.hzcominfo.subject;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zdy
 */
@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(false).run(args);
    }

}
