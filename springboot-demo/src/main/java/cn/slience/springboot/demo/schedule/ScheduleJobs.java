/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.springboot.demo.schedule;

import cn.slience.springboot.demo.service.AsyncService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author breeze
 */
@Component
public class ScheduleJobs {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleJobs.class);

    private final AtomicLong counter = new AtomicLong();
    private final static long SECOND = 1 * 1000;
    private SimpleDateFormat fdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private AsyncService asyncService;

    @CacheEvict(value = "models", allEntries = true)
    @Scheduled(fixedDelay = 60000)
    public void deleteFromCache() {
    }

    @Scheduled(fixedDelay = SECOND * 2)
    public void fixedDelayJob() throws InterruptedException {
        TimeUnit.SECONDS.sleep(2);
        logger.info("-- [FixedDelayJob Execute]" + fdf.format(new Date()));
    }

    @Scheduled(fixedRate = SECOND * 1)
    public void fixedRateJob() throws InterruptedException {
        logger.info("-- [FixedRateJob Execute]--{}", fdf.format(new Date()));
        //asyncService.service();
    }

}
