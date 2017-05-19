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
public class WeiXinMenu {

    //一级菜单
    private WeiXinMenuButton[] button;

    public WeiXinMenuButton[] getButton() {
        return button;
    }

    public void setButton(WeiXinMenuButton[] button) {
        this.button = button;
    }
}
