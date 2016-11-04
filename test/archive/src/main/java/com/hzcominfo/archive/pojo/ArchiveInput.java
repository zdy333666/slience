/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.archive.pojo;

import java.util.Map;

/**
 *
 * @author cominfo4
 */
public class ArchiveInput {
    
    private String dimension;
    private long attrSetId;
    private Map<String, Object> condition;
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
    
}
