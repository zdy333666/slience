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
public class WeiXinMenuClickButton extends WeiXinMenuButton {

    //菜单KEY值
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public WeiXinMenuClickButton(String name, String type, String key) {
        super(name, type);
        this.key = key;
    }
}
