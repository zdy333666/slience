/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.photo.conf;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zdy
 */
public class PhotoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(PhotoConfiguration.class);

    public static String userCardId;
    public static String userName;
    public static String userDept;

    public static String table_photo_czrk;
    public static String table_photo_jwry;

    public static boolean photo_czrk_enabled;
    public static boolean photo_gab_enabled;
    public static boolean photo_default_enabled;

    public static String mongo_uri_rtdb;
    public static String mongo_uri_zpdb;

    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration();
            props.setDelimiterParsingDisabled(true);
            props.load("config/conf.properties");

            userCardId = props.getString("userCardId");
            userName = props.getString("userName");
            userDept = props.getString("userDept");

            table_photo_czrk = props.getString("table_photo_czrk");
            table_photo_jwry = props.getString("table_photo_jwry");
            photo_czrk_enabled = props.getBoolean("photo_czrk_enabled");
            photo_gab_enabled = props.getBoolean("photo_gab_enabled");
            photo_default_enabled = props.getBoolean("photo_default_enabled");

            mongo_uri_rtdb = props.getString("mongo_uri_rtdb");
            mongo_uri_zpdb = props.getString("mongo_uri_zpdb");
            
            props=null;
        } catch (Exception ex) {
            logger.error("", ex);
            System.exit(0);
        }
    }

}
