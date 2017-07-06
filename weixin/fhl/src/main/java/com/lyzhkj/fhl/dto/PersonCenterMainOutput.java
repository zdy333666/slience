/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dto;

import com.lyzhkj.fhl.pojo.IntegralOverview;

/**
 *
 * @author breeze
 */
public class PersonCenterMainOutput {

    private String headPic;
    private String username;
    private IntegralOverview integral;
    private String qrPic;
    private String phoneno;
    private String role;

    /**
     * @return the headPic
     */
    public String getHeadPic() {
        return headPic;
    }

    /**
     * @param headPic the headPic to set
     */
    public void setHeadPic(String headPic) {
        this.headPic = headPic;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the integral
     */
    public IntegralOverview getIntegral() {
        return integral;
    }

    /**
     * @param integral the integral to set
     */
    public void setIntegral(IntegralOverview integral) {
        this.integral = integral;
    }

    /**
     * @return the qrPic
     */
    public String getQrPic() {
        return qrPic;
    }

    /**
     * @param qrPic the qrPic to set
     */
    public void setQrPic(String qrPic) {
        this.qrPic = qrPic;
    }

    /**
     * @return the phoneno
     */
    public String getPhoneno() {
        return phoneno;
    }

    /**
     * @param phoneno the phoneno to set
     */
    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

}
