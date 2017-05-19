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
public class WeiXinMenuUtil {

    //private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinMediaHandler.class);
    public static int createMenu(String accessToken, String message) {
        String url = WeiXinConfig.CREATE_MENU_URL.replace("ACCESS_TOKEN", accessToken);
        // Map<String,Object> result = new RestTemplate().postForObject(url, message, Map.class);
        JSONObject result = HttpUtil.doPostStr(url, message);
        return result.getInt("errcode");
    }

    public static JSONObject getMenu(String accessToken) {
        String url = WeiXinConfig.GET_MENU_URL.replace("ACCESS_TOKEN", accessToken);
        //String result = new RestTemplate().getForObject(url, String.class);

        return HttpUtil.doGetStr(url);
    }

    public static JSONObject deleteMenu(String accessToken) {
        String url = WeiXinConfig.DELETE_MENU_URL.replace("ACCESS_TOKEN", accessToken);
//        JSONObject result = new RestTemplate().getForObject(url, String.class);

        return HttpUtil.doGetStr(url);
    }

}
