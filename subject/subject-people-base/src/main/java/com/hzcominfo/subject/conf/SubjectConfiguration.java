/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.subject.conf;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zdy
 */
public class SubjectConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SubjectConfiguration.class);

    public static String table_hbase_person;
    public static String table_people_base;
    public static String hbase_cf_base;
    public static String hbase_column_prefix;
    public static String mongo_uri_people;

    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration();
            props.setDelimiterParsingDisabled(true);
            props.load("config/conf.properties");

            table_hbase_person = props.getString("table_hbase_person");
            table_people_base = props.getString("table_people_base");
            hbase_cf_base = props.getString("hbase_cf_base");
            hbase_column_prefix = props.getString("hbase_column_prefix");
            mongo_uri_people = props.getString("mongo_uri_people");

            props.clear();
        } catch (Exception ex) {
            logger.error("", ex);
            System.exit(0);
        }
    }

}
