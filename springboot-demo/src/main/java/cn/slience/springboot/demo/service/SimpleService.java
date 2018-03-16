/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.springboot.demo.service;

import cn.slience.springboot.demo.pojo.SimpleModel;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class SimpleService {

    private static final Logger logger = LoggerFactory.getLogger(SimpleService.class);

    @Cacheable(value = "models", key = "#name")//, condition = "#name !=  'slience' ")
    public SimpleModel testCache(String name) throws InterruptedException {

        logger.info("create s SimpleModel instance...");

        TimeUnit.SECONDS.sleep(1);
        SimpleModel simpleModel = new SimpleModel();
        simpleModel.setName(name.toUpperCase());
        simpleModel.setAddress("HangZhou");

        return simpleModel;
    }

    @CacheEvict(value = "models", key = "#name")
    public void deleteFromCache(String name) {
    }

    @CachePut(value = "models", key = "#name")
    public SimpleModel saveModel(String name, String address) {
        return new SimpleModel(name, address);
    }

}
