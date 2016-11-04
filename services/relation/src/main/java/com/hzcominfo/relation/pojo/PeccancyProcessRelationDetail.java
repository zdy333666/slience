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
public class PeccancyProcessRelationDetail implements RelationDetail {

    private String time;
    private String cphm;
    private String hpzl;
    private String detail;

    public PeccancyProcessRelationDetail() {

    }

    public PeccancyProcessRelationDetail(String time, String cphm, String hpzl, String detail) {
        this.time = time;
        this.cphm = cphm;
        this.hpzl = hpzl;
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
     * @return the cphm
     */
    public String getCphm() {
        return cphm;
    }

    /**
     * @param cphm the cphm to set
     */
    public void setCphm(String cphm) {
        this.cphm = cphm;
    }

    /**
     * @return the hpzl
     */
    public String getHpzl() {
        return hpzl;
    }

    /**
     * @param hpzl the hpzl to set
     */
    public void setHpzl(String hpzl) {
        this.hpzl = hpzl;
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
