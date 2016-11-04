/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.service;

/**
 *
 * @author xzh
 */
public class SearchModelDefine {
    private String  modelName;
    private String  modelDisplayName;
    private String  modelViewName;
    private String  searchUrl;
    private int modelDefId;
    private int selectedDefault;
    private String searchSort;
    /**
     * @return the modelName
     */
    public String getModelName() {
        return modelName;
    }

    /**
     * @param modelName the modelName to set
     */
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    /**
     * @return the modelDisplayName
     */
    public String getModelDisplayName() {
        return modelDisplayName;
    }

    /**
     * @param modelDisplayName the modelDisplayName to set
     */
    public void setModelDisplayName(String modelDisplayName) {
        this.modelDisplayName = modelDisplayName;
    }

    /**
     * @return the modelViewName
     */
    public String getModelViewName() {
        return modelViewName;
    }

    /**
     * @param modelViewName the modelViewName to set
     */
    public void setModelViewName(String modelViewName) {
        this.modelViewName = modelViewName;
    }

    /**
     * @return the searchUrl
     */
    public String getSearchUrl() {
        return searchUrl;
    }

    /**
     * @param searchUrl the searchUrl to set
     */
    public void setSearchUrl(String searchUrl) {
        this.searchUrl = searchUrl;
    }

    /**
     * @return the model_def_id
     */
    public int getModelDefId() {
        return modelDefId;
    }

    /**
     * @param model_def_id the model_def_id to set
     */
    public void setModelDefId(int modelDefId) {
        this.modelDefId = modelDefId;
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
     * @return the searchSort
     */
    public String getSearchSort() {
        return searchSort;
    }

    /**
     * @param searchSort the searchSort to set
     */
    public void setSearchSort(String searchSort) {
        this.searchSort = searchSort;
    }
}
