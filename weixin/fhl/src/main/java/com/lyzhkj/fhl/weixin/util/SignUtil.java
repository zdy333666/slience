package com.lyzhkj.fhl.weixin.util;

import com.lyzhkj.fhl.conf.WeiXinConfig;
import java.util.UUID;
import java.util.Formatter;
import java.security.MessageDigest;

/**
 * 微信端签名
 *
 * @author wt
 *
 */
public class SignUtil {

    /**
     * 返回结果json
     *
     * @param url
     * @return
     */
    public static String sign(String url) {

        String res = null;

        try {
            String jsapi_ticket = WeiXinJsSdkUtil.getJsSdkTicket();
            String nonce_str = create_nonce_str();
            String timestamp = create_timestamp();
            String appId = WeiXinConfig.APP_ID;

            //签名计算
            String strSha = "jsapi_ticket=" + jsapi_ticket
                    + "&noncestr=" + nonce_str
                    + "&timestamp=" + timestamp
                    + "&url=" + url;

            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(strSha.getBytes("UTF-8"));
            String sign = byteToHex(crypt.digest());

            //结果返回
            StringBuilder s = new StringBuilder();
            s.append("{");
            s.append("\"appId\" : \"" + appId + "\",");
            s.append("\"timestamp\" : \"" + timestamp + "\",");
            s.append("\"nonceStr\" : \"" + nonce_str + "\",");
            s.append("\"signature\" : \"" + sign + "\"");
            s.append("}");
            res = s.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String signMd5(String nonce) {

        String res = null;

        try {
            //签名计算
            String strSha = nonce + "&lyzh&ljff&jsyfzx";

            MessageDigest crypt = MessageDigest.getInstance("MD5");
            crypt.reset();
            crypt.update(strSha.getBytes("UTF-8"));
            res = byteToHex(crypt.digest());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}
