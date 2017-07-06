/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.weixin.util;

import com.lyzhkj.fhl.conf.WeiXinConfig;
import com.lyzhkj.weixin.common.pojo.ReplyNewsArticle;
import com.lyzhkj.weixin.common.pojo.ReplyNewsMessage;
import com.lyzhkj.weixin.common.pojo.WeiXinMessage;
import com.thoughtworks.xstream.XStream;
import net.sf.json.JSONObject;

/**
 *
 * @author breeze
 */
public class WeiXinMessageUtil {

    /**
     * textMessageToXml
     *
     * @param textMessage
     * @return
     */
    public static String messageToXml(WeiXinMessage message) {
        XStream xstream = new XStream();
        xstream.alias("xml", message.getClass());
        return xstream.toXML(message);
    }

    /**
     * newsMessageToXml
     *
     * @param packet
     * @param url
     * @return
     */
    public static String newsMessageToXml(ReplyNewsMessage message) {

        StringBuilder s = new StringBuilder();
        s.append("<xml>");
        s.append("<ToUserName>");
        s.append(message.getToUserName());
        s.append("</ToUserName>");

        s.append("<FromUserName>");
        s.append(message.getFromUserName());
        s.append("</FromUserName>");

        s.append("<CreateTime>");
        s.append(message.getCreateTime());
        s.append("</CreateTime>");

        s.append("<MsgType>");
        s.append(message.getMsgType());
        s.append("</MsgType>");

        s.append("<ArticleCount>");
        s.append(message.getArticles().size());
        s.append("</ArticleCount>");

        s.append("<Articles>");
        for (ReplyNewsArticle article : message.getArticles()) {
            s.append("<item>");

            s.append("<Title>");
            s.append(article.getTitle());
            s.append("</Title>");

            s.append("<Description>");
            s.append(article.getDescription());
            s.append("</Description>");

            s.append("<PicUrl>");
            s.append(article.getPicUrl());
            s.append("</PicUrl>");

            s.append("<Url>");
            s.append(article.getUrl());
            s.append("</Url>");

            s.append("</item>");
        }
        s.append("</Articles>");
        s.append("</xml>");

        return s.toString();
    }

}
