/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dto;

/**
 *
 * @author breeze
 */
public class SearchMemberResult {

    private int code;
    private GroupMember data;

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the data
     */
    public GroupMember getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(GroupMember data) {
        this.data = data;
    }
}
