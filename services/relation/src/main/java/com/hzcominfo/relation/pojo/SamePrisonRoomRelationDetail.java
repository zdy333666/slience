/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.pojo;

/**
 *
 * @author cominfo4
 */
public class SamePrisonRoomRelationDetail implements RelationDetail {

    private String time;
    private String prison;
    private String room;

    public SamePrisonRoomRelationDetail() {

    }

    public SamePrisonRoomRelationDetail(String time, String prison, String room) {
        this.time = time;
        this.prison = prison;
        this.room = room;
    }

    /**
     * @return the prison
     */
    public String getPrison() {
        return prison;
    }

    /**
     * @param prison the prison to set
     */
    public void setPrison(String prison) {
        this.prison = prison;
    }

    /**
     * @return the room
     */
    public String getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(String room) {
        this.room = room;
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

}
