/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.service;

import com.hzcominfo.relation.conf.KafkaProducerFactory;
import com.hzcominfo.relation.conf.Neo4jSourceFactory;
import com.hzcominfo.relation.conf.RelationConfiguration;
import com.hzcominfo.relation.dao.KafkaDao;
import com.hzcominfo.relation.dao.RelationDao;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import kafka.producer.KeyedMessage;
import org.bson.BSONEncoder;
import org.bson.BasicBSONEncoder;
import org.bson.BasicBSONObject;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author llz
 */
@Component
public class BoltAsynRelationBuilder {

    private final Logger logger = LoggerFactory.getLogger(BoltAsynRelationBuilder.class);
    private final StringBuilder logBuilder = new StringBuilder();

    private final BlockingQueue queue = new LinkedBlockingQueue(10_0000);
    private final AtomicLong counter = new AtomicLong();

    private final Map<String, KeyedMessage<String, byte[]>> messages = new ConcurrentHashMap<>();

    int taskSize = 20;
    private ExecutorService executor;

    @Autowired
    private RelationDao relationDao;

    @Autowired
    private KafkaDao kafkaDao;

    /**
     *
     */
    public BoltAsynRelationBuilder() {
        Neo4jSourceFactory.getSource(RelationConfiguration.neo4j_bolt_uri);
    }

    /**
     *
     */
    @Scheduled(fixedRate = 10_000)
    public void service() {

        if (executor == null) {
            executor = Executors.newFixedThreadPool(taskSize);
            for (int n = 0; n < taskSize; n++) {
                Runnable task = new Runnable() {

                    @Override
                    public void run() {

                        Logger logger = LoggerFactory.getLogger(this.getClass());
                        StringBuilder logBuilder = new StringBuilder();

                        Driver driver = null;
                        Session session = null;
                        BSONEncoder encoder = new BasicBSONEncoder();

                        long startTime = System.currentTimeMillis();
                        long count = 0;
                        boolean ok = false;

                        try {
                            Map<String, Object> tuple = (Map<String, Object>) queue.take();
                            while (tuple != null) {

                                try {
                                    if (driver == null) {
                                        driver = Neo4jSourceFactory.getSource(RelationConfiguration.neo4j_bolt_uri);
                                    }

                                    session = driver.session();
                                    ok = relationDao.build(session, tuple);
                                    session.close();

                                    if (ok) {
                                        tuple.put("type", RelationConfiguration.relation_type);
                                        String key = UUID.randomUUID().toString();
                                        messages.put(key, new KeyedMessage(RelationConfiguration.kafka_target, key, encoder.encode(new BasicBSONObject(tuple))));

                                        count = counter.incrementAndGet();
                                    } else {
                                        tuple.remove("type");
                                        queue.put(tuple);
                                    }

                                    if (count % 1_000 == 0) {
                                        logger.info(logBuilder.delete(0, logBuilder.length()).append(count).append(" -- success --> ").append(tuple).append("-- spend time -- ").append((System.currentTimeMillis() - startTime) / 1_000).append(" ms").toString());
                                        startTime = System.currentTimeMillis();
                                    }

                                } catch (Exception ex) {
                                    queue.put(tuple);
                                    if (session != null && session.isOpen()) {
                                        session.close();
                                    }
                                    logger.error("", ex);
                                    Thread.sleep(3000);
                                }

                                tuple = (Map<String, Object>) queue.take();
                            }

                        } catch (Exception ex) {
                            logger.error("", ex);
                        }
                    }

                };

                executor.submit(task);
            }
        }

    }

    public boolean offer(Map<String, Object> tuple) {
        return queue.offer(tuple);
    }

    public boolean isOk() {
        return queue.isEmpty();
    }

    /**
     *
     */
    public void produceMessages() {
        long produceSt = System.currentTimeMillis();
        int length = 0;

        for (KeyedMessage<String, byte[]> message : messages.values()) {
            length += message.message().length;
        }

        while (kafkaDao.produce(KafkaProducerFactory.getProducer(), new LinkedList(messages.values())) == false) {
            try {
                Thread.sleep(3000);
            } catch (Exception ex) {
                logger.error(null, ex);
            }
        }

        logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- producer store ").append(messages.size()).append(" messages ").append(length / 1024).append(" KB spend time ").append(System.currentTimeMillis() - produceSt).append(" ms").toString());
        messages.clear();

    }

}
