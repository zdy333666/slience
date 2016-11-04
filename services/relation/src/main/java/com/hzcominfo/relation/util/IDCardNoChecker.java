/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.util;

/**
 *
 * @author cominfo4
 */
public class IDCardNoChecker {

    /**
     *
     * @param sfzh
     * @return
     */
    public static boolean check(String sfzh) {
        if (sfzh == null || sfzh.trim().isEmpty() || (sfzh.length() != 18) || sfzh.startsWith("00")) {
            return false;
        } else {
            char c = ' ';
            for (byte i = 0; i < 17; i++) {
                c = sfzh.charAt(i);
                if (c < '0' || c > '9') {
                    return false;
                }
            }
        }

        return true;
    }

}
