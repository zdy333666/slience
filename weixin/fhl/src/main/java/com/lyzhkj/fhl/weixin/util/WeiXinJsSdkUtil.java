package com.lyzhkj.fhl.weixin.util;

import com.lyzhkj.fhl.cache.Cache;
import com.lyzhkj.fhl.conf.WeiXinConfig;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import java.io.IOException;
import net.sf.json.JSONObject;
import org.apache.http.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 微信服务器交互工具类
 *
 * @author wt
 *
 */
public class WeiXinJsSdkUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinJsSdkUtil.class);

    /**
     * 从微信服务器获得JsSdk ticket
     *
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static String getJsSdkTicket() {

        if (Cache.jsapi_ticket == null) {
            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
            Cache.jsapi_ticket = getJsSdkTicket(token.getAccessToken());
        }

        return Cache.jsapi_ticket;
    }

    /**
     * 从微信服务器获得JsSdk ticket
     *
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static String getJsSdkTicket(String token) {

        String ticket = null;
        try {
            String url = WeiXinConfig.JSSDK_TICKET.replace("ACCESS_TOKEN", token);
            JSONObject jsonObject = HttpUtil.doGetStr(url);

            LOGGER.info("Get JsSdkTicket-->" + jsonObject);
            if (jsonObject != null && jsonObject.containsKey("ticket")) {
                ticket = jsonObject.getString("ticket");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ticket;
    }

}
