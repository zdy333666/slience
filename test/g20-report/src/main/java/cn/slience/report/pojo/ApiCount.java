/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.report.pojo;

/**
 *
 * @author cominfo4
 */
public class ApiCount {

    private long addTime;
    private long totalFrequency;

    /**
     * @return the addTime
     */
    public long getAddTime() {
        return addTime;
    }

    /**
     * @param addTime the addTime to set
     */
    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    /**
     * @return the totalFrequency
     */
    public long getTotalFrequency() {
        return totalFrequency;
    }

    /**
     * @param totalFrequency the totalFrequency to set
     */
    public void setTotalFrequency(long totalFrequency) {
        this.totalFrequency = totalFrequency;
    }
}
