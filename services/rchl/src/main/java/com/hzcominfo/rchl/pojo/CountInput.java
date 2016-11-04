/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.rchl.pojo;

/**
 *
 * @author cominfo4
 */
public class CountInput {

    private String flag = "*"; //数据来源
    private String yycj = "*"; //核录场所
    private String cjbmcode; //核查单位
    private String returncode = "*"; //核查结果
    private int isZero; //显示零值 0：否 1：是
    private String startTime; //核录开始时间
    private String endTime; //核录截止时间

    /**
     * @return the flag
     */
    public String getFlag() {
        return flag;
    }

    /**
     * @param flag the flag to set
     */
    public void setFlag(String flag) {
        this.flag = flag;
    }

    /**
     * @return the yycj
     */
    public String getYycj() {
        return yycj;
    }

    /**
     * @param yycj the yycj to set
     */
    public void setYycj(String yycj) {
        this.yycj = yycj;
    }

    /**
     * @return the cjbmcode
     */
    public String getCjbmcode() {
        return cjbmcode;
    }

    /**
     * @param cjbmcode the cjbmcode to set
     */
    public void setCjbmcode(String cjbmcode) {
        this.cjbmcode = cjbmcode;
    }

    /**
     * @return the returncode
     */
    public String getReturncode() {
        return returncode;
    }

    /**
     * @param returncode the returncode to set
     */
    public void setReturncode(String returncode) {
        this.returncode = returncode;
    }

    /**
     * @return the isZero
     */
    public int getIsZero() {
        return isZero;
    }

    /**
     * @param isZero the isZero to set
     */
    public void setIsZero(int isZero) {
        this.isZero = isZero;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}
