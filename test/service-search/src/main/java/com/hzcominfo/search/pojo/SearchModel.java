/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.pojo;

/**
 *
 * @author cominfo4
 */
public class SearchModel {

    private long groupId;

    private long id;
    private String name;
    private String displayName;
    private String viewName;

    private String searchUrl;
    private String searchSort;
    private int selectedDefault;
    private int useSpec;
    private int displayOrder;
    private String addComment;
    private int enable;

    /**
     * @return the groupId
     */
    public long getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(long groupId) {
        this.groupId = groupId;
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
     * @return the viewName
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * @param viewName the viewName to set
     */
    public void setViewName(String viewName) {
        this.viewName = viewName;
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
     * @return the useSpec
     */
    public int getUseSpec() {
        return useSpec;
    }

    /**
     * @param useSpec the useSpec to set
     */
    public void setUseSpec(int useSpec) {
        this.useSpec = useSpec;
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
     * @return the addComment
     */
    public String getAddComment() {
        return addComment;
    }

    /**
     * @param addComment the addComment to set
     */
    public void setAddComment(String addComment) {
        this.addComment = addComment;
    }

    /**
     * @return the enable
     */
    public int getEnable() {
        return enable;
    }

    /**
     * @param enable the enable to set
     */
    public void setEnable(int enable) {
        this.enable = enable;
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
