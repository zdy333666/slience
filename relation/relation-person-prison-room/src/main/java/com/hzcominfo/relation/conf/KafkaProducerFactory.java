/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.conf;

import kafka.producer.ProducerConfig;
import kafka.javaapi.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cominfo4
 */
public class KafkaProducerFactory {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerFactory.class);

    private static volatile Producer<String, byte[]> producer;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                release();
                logger.info("note :  ********* all the producer connection to kafka has released *********");
            }
        });
    }

    public static Producer<String, byte[]> getProducer() {

        if (producer != null) {
            return producer;
        }

        synchronized (KafkaProducerFactory.class) {
            if (producer != null) {
                return producer;
            }

            try {
                ProducerConfig config = new ProducerConfig(RelationConfiguration.kafkaProducerConfig);
                producer = new Producer<>(config);
            } catch (Exception ex) {
                logger.error(null, ex);
            }
        }

        return producer;
    }

    /**
     *
     */
    private static void release() {
        if (producer != null) {
            producer.close();
        }
    }

}
