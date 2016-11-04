/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabinfo.pojo;

import java.util.Map;

/**
 *
 * @author zdy
 */
public class GABInfoInput {

    private String serverId;
    private String dataObjectCode;
    private Map<String, String> condition;
    private String userCardId;
    private String userName;
    private String userDept;
    private String[] fields;

    public GABInfoInput(String _dataObjectCode, Map<String, String> _condition, String[] _fields, String _userCardId, String _userName, String _userDept) {
        this.dataObjectCode = _dataObjectCode;
        this.condition = _condition;
        this.fields = _fields;
        this.userCardId = _userCardId;
        this.userName = _userName;
        this.userDept = _userDept;
    }

    public GABInfoInput() {

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
     * @return the fields
     */
    public String[] getFields() {
        return fields;
    }

    /**
     * @param fields the fields to set
     */
    public void setFields(String[] fields) {
        this.fields = fields;
    }

    /**
     * @return the dataObjectCode
     */
    public String getDataObjectCode() {
        return dataObjectCode;
    }

    /**
     * @param dataObjectCode the dataObjectCode to set
     */
    public void setDataObjectCode(String dataObjectCode) {
        this.dataObjectCode = dataObjectCode;
    }

    /**
     * @return the serverId
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * @param serverId the serverId to set
     */
    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

}
