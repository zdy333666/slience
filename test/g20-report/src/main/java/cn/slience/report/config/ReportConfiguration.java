/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.report.config;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zdy
 */
public class ReportConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ReportConfiguration.class);

    public static String bggr_jdbc_url;
    public static String bggr_jdbc_username;
    public static String bggr_jdbc_password;

    //public static String mongo_uri_rtdb;
    //public static String mongo_uri_hzga;

    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration();
            props.setDelimiterParsingDisabled(true);
            props.load("config/conf.properties");

            bggr_jdbc_url = props.getString("bggr_jdbc_url");
            bggr_jdbc_username = props.getString("bggr_jdbc_username");
            bggr_jdbc_password = props.getString("bggr_jdbc_password");

            //mongo_uri_rtdb = props.getString("mongo_uri_rtdb");
            //mongo_uri_hzga = props.getString("mongo_uri_hzga");

            props = null;
        } catch (Exception ex) {
            logger.error("", ex);
            System.exit(0);
        }
    }

}
