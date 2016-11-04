/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.pojo;

import java.util.Map;

/**
 *
 * @author cominfo4
 */
public class AggregateInput {

    private String dimension;
    private long attrSetId;
    private Map<String, String> condition;
    private int rows = Integer.MAX_VALUE;
    private Map<String, String> auth;

    /**
     * @return the dimension
     */
    public String getDimension() {
        return dimension;
    }

    /**
     * @param dimension the dimension to set
     */
    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    /**
     * @return the condition
     */
    public Map<String, String> getCondition() {
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(Map<String, String> condition) {
        this.condition = condition;
    }

    /**
     * @return the rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * @return the attrSetId
     */
    public long getAttrSetId() {
        return attrSetId;
    }

    /**
     * @param attrSetId the attrSetId to set
     */
    public void setAttrSetId(long attrSetId) {
        this.attrSetId = attrSetId;
    }

    /**
     * @return the auth
     */
    public Map<String, String> getAuth() {
        return auth;
    }

    /**
     * @param auth the auth to set
     */
    public void setAuth(Map<String, String> auth) {
        this.auth = auth;
    }

}
