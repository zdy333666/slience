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
public class SameCasePartyRelationDetail implements RelationDetail {

    private String caseNo;
    private String detail;

    public SameCasePartyRelationDetail() {

    }

    public SameCasePartyRelationDetail(String caseNo, String detail) {
        this.caseNo = caseNo;
        this.detail = detail;
    }

    /**
     * @return the caseNo
     */
    public String getCaseNo() {
        return caseNo;
    }

    /**
     * @param caseNo the caseNo to set
     */
    public void setCaseNo(String caseNo) {
        this.caseNo = caseNo;
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
