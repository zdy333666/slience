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
public class AggrModelField {

    private long id;
    private String name;
    private String alias;
    private String displayName;
    private int displayEnable;
    private int distinctEnable;
    private int sortType;
    private int fieldSpec;

    private String sourceField;

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
     * @return the alias
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias the alias to set
     */
    public void setAlias(String alias) {
        this.alias = alias;
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
     * @return the displayEnable
     */
    public int getDisplayEnable() {
        return displayEnable;
    }

    /**
     * @param displayEnable the displayEnable to set
     */
    public void setDisplayEnable(int displayEnable) {
        this.displayEnable = displayEnable;
    }

    /**
     * @return the sortType
     */
    public int getSortType() {
        return sortType;
    }

    /**
     * @param sortType the sortType to set
     */
    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    /**
     * @return the distinctEnable
     */
    public int getDistinctEnable() {
        return distinctEnable;
    }

    /**
     * @param distinctEnable the distinctEnable to set
     */
    public void setDistinctEnable(int distinctEnable) {
        this.distinctEnable = distinctEnable;
    }

    /**
     * @return the fieldSpec
     */
    public int getFieldSpec() {
        return fieldSpec;
    }

    /**
     * @param fieldSpec the fieldSpec to set
     */
    public void setFieldSpec(int fieldSpec) {
        this.fieldSpec = fieldSpec;
    }

    /**
     * @return the sourceField
     */
    public String getSourceField() {
        return sourceField;
    }

    /**
     * @param sourceField the sourceField to set
     */
    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

}
