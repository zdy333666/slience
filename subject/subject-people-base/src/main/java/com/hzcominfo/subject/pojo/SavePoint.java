/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.subject.pojo;

import org.bson.types.ObjectId;

/**
 *
 * @author cominfo4
 */
public class SavePoint {
    
    private String type;
    private String table;
    private Object value;
    private int skip;

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the table
     */
    public String getTable() {
        return table;
    }

    /**
     * @param table the table to set
     */
    public void setTable(String table) {
        this.table = table;
    }

    /**
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * @return the skip
     */
    public int getSkip() {
        return skip;
    }

    /**
     * @param skip the skip to set
     */
    public void setSkip(int skip) {
        this.skip = skip;
    }
    
}
