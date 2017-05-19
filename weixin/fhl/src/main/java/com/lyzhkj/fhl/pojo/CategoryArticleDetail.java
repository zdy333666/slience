/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.pojo;

/**
 *
 * @author breeze
 */
public class CategoryArticleDetail extends CategoryArticleIntro {

    private String content;
    private int hitCount;
    private String bigPic;

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the hitCount
     */
    public int getHitCount() {
        return hitCount;
    }

    /**
     * @param hitCount the hitCount to set
     */
    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    /**
     * @return the bigPic
     */
    public String getBigPic() {
        return bigPic;
    }

    /**
     * @param bigPic the bigPic to set
     */
    public void setBigPic(String bigPic) {
        this.bigPic = bigPic;
    }

}
