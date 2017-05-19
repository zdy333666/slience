/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.weixin.util;

import com.lyzhkj.fhl.conf.WeiXinConfig;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import java.net.URLEncoder;
import java.util.Random;
import net.sf.json.JSONObject;

/**
 *
 * @author breeze
 */
public class WeiXinUserUtil {

    public static String AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
    //public static String ACCESS_TOKEN= "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    public static String REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
    public static String USERINFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    public static String CHECK = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";
    // 第一步：用户同意授权，获取code

    public static String getCodeRequest(String url) {
        StringBuilder str = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize");
        str.append("?appid=").append(urlEnodeUTF8(WeiXinConfig.APP_ID));
        str.append("&redirect_uri=").append(urlEnodeUTF8(url));
        str.append("&response_type=code");
        str.append("&scope=snsapi_base");
        str.append("&state=").append(new Random().nextInt(10000));
        str.append("#wechat_redirect");
        return str.toString();
    }

    /*public static String getCodeRequest(String url){
        String result = null;
        AUTHORIZE  = AUTHORIZE.replace("APPID", urlEnodeUTF8(ServerConfig.instance().appID));
        AUTHORIZE  = AUTHORIZE.replace("REDIRECT_URI",urlEnodeUTF8(url));
        AUTHORIZE = AUTHORIZE.("STATE",new Random().nextInt(10000));
        AUTHORIZE = AUTHORIZE.replace("SCOPE","snsapi_base");
        result = AUTHORIZE.toString();
        return result;
    }*/
    //第二步：通过code换取网页授权access_token
    public static WebPageAccessToken getAccessToken(String code) {
        WebPageAccessToken token = null;
//        if (CacheManager.instance().pageTokenMap.containsKey(code)) {
//            token = CacheManager.instance().pageTokenMap.get(code);
//        } else {
            String ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
            ACCESS_TOKEN = ACCESS_TOKEN.replace("APPID", urlEnodeUTF8(WeiXinConfig.APP_ID));
            ACCESS_TOKEN = ACCESS_TOKEN.replace("SECRET", urlEnodeUTF8(WeiXinConfig.APP_SECRET));
            ACCESS_TOKEN = ACCESS_TOKEN.replace("CODE", code);
            JSONObject json = HttpUtil.doGetStr(ACCESS_TOKEN);
            if (json != null && json.getString("access_token") != null) {
                token = new WebPageAccessToken();
                token.setAccess_token(json.getString("access_token"));
                token.setOpenid(json.getString("openid"));
                //CacheManager.instance().pageTokenMap.put(code, token);
            } 
//else {
//                Logger.err("获取网页access_token失败");
//            }
       // }
        return token;
    }
    //第三步：刷新access_token（如果需要）
    //第四步：拉取用户信息(需scope为 snsapi_userinfo) 请求方式：GET

    public static String urlEnodeUTF8(String str) {
        String result = str;
        try {
            result = URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
