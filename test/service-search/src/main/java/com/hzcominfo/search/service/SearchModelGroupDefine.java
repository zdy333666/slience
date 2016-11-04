/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.service;

import com.hzcominfo.search.result.ModelGroupKeyMap;
import java.util.ArrayList;

/**
 *
 * @author xzh
 */
public class SearchModelGroupDefine {
    private String  modelGroupName;
    private String  modelGroupDisplayName;
    private String modelGroupDefId;
    private int selectedDefault;
    private int displayOrder;
    private ArrayList<SearchModelDefine> modelList;
    private ArrayList<ModelGroupKeyMap> keyList;

    /**
     * @return the modelGroupName
     */
    public String getModelName() {
        return modelGroupName;
    }

    /**
     * @param modelGroupName the modelGroupName to set
     */
    public void setModelName(String modelName) {
        this.modelGroupName = modelName;
    }

    /**
     * @return the modelGroupDisplayName
     */
    public String getModelGroupDisplayName() {
        return modelGroupDisplayName;
    }

    /**
     * @param modelGroupDisplayName the modelGroupDisplayName to set
     */
    public void setModelGroupDisplayName(String modelGroupDisplayName) {
        this.modelGroupDisplayName = modelGroupDisplayName;
    }

  

    /**
     * @return the selectedDefault
     */
    public int getSelectedDefault() {
        return selectedDefault;
    }

    /**
     * @param selectedDefault the selectedDefault to set
     */
    public void setSelectedDefault(int selectedDefault) {
        this.selectedDefault = selectedDefault;
    }

    /**
     * @return the displayOrder
     */
    public int getDisplayOrder() {
        return displayOrder;
    }

    /**
     * @param displayOrder the displayOrder to set
     */
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
     * @return the modelList
     */
    public ArrayList<SearchModelDefine> getModelList() {
        return modelList;
    }

    /**
     * @param modelList the modelList to set
     */
    public void setModelList(ArrayList<SearchModelDefine> modelList) {
        this.modelList = modelList;
    }

    /**
     * @return the keyList
     */
    public ArrayList<ModelGroupKeyMap> getKeyList() {
        return keyList;
    }

    /**
     * @param keyList the keyList to set
     */
    public void setKeyList(ArrayList<ModelGroupKeyMap> keyList) {
        this.keyList = keyList;
    }

    /**
     * @return the modelGroupDefId
     */
    public String getModelGroupDefId() {
        return modelGroupDefId;
    }

    /**
     * @param modelGroupDefId the modelGroupDefId to set
     */
    public void setModelGroupDefId(String modelGroupDefId) {
        this.modelGroupDefId = modelGroupDefId;
    }
    
}
