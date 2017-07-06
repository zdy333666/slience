/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dto;

/**
 *
 * @author breeze
 */
public class GroupOutput {

    private String id;
    private String name;
    private int score;
    private boolean asAdmin;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the asAdmin
     */
    public boolean isAsAdmin() {
        return asAdmin;
    }

    /**
     * @param asAdmin the asAdmin to set
     */
    public void setAsAdmin(boolean asAdmin) {
        this.asAdmin = asAdmin;
    }

}
