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
public class GarAccusation {

    private long accusationId;
    private String userId;
    private String accusationImage;
    private String accusationTitle;
    private String accusationContent;
    private Date accusationTime;
    private String handleStatus;
    private String leaveMsg;
    private Date leaveMsgTime;
    private String notified;

    /**
     * @return the accusationId
     */
    public long getAccusationId() {
        return accusationId;
    }

    /**
     * @param accusationId the accusationId to set
     */
    public void setAccusationId(long accusationId) {
        this.accusationId = accusationId;
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

    /**
     * @return the accusationImage
     */
    public String getAccusationImage() {
        return accusationImage;
    }

    /**
     * @param accusationImage the accusationImage to set
     */
    public void setAccusationImage(String accusationImage) {
        this.accusationImage = accusationImage;
    }

    /**
     * @return the accusationTitle
     */
    public String getAccusationTitle() {
        return accusationTitle;
    }

    /**
     * @param accusationTitle the accusationTitle to set
     */
    public void setAccusationTitle(String accusationTitle) {
        this.accusationTitle = accusationTitle;
    }

    /**
     * @return the accusationContent
     */
    public String getAccusationContent() {
        return accusationContent;
    }

    /**
     * @param accusationContent the accusationContent to set
     */
    public void setAccusationContent(String accusationContent) {
        this.accusationContent = accusationContent;
    }

    /**
     * @return the accusationTime
     */
    public Date getAccusationTime() {
        return accusationTime;
    }

    /**
     * @param accusationTime the accusationTime to set
     */
    public void setAccusationTime(Date accusationTime) {
        this.accusationTime = accusationTime;
    }

    /**
     * @return the handleStatus
     */
    public String getHandleStatus() {
        return handleStatus;
    }

    /**
     * @param handleStatus the handleStatus to set
     */
    public void setHandleStatus(String handleStatus) {
        this.handleStatus = handleStatus;
    }

    /**
     * @return the leaveMsg
     */
    public String getLeaveMsg() {
        return leaveMsg;
    }

    /**
     * @param leaveMsg the leaveMsg to set
     */
    public void setLeaveMsg(String leaveMsg) {
        this.leaveMsg = leaveMsg;
    }

    /**
     * @return the leaveMsgTime
     */
    public Date getLeaveMsgTime() {
        return leaveMsgTime;
    }

    /**
     * @param leaveMsgTime the leaveMsgTime to set
     */
    public void setLeaveMsgTime(Date leaveMsgTime) {
        this.leaveMsgTime = leaveMsgTime;
    }

    /**
     * @return the notified
     */
    public String getNotified() {
        return notified;
    }

    /**
     * @param notified the notified to set
     */
    public void setNotified(String notified) {
        this.notified = notified;
    }

}
