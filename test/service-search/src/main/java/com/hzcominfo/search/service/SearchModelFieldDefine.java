/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.service;

/**
 *
 * @author xzh
 */
public class SearchModelFieldDefine {
    private String fieldName;
    private String searchFieldName;
    private String fieldDisplayName;
    private int displayInGrid;
    private int displayWidth;
    private int wildMatch;  //  通配
    private int fieldSpec;  //  1 人  2 车 3 案
    private int addressFlag;
    public String[] searchFieldList;
    
    private int listFieldFlag; //是否列表字段 0：列表字段，1：非列表字段  list_field_flag
    private int largeIconFieldFlag; //是否大图标字段 0：大图标字段，1：非大图标字段  large_icon_field_flag
    private int polymerizerFlag; //聚合字段标志 0：普通字段，1：人员聚合，2：车辆聚合，3：案件聚合  polymerizer_flag
    private int detailFlag; //是否详表字段 0：详表字段，1：非详表字段 detail_flag
    
    private int listDisplayOrder; //列表显示顺序 list_display_order
    private int largeIconDisplayOrder; //大图标显示顺序 large_icon_display_order
    private int latticeNum; //格式 lattice_num
    private int rowNum; //行数 row_num
    private int pkField; //是否主键字段 pk_field
    private int displayFieldNameFlag; //是否显示字段名称 display_field_name_flag
    private int statisticsFilterField; //统计筛选字段 statistics_filter_field
    private String statisticsFilterCode1; //统计筛选编码1 statistics_filter_code1
    private String statisticsFilterCode2; //统计筛选编码2 statistics_filter_code2
    private String statisticsFilterCode3; //统计筛选编码3 statistics_filter_code3
    private int statisticsFilterFieldOrder; //统计筛选字段顺序 statistics_filter_field_order

    /**
     * @return the fieldName
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * @param fieldName the fieldName to set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * @return the searchFieldName
     */
    public String getSearchFieldName() {
        return searchFieldName;
    }

    /**
     * @param searchFieldName the searchFieldName to set
     */
    public void setSearchFieldName(String searchFieldName) {
        this.searchFieldName = searchFieldName;
    }

    /**
     * @return the fieldDisplayName
     */
    public String getFieldDisplayName() {
        return fieldDisplayName;
    }

    /**
     * @param fieldDisplayName the fieldDisplayName to set
     */
    public void setFieldDisplayName(String fieldDisplayName) {
        this.fieldDisplayName = fieldDisplayName;
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
     * @return the displayWidth
     */
    public int getDisplayWidth() {
        return displayWidth;
    }

    /**
     * @param displayWidth the displayWidth to set
     */
    public void setDisplayWidth(int displayWidth) {
        this.displayWidth = displayWidth;
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
     * @param listFieldFlag the listFieldFlag to set
     */
    public void setListFieldFlag(int listFieldFlag) {
        this.listFieldFlag = listFieldFlag;
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
};
