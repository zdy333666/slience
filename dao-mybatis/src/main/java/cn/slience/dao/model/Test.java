/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.dao.model;

import java.util.Date;

/**
 *
 * @author Len
 */
public class Test {

    private long id;

    private String data;

    private Date ts;

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the ts
     */
    public Date getTs() {
        return ts;
    }

    /**
     * @param ts the ts to set
     */
    public void setTs(Date ts) {
        this.ts = ts;
    }

}
