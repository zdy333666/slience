/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.weixin.util;

import com.lyzhkj.fhl.conf.WeiXinConfig;
import com.lyzhkj.weixin.common.util.WeiXinMessageConst;
import net.sf.json.JSONObject;

/**
 * 微信客服消息服务
 *
 * @author breeze
 */
public class WeiXinKFMessageUtil {

    /**
     * 消息回复
     *
     * @param accessToken
     * @return
     * @throws ParseException
     */
    public static JSONObject replyMessage(String accessToken, String message) {
        String url = WeiXinConfig.REPLY_MESSAGE_URL.replace("ACCESS_TOKEN", accessToken);
        //String result = new RestTemplate().postForObject(url, message, String.class);

        return HttpUtil.doPostStr(url, message);
    }

    /**
     * 回复文本客服消息
     *
     * @param FromUserName
     * @param ToUserName
     * @return
     */
    public static JSONObject replyTextMessage(String accessToken, String openId, String text) {

//        WeiXinKFTextMessage message = new WeiXinKFTextMessage();
//        message.setTouser(openId);
//        message.setMsgtype(WeiXinMessageConst.MESSAGE_TEXT);
//        message.setText(new WeiXinText(text));
//
//        //String result = new RestTemplate().postForObject(url, message, String.class);
        String url = WeiXinConfig.REPLY_MESSAGE_URL.replace("ACCESS_TOKEN", accessToken);

        JSONObject message = new JSONObject();
        message.put("touser", openId);
        message.put("msgtype", WeiXinMessageConst.MESSAGE_TEXT);

        JSONObject textNode = new JSONObject();
        textNode.put("content", text);

        message.put("text", textNode);

        return HttpUtil.doPostStr(url, message.toString());
    }

    /**
     * 回复图片客服消息
     *
     * @param FromUserName
     * @param ToUserName
     * @return
     */
    public static JSONObject replyImageMessage(String accessToken, String openId, String mediaId) {

        String url = WeiXinConfig.REPLY_MESSAGE_URL.replace("ACCESS_TOKEN", accessToken);
        //String result = new RestTemplate().postForObject(url, message, String.class);

        JSONObject message = new JSONObject();
        message.put("touser", openId);
        message.put("msgtype", WeiXinMessageConst.MESSAGE_IMAGE);

        JSONObject imageNode = new JSONObject();
        imageNode.put("media_id", mediaId);

        message.put("image", imageNode);

        return HttpUtil.doPostStr(url, message.toString());

    }

}
