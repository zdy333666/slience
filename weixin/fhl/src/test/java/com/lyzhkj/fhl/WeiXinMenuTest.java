package com.lyzhkj.fhl;


import com.lyzhkj.fhl.conf.WeiXinMenuConfig;
import com.lyzhkj.fhl.weixin.util.WeiXinMenuUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author breeze
 */
public class WeiXinMenuTest {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinMenuTest.class);
    
    public static void main(String[] args){
        
        //System.out.println("uuid-length-->"+UUID.randomUUID().toString().length());
        
        AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
        //
         LOGGER.info("WEIXIN_MENU_JSON-->"+WeiXinMenuConfig.WEIXIN_MENU_JSON);
        
        int result = WeiXinMenuUtil.createMenu(token.getAccessToken(), WeiXinMenuConfig.WEIXIN_MENU_JSON);
        if (result == 0) {
            LOGGER.info("创建Menu成功");
        } else {
            LOGGER.info("创建Menu失败");
            System.exit(-1);
        }
        
    }
    
}
