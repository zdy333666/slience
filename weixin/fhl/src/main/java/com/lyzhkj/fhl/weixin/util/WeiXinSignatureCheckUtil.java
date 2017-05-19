/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.weixin.util;

import com.lyzhkj.fhl.conf.WeiXinConfig;
import java.security.MessageDigest;
import java.util.Arrays;

/**
 *
 * @author breeze
 */
public class WeiXinSignatureCheckUtil {

    public static boolean checkSignature(String signature, String timestamp, String nonce) {

        if (signature == null || timestamp == null || nonce == null) {
            return false;
        }

        //step1：将开发者填写的token、timestamp、nonce三个参数进行字典序排序
        String[] arr = new String[]{WeiXinConfig.TOKEN, timestamp, nonce};
        Arrays.sort(arr);

        //step2：将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuilder content = new StringBuilder();
        for (String str : arr) {
            content.append(str);
        }

        //step3：开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        String temp = getSha1(content.toString());
        return temp.equals(signature);
    }

    /**
     * Sha1数字签名
     *
     * @param str
     * @return
     */
    public static String getSha1(String str) {
        String rtn = null;
        if (str == null || str.isEmpty()) {
            return rtn;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes("UTF-8"));

            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            rtn = new String(buf);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;
    }

}
