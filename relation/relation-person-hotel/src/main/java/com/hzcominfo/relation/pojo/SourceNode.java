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
public class SourceNode {

    private String sfzh;
    private String xm;

    public SourceNode() {
    }

    public SourceNode(String p_sfzh, String p_xm) {
        this.sfzh = p_sfzh;
        this.xm = p_xm;
    }

    /**
     * @return the sfzh
     */
    public String getSfzh() {
        return sfzh;
    }

    /**
     * @param sfzh the sfzh to set
     */
    public void setSfzh(String sfzh) {
        this.sfzh = sfzh;
    }

    /**
     * @return the xm
     */
    public String getXm() {
        return xm;
    }

    /**
     * @param xm the xm to set
     */
    public void setXm(String xm) {
        this.xm = xm;
    }

}
