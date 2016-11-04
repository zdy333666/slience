/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.conf;

import java.util.Iterator;
import java.util.Properties;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zdy
 */
public class RelationConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RelationConfiguration.class);

    public static String relation_type;
    public static String relation_savepoint_table;

    public static String mongo_zfba_source;
    public static String mongo_dfk_source;
    
    public static String kafka_zfba_source;
    public static String kafka_dfk_source;
    public static String kafka_target;

    public static String neo4j_bolt_uri;

    public static String mongo_uri_rtdb;
    public static String mongo_uri_hzga;

    public static final Properties kafkaConsumerConfig = new Properties();
    public static final Properties kafkaProducerConfig = new Properties();

    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration();
            props.setDelimiterParsingDisabled(true);
            props.load("config/conf.properties");

            relation_type = props.getString("relation_type");
            relation_savepoint_table = props.getString("relation_savepoint_table");

            mongo_dfk_source = props.getString("mongo_dfk_source");
            mongo_zfba_source = props.getString("mongo_zfba_source");
            
            kafka_zfba_source = props.getString("kafka_zfba_source");
            kafka_dfk_source = props.getString("kafka_dfk_source");
            kafka_target = props.getString("kafka_target");

            neo4j_bolt_uri = props.getString("neo4j_bolt_uri");

            mongo_uri_rtdb = props.getString("mongo_uri_rtdb");
            mongo_uri_hzga = props.getString("mongo_uri_hzga");

            props.clear();
            //-------------------------------------
            props.load("config/kafka-consumer.properties");
            Iterator<String> consumer_it = props.getKeys();
            while (consumer_it.hasNext()) {
                String key = consumer_it.next();
                kafkaConsumerConfig.put(key, props.getProperty(key));
            }
            props.clear();
            //---------------------------------------

            props.load("config/kafka-producer.properties");
            Iterator<String> producer_it = props.getKeys();
            while (producer_it.hasNext()) {
                String key = producer_it.next();
                kafkaProducerConfig.put(key, props.getProperty(key));
            }
            props.clear();
        } catch (Exception ex) {
            logger.error("", ex);
            System.exit(0);
        }
    }

}
