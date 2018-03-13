/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.kafka.demo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author breeze
 */
public class ConsumerApplication {

    private static final Logger logger = LoggerFactory.getLogger(ConsumerApplication.class);

    public static void main(String[] args) {

        KafkaConsumer<String, String> consumer = null;
        MongoClient client = null;

        try {
            Properties props = new Properties();
            props.put("bootstrap.servers", "192.168.10.86:9092,192.168.10.87:9092,192.168.10.88:9092");
            props.put("group.id", "test-113");
            props.put("client.id", "test");
            props.put("max.poll.records", "10000");
            props.put("max.poll.interval.ms", "30000");
            props.put("auto.offset.reset", "earliest"); //latest, earliest, none
            props.put("fetch.max.wait.ms", "3000");
            props.put("fetch.min.bytes", "1024000");
            props.put("enable.auto.commit", "false");
            props.put("auto.commit.interval.ms", "1000");
            props.put("session.timeout.ms", "30000");
            props.put("send.buffer.bytes", "1024000");//131072
            props.put("receive.buffer.bytes", "1024000");//65536
            props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Arrays.asList("TOPIC_PACKET_TO_MONGO"));
            logger.info("consumer:{}", consumer);

            MongoClientURI uri = new MongoClientURI("mongodb://192.168.10.86:30000/demo?w=1");
            client = new MongoClient(uri);

            MongoCollection collection = client.getDatabase(uri.getDatabase()).getCollection("shard_demo");
            collection.drop();

            int total = 0;
            List<Document> docs = new LinkedList<>();

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(10000);
                for (ConsumerRecord<String, String> record : records) {
                    //logger.info("offset = {}, key = {}, value = {}", record.offset(), record.key(), record.value());
                    try {
                        MD5Util.MD5(record.value());
                        docs.add(Document.parse(record.value()));
                    } catch (Exception e) {
                    }
                }

                if (!docs.isEmpty()) {
                    //collection.insertMany(docs);
                }

                total += docs.size();
                logger.info("batch:{}, total:{}", docs.size(), total);
                docs.clear();
            }

            //consumer.close();
        } catch (Exception ex) {
            logger.error("", ex);
        } finally {
            if (client != null) {
                client.close();
            }
            logger.info("mongo client closed!");

            if (consumer != null) {
                consumer.close();
            }
            logger.info("kafka consumer closed!");
        }

    }

}
