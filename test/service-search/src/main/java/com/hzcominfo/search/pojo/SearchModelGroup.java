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
public class SearchModelGroup {

    private long id;
    private String name;
    private String displayName;
    private String addComment;
    private int selectedDefault;

    private int useSpec;
    private int displayOrder;
    private int enable;

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

}
