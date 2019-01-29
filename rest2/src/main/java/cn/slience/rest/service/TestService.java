/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.rest.service;

import cn.slience.rest.client.Rest1Client;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Len
 */
@Service
public class TestService {

    @Autowired
    private Rest1Client rest1Client;

    @HystrixCommand(fallbackMethod = "defaultEcho")
    public String echo() {

        long startTime = System.currentTimeMillis();
        String result = rest1Client.echo();
        long endTime = System.currentTimeMillis();

        System.out.println(Thread.currentThread().getName() + "--spend " + (endTime - startTime) + " ms at " + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        return result;
    }

    public String defaultEcho() {

        return "has no rest1 found";
    }

}
