/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.pojo;

/**
 *
 * @author cominfo4
 */
public class AggrModel {

    private long setId;
    private long id;
    private String name;
    private String displayName;

    private String searchModel;
    private String searchUrl;

    /**
     * @return the setId
     */
    public long getSetId() {
        return setId;
    }

    /**
     * @param setId the setId to set
     */
    public void setSetId(long setId) {
        this.setId = setId;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * @return the searchModel
     */
    public String getSearchModel() {
        return searchModel;
    }

    /**
     * @param searchModel the searchModel to set
     */
    public void setSearchModel(String searchModel) {
        this.searchModel = searchModel;
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

}
