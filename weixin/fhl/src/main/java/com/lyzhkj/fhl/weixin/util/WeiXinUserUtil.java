/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.weixin.util;

import com.lyzhkj.fhl.conf.WeiXinConfig;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import com.lyzhkj.weixin.common.pojo.WeiXinUserBaseInfo;
//simport com.lyzhkj.weixin.common.pojo.WeiXinUserBaseInfo;
import java.net.URLEncoder;
import java.util.Random;
import net.sf.json.JSONObject;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author breeze
 */
public class WeiXinUserUtil {

//    public static String AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
//    //public static String ACCESS_TOKEN= "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
//    public static String REFRESH_TOKEN = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
//    public static String USERINFO = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
//    public static String CHECK = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";
    /**
     * 第一步：用户同意授权，获取code
     *
     * @param url
     * @return
     */
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

    /**
     * 第二步：通过code换取网页授权access_token
     *
     * @param code
     * @return
     */
    public static WebPageAccessToken getWebPageAccessToken(String code) {
        WebPageAccessToken token = null;

        String url = WeiXinConfig.WEB_ACCESS_TOKEN_URL;
        url = url.replace("APPID", urlEnodeUTF8(WeiXinConfig.APP_ID));
        url = url.replace("SECRET", urlEnodeUTF8(WeiXinConfig.APP_SECRET));
        url = url.replace("CODE", code);

        JSONObject json = HttpUtil.doGetStr(url);
        if (json != null && json.getString("access_token") != null) {
            token = new WebPageAccessToken();
            token.setAccess_token(json.getString("access_token"));
            token.setOpenid(json.getString("openid"));
        }

        return token;
    }
    //第三步：刷新access_token（如果需要）
    //第四步：拉取用户信息(需scope为 snsapi_userinfo) 请求方式：GET

    /**
     * 获取微信用户基本信息
     *
     * @param accessToken
     * @param openId
     * @return
     */
    public static WeiXinUserBaseInfo getWeiXinUserBaseInfo(String openId) {
        AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
        String url = WeiXinConfig.USER_INFO_URL.replace("ACCESS_TOKEN", token.getAccessToken()).replace("OPENID", openId);
        WeiXinUserBaseInfo result = new RestTemplate().getForObject(url, WeiXinUserBaseInfo.class);

        return result;
    }

    /**
     * 获取微信用户基本信息
     *
     * @param accessToken
     * @param openId
     * @return
     */
    public static WeiXinUserBaseInfo getWeiXinUserBaseInfo(String accessToken, String openId) {

        String url = WeiXinConfig.USER_INFO_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);

        WeiXinUserBaseInfo result = new RestTemplate().getForObject(url, WeiXinUserBaseInfo.class);

        return result;
    }

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
