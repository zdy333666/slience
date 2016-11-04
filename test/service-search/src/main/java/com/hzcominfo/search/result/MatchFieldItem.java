/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.result;

/**
 *
 * @author xzh
 */
public class MatchFieldItem {
    private String searchKey;   //  要搜索的关键字
    private String fieldName;   //  字段
    private String matchValue;  //  匹配的值

    /**
     * @return the searchKey
     */
    public String getSearchKey() {
        return searchKey;
    }

    /**
     * @param searchKey the searchKey to set
     */
    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

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
     * @return the matchValue
     */
    public String getMatchValue() {
        return matchValue;
    }

    /**
     * @param matchValue the matchValue to set
     */
    public void setMatchValue(String matchValue) {
        this.matchValue = matchValue;
    }

}
