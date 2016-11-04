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
public class SearchModelField {

    private long modelId;

    private long id;
    private String name;
    private String displayName;
    private String sourceField;
    private String[] sourceFields;

    private int displayInGrid;
    private int displayInCollision;
    private int fieldOrder;
    private int fieldSpec;
    private int wildMatch;
    private int addressFlag;

    private int listDisplayOrder;
    private int largeIconDisplayOrder;
    private int latticeNum;
    private int rowNum;
    private int pkField;
    private int displayFieldNameFlag;
    private int statisticsFilterField;
    private String statisticsFilterCode1;
    private String statisticsFilterCode2;
    private String statisticsFilterCode3;
    private int statisticsFilterFieldOrder;
    private int listFieldFlag;
    private int largeIconFieldFlag;
    private int polymerizerFlag;
    private int detailFlag;

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

    /**
     * @return the displayInGrid
     */
    public int getDisplayInGrid() {
        return displayInGrid;
    }

    /**
     * @param displayInGrid the displayInGrid to set
     */
    public void setDisplayInGrid(int displayInGrid) {
        this.displayInGrid = displayInGrid;
    }

    /**
     * @return the displayInCollision
     */
    public int getDisplayInCollision() {
        return displayInCollision;
    }

    /**
     * @param displayInCollision the displayInCollision to set
     */
    public void setDisplayInCollision(int displayInCollision) {
        this.displayInCollision = displayInCollision;
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

    /**
     * @return the wildMatch
     */
    public int getWildMatch() {
        return wildMatch;
    }

    /**
     * @param wildMatch the wildMatch to set
     */
    public void setWildMatch(int wildMatch) {
        this.wildMatch = wildMatch;
    }

    /**
     * @return the addressFlag
     */
    public int getAddressFlag() {
        return addressFlag;
    }

    /**
     * @param addressFlag the addressFlag to set
     */
    public void setAddressFlag(int addressFlag) {
        this.addressFlag = addressFlag;
    }

    /**
     * @return the listDisplayOrder
     */
    public int getListDisplayOrder() {
        return listDisplayOrder;
    }

    /**
     * @param listDisplayOrder the listDisplayOrder to set
     */
    public void setListDisplayOrder(int listDisplayOrder) {
        this.listDisplayOrder = listDisplayOrder;
    }

    /**
     * @return the largeIconDisplayOrder
     */
    public int getLargeIconDisplayOrder() {
        return largeIconDisplayOrder;
    }

    /**
     * @param largeIconDisplayOrder the largeIconDisplayOrder to set
     */
    public void setLargeIconDisplayOrder(int largeIconDisplayOrder) {
        this.largeIconDisplayOrder = largeIconDisplayOrder;
    }

    /**
     * @return the latticeNum
     */
    public int getLatticeNum() {
        return latticeNum;
    }

    /**
     * @param latticeNum the latticeNum to set
     */
    public void setLatticeNum(int latticeNum) {
        this.latticeNum = latticeNum;
    }

    /**
     * @return the rowNum
     */
    public int getRowNum() {
        return rowNum;
    }

    /**
     * @param rowNum the rowNum to set
     */
    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    /**
     * @return the pkField
     */
    public int getPkField() {
        return pkField;
    }

    /**
     * @param pkField the pkField to set
     */
    public void setPkField(int pkField) {
        this.pkField = pkField;
    }

    /**
     * @return the displayFieldNameFlag
     */
    public int getDisplayFieldNameFlag() {
        return displayFieldNameFlag;
    }

    /**
     * @param displayFieldNameFlag the displayFieldNameFlag to set
     */
    public void setDisplayFieldNameFlag(int displayFieldNameFlag) {
        this.displayFieldNameFlag = displayFieldNameFlag;
    }

    /**
     * @return the statisticsFilterField
     */
    public int getStatisticsFilterField() {
        return statisticsFilterField;
    }

    /**
     * @param statisticsFilterField the statisticsFilterField to set
     */
    public void setStatisticsFilterField(int statisticsFilterField) {
        this.statisticsFilterField = statisticsFilterField;
    }

    /**
     * @return the statisticsFilterCode1
     */
    public String getStatisticsFilterCode1() {
        return statisticsFilterCode1;
    }

    /**
     * @param statisticsFilterCode1 the statisticsFilterCode1 to set
     */
    public void setStatisticsFilterCode1(String statisticsFilterCode1) {
        this.statisticsFilterCode1 = statisticsFilterCode1;
    }

    /**
     * @return the statisticsFilterCode2
     */
    public String getStatisticsFilterCode2() {
        return statisticsFilterCode2;
    }

    /**
     * @param statisticsFilterCode2 the statisticsFilterCode2 to set
     */
    public void setStatisticsFilterCode2(String statisticsFilterCode2) {
        this.statisticsFilterCode2 = statisticsFilterCode2;
    }

    /**
     * @return the statisticsFilterCode3
     */
    public String getStatisticsFilterCode3() {
        return statisticsFilterCode3;
    }

    /**
     * @param statisticsFilterCode3 the statisticsFilterCode3 to set
     */
    public void setStatisticsFilterCode3(String statisticsFilterCode3) {
        this.statisticsFilterCode3 = statisticsFilterCode3;
    }

    /**
     * @return the statisticsFilterFieldOrder
     */
    public int getStatisticsFilterFieldOrder() {
        return statisticsFilterFieldOrder;
    }

    /**
     * @param statisticsFilterFieldOrder the statisticsFilterFieldOrder to set
     */
    public void setStatisticsFilterFieldOrder(int statisticsFilterFieldOrder) {
        this.statisticsFilterFieldOrder = statisticsFilterFieldOrder;
    }

    /**
     * @return the listFieldFlag
     */
    public int getListFieldFlag() {
        return listFieldFlag;
    }

    /**
     * @param listFieldFlag the listFieldFlag to set
     */
    public void setListFieldFlag(int listFieldFlag) {
        this.listFieldFlag = listFieldFlag;
    }

    /**
     * @return the largeIconFieldFlag
     */
    public int getLargeIconFieldFlag() {
        return largeIconFieldFlag;
    }

    /**
     * @param largeIconFieldFlag the largeIconFieldFlag to set
     */
    public void setLargeIconFieldFlag(int largeIconFieldFlag) {
        this.largeIconFieldFlag = largeIconFieldFlag;
    }

    /**
     * @return the polymerizerFlag
     */
    public int getPolymerizerFlag() {
        return polymerizerFlag;
    }

    /**
     * @param polymerizerFlag the polymerizerFlag to set
     */
    public void setPolymerizerFlag(int polymerizerFlag) {
        this.polymerizerFlag = polymerizerFlag;
    }

    /**
     * @return the detailFlag
     */
    public int getDetailFlag() {
        return detailFlag;
    }

    /**
     * @param detailFlag the detailFlag to set
     */
    public void setDetailFlag(int detailFlag) {
        this.detailFlag = detailFlag;
    }

    /**
     * @return the sourceFields
     */
    public String[] getSourceFields() {
        return sourceFields;
    }

    /**
     * @param sourceFields the sourceFields to set
     */
    public void setSourceFields(String[] sourceFields) {
        this.sourceFields = sourceFields;
    }

}
