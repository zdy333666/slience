/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabinfo.conf;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zdy
 */
public class GABInfoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(GABInfoConfiguration.class);

    public static String SERVER_URL;
    public static String SERVER_ID_QGRK;
    public static String CLIENT_ID;
    public static String TABLE_GAB_QGRK_BACKUP;
    public static String MONGO_URI_HZGA;

    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration();
            props.setDelimiterParsingDisabled(true);
            props.load("config/conf.properties");

            SERVER_URL = props.getString("server_url");
            SERVER_ID_QGRK = props.getString("server_id_qgrk");
            CLIENT_ID = props.getString("client_id");
            TABLE_GAB_QGRK_BACKUP = props.getString("gab_qgrk_backup_table");
            MONGO_URI_HZGA = props.getString("mongo_uri_hzga");

        } catch (Exception ex) {
            logger.error("", ex);
            System.exit(0);
        }
    }

}
