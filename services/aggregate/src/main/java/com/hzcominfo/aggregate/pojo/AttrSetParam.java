/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.pojo;

/**
 *
 * @author zdy
 */
public class AttrSetParam {

    //private int dimensionId;
    //private String dimensionName;
    private String dimensionParam;
    private long attrSetId;
    //private String attrSetName;
    private String attrSetParam;
    //private int displayOrder;
    //private String addComment;

    /**
     * @return the dimensionParam
     */
    public String getDimensionParam() {
        return dimensionParam;
    }

    /**
     * @param dimensionParam the dimensionParam to set
     */
    public void setDimensionParam(String dimensionParam) {
        this.dimensionParam = dimensionParam;
    }

    /**
     * @return the attrSetId
     */
    public long getAttrSetId() {
        return attrSetId;
    }

    /**
     * @param attrSetId the attrSetId to set
     */
    public void setAttrSetId(long attrSetId) {
        this.attrSetId = attrSetId;
    }

    /**
     * @return the attrSetParam
     */
    public String getAttrSetParam() {
        return attrSetParam;
    }

    /**
     * @param attrSetParam the attrSetParam to set
     */
    public void setAttrSetParam(String attrSetParam) {
        this.attrSetParam = attrSetParam;
    }

}
