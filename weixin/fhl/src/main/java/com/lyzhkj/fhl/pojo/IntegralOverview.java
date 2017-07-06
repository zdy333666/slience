/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.pojo;

/**
 *
 * @author breeze
 */
public class IntegralOverview {

    private int curr; //可兑换
    private int total;//账户总积分
    private int pick;//巡检积分
    private int recycle;//可回收
    private int put;//投放奖励
    private int activity;//活动积分

    /**
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * @return the pick
     */
    public int getPick() {
        return pick;
    }

    /**
     * @param pick the pick to set
     */
    public void setPick(int pick) {
        this.pick = pick;
    }

    /**
     * @return the put
     */
    public int getPut() {
        return put;
    }

    /**
     * @param put the put to set
     */
    public void setPut(int put) {
        this.put = put;
    }

    /**
     * @return the activity
     */
    public int getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(int activity) {
        this.activity = activity;
    }

    /**
     * @return the curr
     */
    public int getCurr() {
        return curr;
    }

    /**
     * @param curr the curr to set
     */
    public void setCurr(int curr) {
        this.curr = curr;
    }

    /**
     * @return the recycle
     */
    public int getRecycle() {
        return recycle;
    }

    /**
     * @param recycle the recycle to set
     */
    public void setRecycle(int recycle) {
        this.recycle = recycle;
    }

}
