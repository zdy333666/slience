/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.pojo;

/**
 *
 * @author zdy
 */
public class AttrSet {

    private long id;
    private String name;
    private String displayName;
    //private String addComment;

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
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

//    /**
//     * @return the addComment
//     */
//    public String getAddComment() {
//        return addComment;
//    }
//
//    /**
//     * @param addComment the addComment to set
//     */
//    public void setAddComment(String addComment) {
//        this.addComment = addComment;
//    }
}
