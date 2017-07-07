/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.conf;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author breeze
 */
public class FHLConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(FHLConfig.class);

    //public static String WEIXIN_TOKEN_SERVER;
    public static String APP_URL;

    public static String NOTICEMESSAGE_WEBSERVICE_SERVER_IP;
    public static int NOTICEMESSAGE_WEBSERVICE_SERVER_PORT;

    public static String NET_SERVER_IP;
    public static int NET_SERVER_PORT;
    public static String JPUSH_APP_KEY;
    public static String JPUSH_MASTER_SECRET;

    public static String IGTPUSH_APPID;
    public static String IGTPUSH_APPKEY;
    public static String IGTPUSH_MASTER;
    public static String IGTPUSH_HOST;

    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration();
            props.setDelimiterParsingDisabled(true);
            props.load("config/fhl.properties");

            //WEIXIN_TOKEN_SERVER = props.getString("fhl.weixin_token_server");
            APP_URL = props.getString("fhl.app_url");

            NOTICEMESSAGE_WEBSERVICE_SERVER_IP = props.getString("fhl.noticeMessage.webservice.server.ip");
            NOTICEMESSAGE_WEBSERVICE_SERVER_PORT = props.getInt("fhl.noticeMessage.webservice.server.port");

            NET_SERVER_IP = props.getString("fhl.netServerIp");
            NET_SERVER_PORT = props.getInt("fhl.netServerPort");
            JPUSH_APP_KEY = props.getString("fhl.jpush_app_key");
            JPUSH_MASTER_SECRET = props.getString("fhl.jpush_master_secret");

            IGTPUSH_APPID = props.getString("fhl.igtpush_appId");
            IGTPUSH_APPKEY = props.getString("fhl.igtpush_appKey");
            IGTPUSH_MASTER = props.getString("fhl.igtpush_master");
            IGTPUSH_HOST = props.getString("fhl.igtpush_host");

            props = null;

            //LOGGER.info(new StringBuilder("fhl.weixin_token_server").append(":").append(WEIXIN_TOKEN_SERVER).toString());
        } catch (ConfigurationException ex) {
            LOGGER.error("failed to load config/fhl.properties", ex);
            System.exit(0);
        }
    }
}
