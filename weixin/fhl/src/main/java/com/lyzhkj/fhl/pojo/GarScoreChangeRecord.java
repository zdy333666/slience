/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.pojo;

import java.util.Date;

/**
 *
 * @author breeze
 */
public class GarScoreChangeRecord {

    private String RecordId;
    private String UserId;
    private Date ChangeTime;
    private int Score;
    private String ProductId;
    private String Status;

    /**
     * @return the RecordId
     */
    public String getRecordId() {
        return RecordId;
    }

    /**
     * @param RecordId the RecordId to set
     */
    public void setRecordId(String RecordId) {
        this.RecordId = RecordId;
    }

    /**
     * @return the UserId
     */
    public String getUserId() {
        return UserId;
    }

    /**
     * @param UserId the UserId to set
     */
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    /**
     * @return the ChangeTime
     */
    public Date getChangeTime() {
        return ChangeTime;
    }

    /**
     * @param ChangeTime the ChangeTime to set
     */
    public void setChangeTime(Date ChangeTime) {
        this.ChangeTime = ChangeTime;
    }

    /**
     * @return the Score
     */
    public int getScore() {
        return Score;
    }

    /**
     * @param Score the Score to set
     */
    public void setScore(int Score) {
        this.Score = Score;
    }

    /**
     * @return the ProductId
     */
    public String getProductId() {
        return ProductId;
    }

    /**
     * @param ProductId the ProductId to set
     */
    public void setProductId(String ProductId) {
        this.ProductId = ProductId;
    }

    /**
     * @return the Status
     */
    public String getStatus() {
        return Status;
    }

    /**
     * @param Status the Status to set
     */
    public void setStatus(String Status) {
        this.Status = Status;
    }
}
