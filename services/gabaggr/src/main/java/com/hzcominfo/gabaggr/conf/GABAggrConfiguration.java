/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabaggr.conf;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zdy
 */
public class GABAggrConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(GABAggrConfiguration.class);

    public static String table_auth_user;

    public static String table_gab_serviceset_define;
    public static String table_gab_serviceset_param;
    public static String table_gab_service_define;
    public static String table_gab_region_define;
    public static String table_gab_service_field_define;

    public static String table_gab_result;

    public static int submit_queue_max_size = 100;
    public static int submit_queue_processor_max_size = 10;
    //public static int submit_tuple_processor_max_size = 1;
    public static int gab_result_expired_seconds = 60;

    public static String mongo_uri_rtdb;

    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration();
            props.setDelimiterParsingDisabled(true);
            props.load("config/conf.properties");

            table_auth_user = props.getString("table_auth_user");

            table_gab_serviceset_define = props.getString("table_gab_serviceset_define");
            table_gab_serviceset_param = props.getString("table_gab_serviceset_param");
            table_gab_service_define = props.getString("table_gab_service_define");
            table_gab_region_define = props.getString("table_gab_region_define");
            table_gab_service_field_define = props.getString("table_gab_service_field_define");

            table_gab_result = props.getString("table_gab_result");

            submit_queue_max_size = props.getInt("submit_queue_max_size");
            submit_queue_processor_max_size = props.getInt("submit_queue_processor_max_size");
            //submit_tuple_processor_max_size = props.getInt("submit_tuple_processor_max_size");
            gab_result_expired_seconds = props.getInt("gab_result_expired_seconds");

            mongo_uri_rtdb = props.getString("mongo_uri_rtdb");

        } catch (Exception ex) {
            logger.info("", ex);
            System.exit(0);
        }
    }

}
