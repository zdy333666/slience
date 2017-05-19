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
public class WeiXinKFNews {

    private WeiXinKFNewsArticle[] articles;

    /**
     * @return the articles
     */
    public WeiXinKFNewsArticle[] getArticles() {
        return articles;
    }

    /**
     * @param articles the articles to set
     */
    public void setArticles(WeiXinKFNewsArticle[] articles) {
        this.articles = articles;
    }
}
