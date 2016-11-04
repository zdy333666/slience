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
public class SameCaseRelationDetail implements RelationDetail {

    private String caseNo;

    public SameCaseRelationDetail() {

    }

    public SameCaseRelationDetail(String caseNo) {
        this.caseNo = caseNo;
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

}
