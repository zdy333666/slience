/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.pojo;

/**
 *
 * @author breeze
 */
public class GarUser {

    private String userId;
    private String userName;
    private String mobilephone;
    private String userType;
    private String openId;

    private int intCurrency;//可用积分
    private int intSpend;//消费积分
    private int intExpired;//过期积分
    private int intTotal;//总计积分
    private int throwExchangeInt;//投放积分
    private int recycleExchangeInt;//可回收积分
    private int pickExchangeInt;//巡检积分
    private int activityExchangeInt;//活动积分
    private String city;

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the mobilephone
     */
    public String getMobilephone() {
        return mobilephone;
    }

    /**
     * @param mobilephone the mobilephone to set
     */
    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    /**
     * @return the userType
     */
    public String getUserType() {
        return userType;
    }

    /**
     * @param userType the userType to set
     */
    public void setUserType(String userType) {
        this.userType = userType;
    }

    /**
     * @return the intCurrency
     */
    public int getIntCurrency() {
        return intCurrency;
    }

    /**
     * @param intCurrency the intCurrency to set
     */
    public void setIntCurrency(int intCurrency) {
        this.intCurrency = intCurrency;
    }

    /**
     * @return the intSpend
     */
    public int getIntSpend() {
        return intSpend;
    }

    /**
     * @param intSpend the intSpend to set
     */
    public void setIntSpend(int intSpend) {
        this.intSpend = intSpend;
    }

    /**
     * @return the intExpired
     */
    public int getIntExpired() {
        return intExpired;
    }

    /**
     * @param intExpired the intExpired to set
     */
    public void setIntExpired(int intExpired) {
        this.intExpired = intExpired;
    }

    /**
     * @return the intTotal
     */
    public int getIntTotal() {
        return intTotal;
    }

    /**
     * @param intTotal the intTotal to set
     */
    public void setIntTotal(int intTotal) {
        this.intTotal = intTotal;
    }

    /**
     * @return the throwExchangeInt
     */
    public int getThrowExchangeInt() {
        return throwExchangeInt;
    }

    /**
     * @param throwExchangeInt the throwExchangeInt to set
     */
    public void setThrowExchangeInt(int throwExchangeInt) {
        this.throwExchangeInt = throwExchangeInt;
    }

    /**
     * @return the recycleExchangeInt
     */
    public int getRecycleExchangeInt() {
        return recycleExchangeInt;
    }

    /**
     * @param recycleExchangeInt the recycleExchangeInt to set
     */
    public void setRecycleExchangeInt(int recycleExchangeInt) {
        this.recycleExchangeInt = recycleExchangeInt;
    }

    /**
     * @return the pickExchangeInt
     */
    public int getPickExchangeInt() {
        return pickExchangeInt;
    }

    /**
     * @param pickExchangeInt the pickExchangeInt to set
     */
    public void setPickExchangeInt(int pickExchangeInt) {
        this.pickExchangeInt = pickExchangeInt;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the openId
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId the openId to set
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * @return the activityExchangeInt
     */
    public int getActivityExchangeInt() {
        return activityExchangeInt;
    }

    /**
     * @param activityExchangeInt the activityExchangeInt to set
     */
    public void setActivityExchangeInt(int activityExchangeInt) {
        this.activityExchangeInt = activityExchangeInt;
    }

}
