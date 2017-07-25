/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.conf.FHLConfig;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinKFMessageUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import com.lyzhkj.weixin.common.pojo.WeiXinKFNews;
import com.lyzhkj.weixin.common.pojo.WeiXinKFNewsArticle;
import com.lyzhkj.weixin.common.pojo.WeiXinKFNewsMessage;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class ChatService {

    public void service(String openId, String content) {

        if ("兑换".equals(content) || "积分兑换".equals(content)) {

            WeiXinKFNewsArticle article = new WeiXinKFNewsArticle();
            article.setTitle("垃圾分类积分兑换流程");
            //article.setDescription("");
            article.setPicurl(FHLConfig.APP_URL + "/img/score-exchange.png");
            article.setUrl("http://mp.weixin.qq.com/s/H4d6sQ3qxFGgO02V2Iorkg");

            WeiXinKFNews news = new WeiXinKFNews();
            news.setArticles(new WeiXinKFNewsArticle[]{article});

            WeiXinKFNewsMessage message = new WeiXinKFNewsMessage();
            message.setTouser(openId);
            message.setMsgtype("news");
            message.setNews(news);

            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
            WeiXinKFMessageUtil.replyMessage(token.getAccessToken(), JSONObject.fromObject(message).toString());

        }
    }

}
