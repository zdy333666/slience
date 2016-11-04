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
public class SameHotelRoomRelationDetail implements RelationDetail {

    private String time;
    private String hotel;
    private String room;

    public SameHotelRoomRelationDetail() {
    }

    public SameHotelRoomRelationDetail(String time, String hotel, String room) {
        this.time = time;
        this.hotel = hotel;
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

    /**
     * @return the hotel
     */
    public String getHotel() {
        return hotel;
    }

    /**
     * @param hotel the hotel to set
     */
    public void setHotel(String hotel) {
        this.hotel = hotel;
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

}
