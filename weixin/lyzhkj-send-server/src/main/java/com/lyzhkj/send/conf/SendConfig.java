/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.send.conf;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author breeze
 */
public class SendConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendConfig.class);

    public static String NOTICEMESSAGE_WEBSERVICE_SERVER_IP;
    public static int NOTICEMESSAGE_WEBSERVICE_SERVER_PORT;

    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration();
            props.setDelimiterParsingDisabled(true);
            props.load("config/send.properties");

            NOTICEMESSAGE_WEBSERVICE_SERVER_IP = props.getString("send.noticeMessage.webservice.server.ip");
            NOTICEMESSAGE_WEBSERVICE_SERVER_PORT = props.getInt("send.noticeMessage.webservice.server.port");

            LOGGER.info(new StringBuilder("remote send.noticeMessage.webservice>>").append(NOTICEMESSAGE_WEBSERVICE_SERVER_IP).append(":").append(NOTICEMESSAGE_WEBSERVICE_SERVER_PORT).toString());
            props = null;
        } catch (ConfigurationException ex) {
            LOGGER.error("failed to load config/send.properties", ex);
            System.exit(0);
        }
    }
}
