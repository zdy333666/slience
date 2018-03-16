/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.springboot.demo.pojo;

import java.util.Date;

/**
 *
 * @author breeze
 */
public class EventModel {

    private String say;
    private Date time;

    public EventModel() {

    }

    public EventModel(String say, Date time) {
        this.say = say;
        this.time = time;
    }

    /**
     * @return the say
     */
    public String getSay() {
        return say;
    }

    /**
     * @param say the say to set
     */
    public void setSay(String say) {
        this.say = say;
    }

    /**
     * @return the time
     */
    public Date getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(Date time) {
        this.time = time;
    }

}
