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

    public static String WEIXIN_TOKEN_SERVER;
    public static String APP_URL;

    public static String NOTICEMESSAGE_WEBSERVICE_SERVER_IP;
    public static int NOTICEMESSAGE_WEBSERVICE_SERVER_PORT;

    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration();
            props.setDelimiterParsingDisabled(true);
            props.load("config/fhl.properties");

            WEIXIN_TOKEN_SERVER = props.getString("fhl.weixin_token_server");
            APP_URL = props.getString("fhl.app_url");

            NOTICEMESSAGE_WEBSERVICE_SERVER_IP = props.getString("fhl.noticeMessage.webservice.server.ip");
            NOTICEMESSAGE_WEBSERVICE_SERVER_PORT = props.getInt("fhl.noticeMessage.webservice.server.port");

            LOGGER.info(new StringBuilder("fhl.weixin_token_server").append(":").append(WEIXIN_TOKEN_SERVER).toString());

            props = null;
        } catch (ConfigurationException ex) {
            LOGGER.error("failed to load config/fhl.properties", ex);
            System.exit(0);
        }
    }
}
