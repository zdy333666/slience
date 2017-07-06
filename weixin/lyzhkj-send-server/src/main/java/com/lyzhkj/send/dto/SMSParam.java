/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.send.dto;

/**
 *
 * @author breeze
 */
public class SMSParam {

    private String telNo;
    private String sms;

    /**
     * @return the telNo
     */
    public String getTelNo() {
        return telNo;
    }

    /**
     * @param telNo the telNo to set
     */
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    /**
     * @return the sms
     */
    public String getSms() {
        return sms;
    }

    /**
     * @param sms the sms to set
     */
    public void setSms(String sms) {
        this.sms = sms;
    }
}
