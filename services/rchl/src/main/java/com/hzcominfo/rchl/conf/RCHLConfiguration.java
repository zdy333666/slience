/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.rchl.conf;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zdy
 */
public class RCHLConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RCHLConfiguration.class);

    public static String table_code_dpt;
    public static String table_code_jcz;
    public static String table_ryhc;
    public static String table_clhc;

    //public static String mongo_uri_rtdb;
    public static String mongo_uri_hzga;

    public static String solr_uri_ryhc;
    public static String solr_uri_clhc;

    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration();
            props.setDelimiterParsingDisabled(true);
            props.load("config/conf.properties");

            table_code_dpt = props.getString("table_code_dpt");
            table_code_jcz = props.getString("table_code_jcz");
            table_ryhc = props.getString("table_ryhc");
            table_clhc = props.getString("table_clhc");

            //mongo_uri_rtdb = props.getString("mongo_uri_rtdb");
            mongo_uri_hzga = props.getString("mongo_uri_hzga");

            solr_uri_ryhc = props.getString("solr_uri_ryhc");
            solr_uri_clhc = props.getString("solr_uri_clhc");

            props = null;
        } catch (Exception ex) {
            logger.error("", ex);
            System.exit(0);
        }
    }

}
