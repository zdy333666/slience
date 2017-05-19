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
public class WeiXinKFTextMessage extends WeiXinKFMessage{
    private WeiXinText text;

    /**
     * @return the text
     */
    public WeiXinText getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(WeiXinText text) {
        this.text = text;
    }
}
