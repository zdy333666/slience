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
public class TargetNode {

    private String lgdm;
    private String lgmc;

    public TargetNode() {
    }

    public TargetNode(String p_lgdm, String p_lgmc) {
        this.lgdm = p_lgdm;
        this.lgmc = p_lgmc;
    }

    /**
     * @return the lgdm
     */
    public String getLgdm() {
        return lgdm;
    }

    /**
     * @param lgdm the lgdm to set
     */
    public void setLgdm(String lgdm) {
        this.lgdm = lgdm;
    }

    /**
     * @return the lgmc
     */
    public String getLgmc() {
        return lgmc;
    }

    /**
     * @param lgmc the lgmc to set
     */
    public void setLgmc(String lgmc) {
        this.lgmc = lgmc;
    }

}
