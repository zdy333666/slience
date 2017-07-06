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
public class GarbagPick {

    // 垃圾分拣记录表
    private String qrCode; 	// 二维码(垃圾袋/居民二维码)
    private String garType; 	// 类型
    private Date pickTime; 	// 分拣时间
    private int intPick; 	// 分拣积分
    private String pickerId; 	// 分拣员
    private String hostName;
    private String pickTimeWithHHMMSS;

    /**
     * @return the qrCode
     */
    public String getQrCode() {
        return qrCode;
    }

    /**
     * @param qrCode the qrCode to set
     */
    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    /**
     * @return the garType
     */
    public String getGarType() {
        return garType;
    }

    /**
     * @param garType the garType to set
     */
    public void setGarType(String garType) {
        this.garType = garType;
    }

    /**
     * @return the pickTime
     */
    public Date getPickTime() {
        return pickTime;
    }

    /**
     * @param pickTime the pickTime to set
     */
    public void setPickTime(Date pickTime) {
        this.pickTime = pickTime;
    }

    /**
     * @return the intPick
     */
    public int getIntPick() {
        return intPick;
    }

    /**
     * @param intPick the intPick to set
     */
    public void setIntPick(int intPick) {
        this.intPick = intPick;
    }

    /**
     * @return the pickerId
     */
    public String getPickerId() {
        return pickerId;
    }

    /**
     * @param pickerId the pickerId to set
     */
    public void setPickerId(String pickerId) {
        this.pickerId = pickerId;
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
     * @return the pickTimeWithHHMMSS
     */
    public String getPickTimeWithHHMMSS() {
        return pickTimeWithHHMMSS;
    }

    /**
     * @param pickTimeWithHHMMSS the pickTimeWithHHMMSS to set
     */
    public void setPickTimeWithHHMMSS(String pickTimeWithHHMMSS) {
        this.pickTimeWithHHMMSS = pickTimeWithHHMMSS;
    }

}
