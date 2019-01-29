/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.rest.scheduler;

import cn.slience.rest.service.TestService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author Len
 */
@Component
public class TestScheduler {

    @Autowired
    private TestService testService;

//    @Scheduled(fixedDelay = 2000)
//    private void test() {
//
//        System.out.println(Thread.currentThread().getName() + "--now-->" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
//    }
    @Scheduled(fixedDelay = 1)
    public void echo() {

        testService.echo();
    }

    @Scheduled(fixedDelay = 1)
    public void echo2() {

        testService.echo();
    }

}
