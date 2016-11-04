/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.service;

/**
 *
 * @author xzh
 */
public class BulkResultItem {
    private String busiModel;
    private long totalNum;
    private int modelIndex;

    /**
     * @return the busiModel
     */
    public String getBusiModel() {
        return busiModel;
    }

    /**
     * @param busiModel the busiModel to set
     */
    public void setBusiModel(String busiModel) {
        this.busiModel = busiModel;
    }

    /**
     * @return the totalNum
     */
    public long getTotalNum() {
        return totalNum;
    }

    /**
     * @param totalNum the totalNum to set
     */
    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }

    /**
     * @return the modelIndex
     */
    public int getModelIndex() {
        return modelIndex;
    }

    /**
     * @param modelIndex the modelIndex to set
     */
    public void setModelIndex(int modelIndex) {
        this.modelIndex = modelIndex;
    }
}
