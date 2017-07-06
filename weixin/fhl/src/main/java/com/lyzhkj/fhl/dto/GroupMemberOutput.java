/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dto;

import java.util.List;

/**
 *
 * @author breeze
 */
public class GroupMemberOutput {

    private int score;
    private String name;
    private String kitchenQR;
    private String otherQR;
    private List<GroupMember> rows;

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the rows
     */
    public List<GroupMember> getRows() {
        return rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(List<GroupMember> rows) {
        this.rows = rows;
    }

    /**
     * @return the kitchenQR
     */
    public String getKitchenQR() {
        return kitchenQR;
    }

    /**
     * @param kitchenQR the kitchenQR to set
     */
    public void setKitchenQR(String kitchenQR) {
        this.kitchenQR = kitchenQR;
    }

    /**
     * @return the otherQR
     */
    public String getOtherQR() {
        return otherQR;
    }

    /**
     * @param otherQR the otherQR to set
     */
    public void setOtherQR(String otherQR) {
        this.otherQR = otherQR;
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
}
