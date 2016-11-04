/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.conf;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zdy
 */
public class RelationConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RelationConfiguration.class);

    public static String relation_define_table;
    public static String people_base_table;
    public static String people_bad_table;
    public static String airport_translate_table;

    public static String neo4j_bolt_uri;
    public static String mongo_uri_rtdb;
    public static String mongo_uri_people;

    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration();
            props.setDelimiterParsingDisabled(true);
            props.load("config/conf.properties");

            relation_define_table = props.getString("relation_define_table");
            people_base_table = props.getString("people_base_table");
            people_bad_table = props.getString("people_bad_table");
            airport_translate_table = props.getString("airport_translate_table");

            neo4j_bolt_uri = props.getString("neo4j_bolt_uri");
            mongo_uri_rtdb = props.getString("mongo_uri_rtdb");
            mongo_uri_people = props.getString("mongo_uri_people");

            props = null;
        } catch (Exception ex) {
            logger.error("", ex);
            System.exit(0);
        }
    }

}
