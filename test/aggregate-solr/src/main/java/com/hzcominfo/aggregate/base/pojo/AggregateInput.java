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

    private long dimensionId;
    private long setId;
    private Map<String, Object> condition;
    private int rows = Integer.MAX_VALUE;
    private Map<String, String> auth;

    /**
     * @return the dimensionId
     */
    public long getDimensionId() {
        return dimensionId;
    }

    /**
     * @param dimensionId the dimensionId to set
     */
    public void setDimensionId(long dimensionId) {
        this.dimensionId = dimensionId;
    }

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
     * @return the condition
     */
    public Map<String, Object> getCondition() {
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(Map<String, Object> condition) {
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
