/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabaggr.pojo;

import java.util.Map;

/**
 *
 * @author zdy
 */
public class SubmitInput {

    private String sessionId;

    private String dimension;
    private long[] serviceIds;
    private Map<String, String> condition;

    private String userId;

    private String userCardId;
    private String userName;
    private String userDept;

    public SubmitInput() {

    }

    public SubmitInput(String sessionId, String dimension, long[] serviceIds, Map<String, String> condition, String userId, String userCardId, String userName, String userDept) {
        this.sessionId = sessionId;
        this.dimension = dimension;
        this.serviceIds = serviceIds;
        this.condition = condition;
        this.userId = userId;

        this.userCardId = userCardId;
        this.userName = userName;
        this.userDept = userDept;
    }

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
     * @return the serviceIds
     */
    public long[] getServiceIds() {
        return serviceIds;
    }

    /**
     * @param serviceIds the serviceIds to set
     */
    public void setServiceIds(long[] serviceIds) {
        this.serviceIds = serviceIds;
    }

    /**
     * @return the userCardId
     */
    public String getUserCardId() {
        return userCardId;
    }

    /**
     * @param userCardId the userCardId to set
     */
    public void setUserCardId(String userCardId) {
        this.userCardId = userCardId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userDept
     */
    public String getUserDept() {
        return userDept;
    }

    /**
     * @param userDept the userDept to set
     */
    public void setUserDept(String userDept) {
        this.userDept = userDept;
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
     * @return the sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId the sessionId to set
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

}
