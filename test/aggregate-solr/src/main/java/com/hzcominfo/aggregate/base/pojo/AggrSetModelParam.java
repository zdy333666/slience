/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.pojo;

/**
 *
 * @author zdy
 */
public class AggrSetModelParam {

    private String setParam;
    private long modelId;
    private String modelParam;

    /**
     * @return the setParam
     */
    public String getSetParam() {
        return setParam;
    }

    /**
     * @param setParam the setParam to set
     */
    public void setSetParam(String setParam) {
        this.setParam = setParam;
    }

    /**
     * @return the modelId
     */
    public long getModelId() {
        return modelId;
    }

    /**
     * @param modelId the modelId to set
     */
    public void setModelId(long modelId) {
        this.modelId = modelId;
    }

    /**
     * @return the modelParam
     */
    public String getModelParam() {
        return modelParam;
    }

    /**
     * @param modelParam the modelParam to set
     */
    public void setModelParam(String modelParam) {
        this.modelParam = modelParam;
    }

}
