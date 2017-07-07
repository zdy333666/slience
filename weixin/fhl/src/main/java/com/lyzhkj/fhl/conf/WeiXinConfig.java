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
import org.springframework.stereotype.Component;

/**
 *
 * @author breeze
 */
@Component
public class WeiXinConfig {

    private static Logger logger = LoggerFactory.getLogger(WeiXinConfig.class);

    public static String TOKEN;

    public static String APP_ID;
    public static String APP_SECRET;

    //令牌
    public static String ACCESS_TOKEN_URL;

    //网页授权令牌
    public static String WEB_ACCESS_TOKEN_URL;
    
    //获取用户基本信息
    public static String USER_INFO_URL;

    //获取二维码ticket
    public static String CREATE_QR_URL;

    //用ticket换取二维码图片
    public static String GET_QR_IMAGE;

    //上传二维码图片，获得素材标识
    public static String UPLOAD_URL;

    //消息回复
    public static String REPLY_MESSAGE_URL;

    //微信JSSDK的ticket
    public static String JSSDK_TICKET;

    //创建Menu
    public static String CREATE_MENU_URL;

    //查询菜单
    public static String GET_MENU_URL;

    //删除Menu
    public static String DELETE_MENU_URL;

    //订单查询getbyid
    public static String ORDER_GETBYID_URL;

    //订单查询getbyfilter
    public static String ORDER_GETBYFILTER_URL;

    //订单设置setdelivery
    public static String ORDER_SETDELIVERY_URL;

    //#创建货架
    public static String CREATE_SHELF_URL;

    //获取所有货架
    public static String GETALL_SHELIES_URL;

    //删除货架
    public static String DELETE_SHELF_URL;

    static {
        try {
            PropertiesConfiguration props = new PropertiesConfiguration();
            props.setDelimiterParsingDisabled(true);
            props.load("config/weixin.properties");

            TOKEN = props.getString("wx.token");

            APP_ID = props.getString("wx.appID");
            APP_SECRET = props.getString("wx.appSecret");

            ACCESS_TOKEN_URL = props.getString("wx.access_token_url");
            WEB_ACCESS_TOKEN_URL = props.getString("wx.web_access_token_url");
            USER_INFO_URL = props.getString("wx.user_info_url");

            CREATE_QR_URL = props.getString("wx.create_qr_url");
            GET_QR_IMAGE = props.getString("wx.get_qr_image");

            UPLOAD_URL = props.getString("wx.upload_url");
            REPLY_MESSAGE_URL = props.getString("wx.reply_message_url");
            JSSDK_TICKET = props.getString("wx.jssdk_ticket");

            CREATE_MENU_URL = props.getString("wx.create_menu_url");
            GET_MENU_URL = props.getString("wx.get_menu_url");
            DELETE_MENU_URL = props.getString("wx.delete_menu_url");

            ORDER_GETBYID_URL = props.getString("wx.order_getbyid_url");
            ORDER_GETBYFILTER_URL = props.getString("wx.order_getbyfilter_url");
            ORDER_SETDELIVERY_URL = props.getString("wx.order_setdelivery_url");

            CREATE_SHELF_URL = props.getString("wx.create_shelf_url");
            GETALL_SHELIES_URL = props.getString("wx.getall_shelies_url");
            DELETE_SHELF_URL = props.getString("wx.delete_shelf_url");

//            logger.info(new StringBuilder("APP_ID").append(":").append(APP_ID).toString());
//            logger.info(new StringBuilder("APP_SECRET").append(":").append(APP_SECRET).toString());
//            logger.info(new StringBuilder("ACCESS_TOKEN_URL").append(":").append(ACCESS_TOKEN_URL).toString());
            props = null;
        } catch (ConfigurationException ex) {
            logger.error("failed to load config/weixin.properties", ex);
            System.exit(0);
        }
    }
}
