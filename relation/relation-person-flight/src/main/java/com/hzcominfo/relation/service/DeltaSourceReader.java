/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.service;

import com.hzcominfo.relation.util.TupleChecker;
import com.hzcominfo.relation.conf.RelationConfiguration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.bson.BSONDecoder;
import org.bson.BSONObject;
import org.bson.BasicBSONDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class DeltaSourceReader {

    private final Logger logger = LoggerFactory.getLogger(DeltaSourceReader.class);
    private final StringBuilder logBuilder = new StringBuilder();

    private final AtomicLong counter = new AtomicLong();

    private ConsumerConnector consumerConnector;
    private ExecutorService executor;

    @Autowired
    private BoltAsynRelationBuilder relationBuilder;

    /**
     *
     */
    @Scheduled(fixedDelay = 10_000)
    public void read() {

        if (consumerConnector != null) {
            return;
        }

        if (executor != null && (!executor.isShutdown())) {
            return;
        }

        try {
            ConsumerConfig consumerConfig = new ConsumerConfig(RelationConfiguration.kafkaConsumerConfig);
            if (consumerConnector == null) {
                consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
            }

            Map<String, Integer> topicStreamsConfig = new LinkedHashMap<>();
            topicStreamsConfig.put(RelationConfiguration.kafka_source, 1);

            Map<String, List<KafkaStream<byte[], byte[]>>> topicMessageStreams = consumerConnector.createMessageStreams(topicStreamsConfig);

            int n = 0;
            for (List<KafkaStream<byte[], byte[]>> streams : topicMessageStreams.values()) {
                n += streams.size();
            }
            if (executor == null) {
                executor = Executors.newFixedThreadPool(n);
            }

            for (Map.Entry<String, List<KafkaStream<byte[], byte[]>>> messageStreamsOfTopic : topicMessageStreams.entrySet()) {

                List<KafkaStream<byte[], byte[]>> streams = messageStreamsOfTopic.getValue();

                logger.info(logBuilder.delete(0, logBuilder.length()).append("").append(" -- topic = ").append(messageStreamsOfTopic.getKey()).append(" -- streams.size ---> ").append(streams.size()).toString());

                if (streams.isEmpty()) {
                    continue;
                }

                for (final KafkaStream<byte[], byte[]> stream : streams) {

                    executor.submit(new Runnable() {
                        private final Logger logger = LoggerFactory.getLogger(getClass());

                        private long currCount = 0L;
                        private final BSONDecoder decoder = new BasicBSONDecoder();

                        @Override
                        public void run() {

                            Map<String, Object> tuple = null;

                            for (MessageAndMetadata<byte[], byte[]> msgAndMetadata : stream) {

                                currCount = counter.incrementAndGet();

                                BSONObject message = decoder.readObject(msgAndMetadata.message());
                                BSONObject value = (BSONObject) message.get("value");
                                tuple = value.toMap();

                                tuple = TupleChecker.check(tuple);
                                if (tuple == null) {
                                    continue;
                                }

                                while (!relationBuilder.offer(tuple)) {
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException ex) {
                                        logger.error("error happened when thread try to sleep !", ex);
                                    }
                                }

                                if (currCount % 1_00 == 0) {
                                    while (!relationBuilder.isOk()) {
                                        try {
                                            Thread.sleep(10);
                                        } catch (InterruptedException ex) {
                                            logger.error("error happened when thread try to sleep !", ex);
                                        }
                                    }

                                    relationBuilder.produceMessages();
                                    consumerConnector.commitOffsets(true);

                                    //logger.info(currCount + " ------------------- queue is ready ................ ");
                                }

                            }

                        }
                    });

                }
            }

        } catch (Exception ex) {
            if (consumerConnector != null) {
                consumerConnector.shutdown();
            }
            if (executor != null && (!executor.isShutdown())) {
                executor.shutdown();
            }

            logger.error("", ex);
        }

    }

}
