/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 *
 * @author breeze
 */
public class MD5Factory {

    protected static String md5plus = "lyzh#ljff#jsyfzx";

    // 全局数组
    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5",
        "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public MD5Factory() {
    }

    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }
//
//    // 返回形式只为数字
//    private static String byteToNum(byte bByte) {
//        int iRet = bByte;
//        System.out.println("iRet1=" + iRet);
//        if (iRet < 0) {
//            iRet += 256;
//        }
//        return String.valueOf(iRet);
//    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static String GetMD5Code(String strObj) {
        String resultString = null;
        try {
            resultString = new String(strObj);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(strObj.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return resultString;
    }

    public static String RandomString() {
        int length = 16;
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < length; i++) {
            int num = random.nextInt(62);
            buf.append(str.charAt(num));
        }

        return buf.toString();
    }

    /**
     * 获得随机子
     *
     * @return
     */
    public static String getNonce() {
        String nonce = RandomString();
        return nonce;
    }

    /**
     * 获得签名结果
     *
     * @param nonce
     * @return
     */
    public static String getSign(String nonce) {
        String sign = MD5Factory.GetMD5Code(nonce + md5plus);
        return sign;
    }
}
