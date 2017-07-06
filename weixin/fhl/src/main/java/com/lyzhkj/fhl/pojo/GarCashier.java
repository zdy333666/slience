/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.pojo;

/**
 * 收银员 商户信息
 *
 * @author breeze
 */
public class GarCashier {

    private String userId;
    private String mobilephone;
    private String userName;
    private String openId;
    // 管理单位
    private String munitid;
    // 管理单位
    private String munit;
    // 用户类型
    private String userType;
    // 商户编号
    private String merchantsNum;
    // 是否有效
    private String valid;
    // 商户所属名
    private String merchantsName;

    private String toJson() {
        //结果返回
        StringBuilder s = new StringBuilder();
        s.append("{");
        s.append("\"pickerId\" : \"" + getUserId() + "\",");
        s.append("\"pickerName\" : \"" + getUserName() + "\"");
        s.append("}");
        return s.toString();
    }

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
     * @return the merchantsNum
     */
    public String getMerchantsNum() {
        return merchantsNum;
    }

    /**
     * @param merchantsNum the merchantsNum to set
     */
    public void setMerchantsNum(String merchantsNum) {
        this.merchantsNum = merchantsNum;
    }

    /**
     * @return the valid
     */
    public String getValid() {
        return valid;
    }

    /**
     * @param valid the valid to set
     */
    public void setValid(String valid) {
        this.valid = valid;
    }

    /**
     * @return the merchantsName
     */
    public String getMerchantsName() {
        return merchantsName;
    }

    /**
     * @param merchantsName the merchantsName to set
     */
    public void setMerchantsName(String merchantsName) {
        this.merchantsName = merchantsName;
    }
}
