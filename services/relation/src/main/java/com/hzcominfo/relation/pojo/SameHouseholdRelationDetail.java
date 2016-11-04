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
public class SameHouseholdRelationDetail implements RelationDetail {

    private String hhid;

    public SameHouseholdRelationDetail() {

    }

    public SameHouseholdRelationDetail(String hhid) {
        this.hhid = hhid;
    }

    /**
     * @return the hhid
     */
    public String getHhid() {
        return hhid;
    }

    /**
     * @param hhid the hhid to set
     */
    public void setHhid(String hhid) {
        this.hhid = hhid;
    }

}
