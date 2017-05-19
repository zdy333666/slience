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
public class WeiXinKFNewsMessage extends WeiXinKFMessage {

    private WeiXinKFNews news;

    public WeiXinKFNewsMessage() {

    }

    public WeiXinKFNewsMessage(WeiXinKFNews news) {
        this.news = news;
    }

    /**
     * @return the news
     */
    public WeiXinKFNews getNews() {
        return news;
    }

    /**
     * @param news the news to set
     */
    public void setNews(WeiXinKFNews news) {
        this.news = news;
    }
}
