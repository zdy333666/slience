/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.pojo;

/**
 *
 * @author cominfo4
 */
public class PhoneTransRelationDetail implements RelationDetail {

    private String time;
    private String detail;

    public PhoneTransRelationDetail() {

    }

    public PhoneTransRelationDetail(String time, String detail) {
        this.time = time;
        this.detail = detail;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

}
