/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import java.util.List;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class KafkaDao {

    private final Logger logger = LoggerFactory.getLogger(KafkaDao.class);

    /**
     *
     * @param producer
     * @param message
     * @return
     */
    public boolean produce(Producer<String, byte[]> producer, KeyedMessage<String, byte[]> message) {

        try {
            producer.send(message);
        } catch (Exception ex) {
            logger.error(null, ex);
            return false;
        }

        return true;
    }

    /**
     *
     * @param producer
     * @param messages
     * @return
     */
    public boolean produce(Producer<String, byte[]> producer, List<KeyedMessage<String, byte[]>> messages) {

        try {
            producer.send(messages);
        } catch (Exception ex) {
            logger.error(null, ex);
            return false;
        }

        return true;
    }

}
