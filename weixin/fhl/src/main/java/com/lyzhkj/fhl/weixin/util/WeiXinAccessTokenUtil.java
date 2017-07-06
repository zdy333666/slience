/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.weixin.util;

import com.lyzhkj.fhl.cache.Cache;
import com.lyzhkj.fhl.conf.WeiXinConfig;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author breeze
 */
public class WeiXinAccessTokenUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinAccessTokenUtil.class);

    public static AccessToken getAccessToken() {
        //return new RestTemplate().getForObject(FHLConfig.WEIXIN_TOKEN_SERVER, AccessToken.class);
        if (Cache.accessToken == null) {
            Cache.accessToken = getAccessToken(WeiXinConfig.ACCESS_TOKEN_URL, WeiXinConfig.APP_ID, WeiXinConfig.APP_SECRET);
        }
        return Cache.accessToken;
    }

    /**
     * 从微信服务器获得令牌(有效时间2H)
     *
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static AccessToken getAccessToken(String url, String appId, String appSecret) {
        AccessToken accessToken = null;

        url = url.replace("APPID", appId).replace("APPSECRET", appSecret);

        Map<String, Object> result = new RestTemplate().getForObject(url, Map.class);

        LOGGER.info("Get AccessToken-->" + result);

        if (result == null || !result.containsKey("access_token")) {
            return accessToken;
        }

        return new AccessToken((String) result.get("access_token"), (int) result.get("expires_in"));
    }

}
