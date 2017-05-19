/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.weixin.common.pojo;

/**
 *
 * @author breeze
 */
public class WeiXinKFMessage {

    private String touser;
    private String msgtype;

    /**
     * @return the touser
     */
    public String getTouser() {
        return touser;
    }

    /**
     * @param touser the touser to set
     */
    public void setTouser(String touser) {
        this.touser = touser;
    }

    /**
     * @return the msgtype
     */
    public String getMsgtype() {
        return msgtype;
    }

    /**
     * @param msgtype the msgtype to set
     */
    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }
}
