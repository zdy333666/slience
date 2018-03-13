/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.springboot.demo.service;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class AsyncService {

    private static final Logger logger = LoggerFactory.getLogger(AsyncService.class);

    private final AtomicLong counter = new AtomicLong();

    @Async
    public void service() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
       // logger.info("--{}-- I has complete this job.........", counter.incrementAndGet());
    }

}
