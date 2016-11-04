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
    
    public static String relationType;
    public static String relationSavepointTable;
    
    public static String mongoSource;
    public static String kafkaSource;
    public static String kafkaTarget;

    //public static String neo4j_jdbc_uri;
    public static String neo4j_bolt_uri;
    
    public static String mongo_uri_rtdb;
    public static String mongo_uri_hzga;
    
    public static Properties kafkaConsumerConfig = new Properties();
    public static Properties kafkaProducerConfig = new Properties();
    
    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration();
            props.setDelimiterParsingDisabled(true);
            props.load("config/conf.properties");
            
            relationType = props.getString("relationType");
            relationSavepointTable = props.getString("relationSavepointTable");
            
            mongoSource = props.getString("mongoSource");
            kafkaSource = props.getString("kafkaSource");
            kafkaTarget = props.getString("kafkaTarget");

            //neo4j_jdbc_uri = props.getString("neo4j_jdbc_uri");
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
