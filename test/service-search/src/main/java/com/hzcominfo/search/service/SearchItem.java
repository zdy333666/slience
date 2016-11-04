/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.service;

/**
 *
 * @author xzh
 */
public class SearchItem {
    private String busiModel;
    private String q;
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
     * @return the q
     */
    public String getQ() {
        return q;
    }

    /**
     * @param q the q to set
     */
    public void setQ(String q) {
        this.q = q;
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
