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
public class FlightNeighborRelationDetail implements RelationDetail {

    private String time;
    private String strt;
    private String dest;
    private String flight;
    private String detail;

    public FlightNeighborRelationDetail() {
    }

    public FlightNeighborRelationDetail(String time, String strt, String dest, String flight, String detail) {
        this.time = time;
        this.strt = strt;
        this.dest = dest;
        this.flight = flight;
        this.detail = detail;
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
     * @return the start
     */
    public String getStrt() {
        return strt;
    }

    /**
     * @param start the start to set
     */
    public void setStrt(String strt) {
        this.strt = strt;
    }

    /**
     * @return the dest
     */
    public String getDest() {
        return dest;
    }

    /**
     * @param dest the dest to set
     */
    public void setDest(String dest) {
        this.dest = dest;
    }

    /**
     * @return the flight
     */
    public String getFlight() {
        return flight;
    }

    /**
     * @param flight the flight to set
     */
    public void setFlight(String flight) {
        this.flight = flight;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

}
