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
public class WeiXinMenuButton {

    //菜单的响应动作类型
    private String type;
    //菜单标题
    private String name;
    //一级菜单数组，个数应为1~3个
    //二级菜单数组，个数应为1~5个
    private WeiXinMenuButton[] sub_button;

    public WeiXinMenuButton(String name) {
        this.name = name;
    }

    public WeiXinMenuButton(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the sub_button
     */
    public WeiXinMenuButton[] getSub_button() {
        return sub_button;
    }

    /**
     * @param sub_button the sub_button to set
     */
    public void setSub_button(WeiXinMenuButton[] sub_button) {
        this.sub_button = sub_button;
    }

}
