/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.pojo;

import java.util.Date;

/**
 *
 * @author breeze
 */
public class GarStoreOrder {

// 业主id
    private String citizenId;
    // 兑换积分
    private int exchangeInt;
    // 兑换时间
    private Date exchangetime;

    /**
     * @return the citizenId
     */
    public String getCitizenId() {
        return citizenId;
    }

    /**
     * @param citizenId the citizenId to set
     */
    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
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
     * @return the exchangetime
     */
    public Date getExchangetime() {
        return exchangetime;
    }

    /**
     * @param exchangetime the exchangetime to set
     */
    public void setExchangetime(Date exchangetime) {
        this.exchangetime = exchangetime;
    }

}
