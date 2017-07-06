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
public class GarCitizen {

    private String citizenId;	//编号，主键
    private String hostName;		//户主
    private String hostNo;		//户主
    private String mobilephone;	//手机号
    private String kitchenQR;	//二维码(厨余)
    private String otherQR;		//二维码(其他)
    private String openId;		//微信用户ID	
    private String weixinQR;		//我的二维码存根
    private String kitchenMid;	//二维码存根(厨余)
    private String otherMid;		//二维码存根(生活)
    private int intCurrency;	//可用积分
    private String munit;		//管理单位
    private String munitid;
    private String address;
    private String intExchangeType;	//可用积分相关
    private int throwExchangeInt;	//投放积分
    private int recycleExchangeInt;	//可回收积分
    private int pickExchangeInt;//巡检积分

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
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the hostNo
     */
    public String getHostNo() {
        return hostNo;
    }

    /**
     * @param hostNo the hostNo to set
     */
    public void setHostNo(String hostNo) {
        this.hostNo = hostNo;
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
     * @return the kitchenQR
     */
    public String getKitchenQR() {
        return kitchenQR;
    }

    /**
     * @param kitchenQR the kitchenQR to set
     */
    public void setKitchenQR(String kitchenQR) {
        this.kitchenQR = kitchenQR;
    }

    /**
     * @return the otherQR
     */
    public String getOtherQR() {
        return otherQR;
    }

    /**
     * @param otherQR the otherQR to set
     */
    public void setOtherQR(String otherQR) {
        this.otherQR = otherQR;
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
     * @return the weixinQR
     */
    public String getWeixinQR() {
        return weixinQR;
    }

    /**
     * @param weixinQR the weixinQR to set
     */
    public void setWeixinQR(String weixinQR) {
        this.weixinQR = weixinQR;
    }

    /**
     * @return the kitchenMid
     */
    public String getKitchenMid() {
        return kitchenMid;
    }

    /**
     * @param kitchenMid the kitchenMid to set
     */
    public void setKitchenMid(String kitchenMid) {
        this.kitchenMid = kitchenMid;
    }

    /**
     * @return the otherMid
     */
    public String getOtherMid() {
        return otherMid;
    }

    /**
     * @param otherMid the otherMid to set
     */
    public void setOtherMid(String otherMid) {
        this.otherMid = otherMid;
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
     * @return the munit
     */
    public String getMunit() {
        return munit;
    }

    /**
     * @param munit the munit to set
     */
    public void setMunit(String munit) {
        this.munit = munit;
    }

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
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the intExchangeType
     */
    public String getIntExchangeType() {
        return intExchangeType;
    }

    /**
     * @param intExchangeType the intExchangeType to set
     */
    public void setIntExchangeType(String intExchangeType) {
        this.intExchangeType = intExchangeType;
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

    //获得有效积分
    public int getEffectiveScore() {
        int count = 0;
        //1投放
        if (intExchangeType.contains("1")) {
            count += throwExchangeInt;
        }
        //2可回收
        if (intExchangeType.contains("2")) {
            count += recycleExchangeInt;
        }
        //3巡检积分
        if (intExchangeType.contains("3")) {
            count += pickExchangeInt;
        }
        return count;
    }

    //账户总积分
    public int getAllScore() {
        int count = 0;

        count += throwExchangeInt;
        count += recycleExchangeInt;
        count += pickExchangeInt;

        return count;
    }

}
