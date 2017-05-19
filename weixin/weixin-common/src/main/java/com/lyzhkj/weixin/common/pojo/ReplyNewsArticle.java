/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.weixin.common.pojo;

/**
 *
 * @author breeze
 */
public class ReplyNewsArticle {

    private String Title;
    private String Description;
    private String PicUrl;
    private String Url;

    /**
     * @return the Title
     */
    public String getTitle() {
        return Title;
    }

    /**
     * @param Title the Title to set
     */
    public void setTitle(String Title) {
        this.Title = Title;
    }

    /**
     * @return the Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @param Description the Description to set
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }

    /**
     * @return the PicUrl
     */
    public String getPicUrl() {
        return PicUrl;
    }

    /**
     * @param PicUrl the PicUrl to set
     */
    public void setPicUrl(String PicUrl) {
        this.PicUrl = PicUrl;
    }

    /**
     * @return the Url
     */
    public String getUrl() {
        return Url;
    }

    /**
     * @param Url the Url to set
     */
    public void setUrl(String Url) {
        this.Url = Url;
    }
}
