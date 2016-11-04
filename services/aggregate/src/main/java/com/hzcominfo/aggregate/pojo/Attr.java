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
public class Attr {

    //private long attrSetId;
    private long id;
    private String name;
    private String displayName;
    private int itemType;
    private long ownerId;

    private String collName;
    private String collAlias;
    private String collFieldName;

    private int paramFlag;
    private int distinctFlag;
    private int displayFlag;
    private int sortFlag;
    private int fieldOrder;

    private int fieldSpec;
    //private int displayWidth;
    //private String displaySourceSystem;
    //private String relatedCollName;
    //private String relatedFieldName;
    /**
     * @return the attrSetId
     */
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
     * @return the itemType
     */
    public int getItemType() {
        return itemType;
    }

    /**
     * @param itemType the itemType to set
     */
    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    /**
     * @return the ownerId
     */
    public long getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId the ownerId to set
     */
    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * @return the collName
     */
    public String getCollName() {
        return collName;
    }

    /**
     * @param collName the collName to set
     */
    public void setCollName(String collName) {
        this.collName = collName;
    }

    /**
     * @return the collAlias
     */
    public String getCollAlias() {
        return collAlias;
    }

    /**
     * @param collAlias the collAlias to set
     */
    public void setCollAlias(String collAlias) {
        this.collAlias = collAlias;
    }

    /**
     * @return the collFieldName
     */
    public String getCollFieldName() {
        return collFieldName;
    }

    /**
     * @param collFieldName the collFieldName to set
     */
    public void setCollFieldName(String collFieldName) {
        this.collFieldName = collFieldName;
    }

    /**
     * @return the paramFlag
     */
    public int getParamFlag() {
        return paramFlag;
    }

    /**
     * @param paramFlag the paramFlag to set
     */
    public void setParamFlag(int paramFlag) {
        this.paramFlag = paramFlag;
    }

    /**
     * @return the distinctFlag
     */
    public int getDistinctFlag() {
        return distinctFlag;
    }

    /**
     * @param distinctFlag the distinctFlag to set
     */
    public void setDistinctFlag(int distinctFlag) {
        this.distinctFlag = distinctFlag;
    }

    /**
     * @return the displayFlag
     */
    public int getDisplayFlag() {
        return displayFlag;
    }

    /**
     * @param displayFlag the displayFlag to set
     */
    public void setDisplayFlag(int displayFlag) {
        this.displayFlag = displayFlag;
    }

    /**
     * @return the sortFlag
     */
    public int getSortFlag() {
        return sortFlag;
    }

    /**
     * @param sortFlag the sortFlag to set
     */
    public void setSortFlag(int sortFlag) {
        this.sortFlag = sortFlag;
    }

    /**
     * @return the fieldOrder
     */
    public int getFieldOrder() {
        return fieldOrder;
    }

    /**
     * @param fieldOrder the fieldOrder to set
     */
    public void setFieldOrder(int fieldOrder) {
        this.fieldOrder = fieldOrder;
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
}
