/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.pojo;

/**
 * 新商户
 *
 * @author breeze
 */
public class GarMerchants {

    // KEY
    private String munitid;
    // 累计兑换积分
    private String name;
    // 累计兑换积分
    private int exchangeInt;
    // 累计提现积分
    private int withdrawInt;
    // 可提现积分
    private int hasWithdrawInt;

    /**
     * @return the munitid
     */
    public String getMunitid() {
        return munitid;
    }

    /**
     * @param munitid the munitid to set
     */
    public void setMunitid(String munitid) {
        this.munitid = munitid;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the exchangeInt
     */
    public int getExchangeInt() {
        return exchangeInt;
    }

    /**
     * @param exchangeInt the exchangeInt to set
     */
    public void setExchangeInt(int exchangeInt) {
        this.exchangeInt = exchangeInt;
    }

    /**
     * @return the withdrawInt
     */
    public int getWithdrawInt() {
        return withdrawInt;
    }

    /**
     * @param withdrawInt the withdrawInt to set
     */
    public void setWithdrawInt(int withdrawInt) {
        this.withdrawInt = withdrawInt;
    }

    /**
     * @return the hasWithdrawInt
     */
    public int getHasWithdrawInt() {
        return hasWithdrawInt;
    }

    /**
     * @param hasWithdrawInt the hasWithdrawInt to set
     */
    public void setHasWithdrawInt(int hasWithdrawInt) {
        this.hasWithdrawInt = hasWithdrawInt;
    }
}
