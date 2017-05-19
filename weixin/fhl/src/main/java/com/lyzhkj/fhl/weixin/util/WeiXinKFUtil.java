/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.weixin.util;

import com.lyzhkj.fhl.conf.WeiXinConfig;
import net.sf.json.JSONObject;

/**
 *
 * @author breeze
 */
public class WeiXinKFUtil {
    
    public static void sendMessage(String accessToken, String message) {
        String url = WeiXinConfig.REPLY_MESSAGE_URL.replace("ACCESS_TOKEN", accessToken);
        
        JSONObject result = HttpUtil.doPostStr(url, message);
        
        System.out.println("kf-send-result--->" + result);
    }
    
}
