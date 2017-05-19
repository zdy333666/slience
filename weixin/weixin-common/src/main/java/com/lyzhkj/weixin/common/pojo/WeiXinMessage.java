/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.weixin.common.pojo;

/**
 * 微信服务器的推送信息
 *
 * @author breeze
 */
public class WeiXinMessage {

    private String FromUserName;
    private String ToUserName;
    private String MsgType;
    private long CreateTime;

    /**
     * @return the FromUserName
     */
    public String getFromUserName() {
        return FromUserName;
    }

    /**
     * @param FromUserName the FromUserName to set
     */
    public void setFromUserName(String FromUserName) {
        this.FromUserName = FromUserName;
    }

    /**
     * @return the ToUserName
     */
    public String getToUserName() {
        return ToUserName;
    }

    /**
     * @param ToUserName the ToUserName to set
     */
    public void setToUserName(String ToUserName) {
        this.ToUserName = ToUserName;
    }

    /**
     * @return the MsgType
     */
    public String getMsgType() {
        return MsgType;
    }

    /**
     * @param MsgType the MsgType to set
     */
    public void setMsgType(String MsgType) {
        this.MsgType = MsgType;
    }

    /**
     * @return the CreateTime
     */
    public long getCreateTime() {
        return CreateTime;
    }

    /**
     * @param CreateTime the CreateTime to set
     */
    public void setCreateTime(long CreateTime) {
        this.CreateTime = CreateTime;
    }
}
