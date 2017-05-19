/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.helper;

import com.lyzhkj.fhl.conf.FHLConfig;
import com.lyzhkj.weixin.common.pojo.WeiXinKFNews;
import com.lyzhkj.weixin.common.pojo.WeiXinKFNewsArticle;
import com.lyzhkj.weixin.common.pojo.WeiXinKFNewsMessage;
import com.lyzhkj.weixin.common.pojo.WeiXinKFTextMessage;
import com.lyzhkj.weixin.common.pojo.WeiXinText;
import com.lyzhkj.weixin.common.util.MessageConst;
import net.sf.json.JSONObject;

/**
 *
 * @author breeze
 */
public class UserBindHelper {

    /**
     * 创建用户注册提醒消息 XML
     *
     * @param FromUserName
     * @param ToUserName
     * @return
     */
//    public static String buildUserBindNotifyMessage(String FromUserName, String ToUserName) {
//
//        ReplyNewsArticle article = new ReplyNewsArticle();
//        article.setTitle("感谢关注");
//        article.setPicUrl(FHLConfig.APP_URL + "/img/welcome.png");
//        article.setDescription("为了提供更好的服务，享受本公众号的完整功能，建议您进行绑定操作！");
//        article.setUrl(FHLConfig.APP_URL + "/bindpage.html?openId=" + ToUserName);
//
//        ReplyNewsMessage message = new ReplyNewsMessage();
//        message.setFromUserName(FromUserName);
//        message.setToUserName(ToUserName);
//        message.setMsgType(MessageConst.MESSAGE_NEWS);
//        message.setCreateTime(System.currentTimeMillis());
//        message.setArticleCount(1);
//        message.getArticles().add(article);
//
//        return WeiXinMessageUtil.newsMessageToXml(message);
//    }
    /**
     * 创建用户注册提醒消息 JSON
     *
     * @param FromUserName
     * @param ToUserName
     * @return
     */
    public static String buildUserBindNotifyMessage(String openId) {

        WeiXinKFNewsArticle article = new WeiXinKFNewsArticle();
        article.setTitle("感谢关注");
        article.setDescription("为了提供更好的服务，享受本公众号的完整功能，建议您进行绑定操作！");
        article.setPicurl(FHLConfig.APP_URL + "/img/welcome.png");
        article.setUrl(FHLConfig.APP_URL + "/user-bind?openId=" + openId);

        WeiXinKFNews news = new WeiXinKFNews();
        news.setArticles(new WeiXinKFNewsArticle[]{article});

        WeiXinKFNewsMessage message = new WeiXinKFNewsMessage();
        message.setTouser(openId);
        message.setMsgtype("news");
        message.setNews(news);

        return JSONObject.fromObject(message).toString();
    }

    /**
     * 创建用户注册提醒消息
     *
     * @param FromUserName
     * @param ToUserName
     * @return
     */
    public static String buildUserBindNotifyKFMessage(String openId) {

        WeiXinKFNewsArticle article = new WeiXinKFNewsArticle();
        article.setTitle("用户绑定提醒");
        article.setDescription("此功能需要您绑定业主身份");
        article.setPicurl(FHLConfig.APP_URL + "/img/welcome.png");
        article.setUrl(FHLConfig.APP_URL + "/user-bind?openId=" + openId);

        WeiXinKFNews news = new WeiXinKFNews();
        news.setArticles(new WeiXinKFNewsArticle[]{article});

        WeiXinKFNewsMessage message = new WeiXinKFNewsMessage();
        message.setTouser(openId);
        message.setMsgtype("news");
        message.setNews(news);

        return JSONObject.fromObject(message).toString();
    }

    /**
     * 创建用户注册成功提醒消息
     *
     * @param FromUserName
     * @param ToUserName
     * @return
     */
    public static String buildUserBindSuccessNotifyMessage(String ToUserName) {

        WeiXinKFTextMessage message = new WeiXinKFTextMessage();
        message.setTouser(ToUserName);
        message.setMsgtype(MessageConst.MESSAGE_TEXT);
        message.setText(new WeiXinText("恭喜您注册成功"));

        return JSONObject.fromObject(message).toString();
    }

    public static String buildUserBindRepeatNotifyMessage(String ToUserName) {

        WeiXinKFTextMessage message = new WeiXinKFTextMessage();
        message.setTouser(ToUserName);
        message.setMsgtype(MessageConst.MESSAGE_TEXT);
        message.setText(new WeiXinText("您已有注册信息"));

        return JSONObject.fromObject(message).toString();
    }

}
