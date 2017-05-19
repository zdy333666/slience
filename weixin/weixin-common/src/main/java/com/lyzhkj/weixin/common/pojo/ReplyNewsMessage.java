/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.weixin.common.pojo;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author breeze
 */
public class ReplyNewsMessage extends WeiXinMessage {

    private int ArticleCount;
    private List<ReplyNewsArticle> Articles=new ArrayList<ReplyNewsArticle>();

    /**
     * @return the ArticleCount
     */
    public int getArticleCount() {
        return ArticleCount;
    }

    /**
     * @param ArticleCount the ArticleCount to set
     */
    public void setArticleCount(int ArticleCount) {
        this.ArticleCount = ArticleCount;
    }

    /**
     * @return the Articles
     */
    public List<ReplyNewsArticle> getArticles() {
        return Articles;
    }

    /**
     * @param Articles the Articles to set
     */
    public void setArticles(List<ReplyNewsArticle> Articles) {
        this.Articles = Articles;
    }

}
