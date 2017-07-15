/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.redis.service;

import cn.slience.redis.dao.TestMyBatisDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class TestService3 {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestService3.class);

    public static final String PREFIX = "group_category.";

    @Autowired
    private RedisServiceImpl redisServiceImpl;

    @Autowired
    private TestMyBatisDAO testMyBatisDAO;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public String getGroupCategoryNameById(int id) {

        String result = null;

        String key = new StringBuilder(PREFIX).append(id).toString();

        if (redisTemplate.hasKey(key)) {
            result = redisServiceImpl.get(key);

            LOGGER.info("get value from redis with key:{}", key);
            return result;
        }

        result = testMyBatisDAO.getGroupCategoryNameById(id);
        LOGGER.info("get value from mysql with id:{}", id);

        if (result != null) {
            redisServiceImpl.set(key, result);

            LOGGER.info("set value to redis with key:{}", key);
        }

        return result;
    }

}
