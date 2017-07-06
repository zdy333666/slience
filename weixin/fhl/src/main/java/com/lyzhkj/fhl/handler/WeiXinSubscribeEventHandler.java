/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.handler;

import com.lyzhkj.fhl.helper.UserBindHelper;
import com.lyzhkj.fhl.service.SubscribeService;
import com.lyzhkj.fhl.service.UserService;
import com.lyzhkj.fhl.weixin.util.FHLConst;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinKFMessageUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author breeze
 */
@Component
public class WeiXinSubscribeEventHandler implements WeiXinEventHandler {

    @Autowired
    private SubscribeService subscribeService;

    @Autowired
    private UserService userService;

    @Override
    public void handle(Map<String, String> body) {

        String openId = body.get("FromUserName");
        String event = body.get("Event");

        //发生用户关注事件并且此用户未曾绑定时向用户发生绑定提醒消息
        if ("subscribe".equals(event)) {
            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
            WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, FHLConst.SUBSCRIBE);

            if (!userService.checkUserBind(openId)) {
                String msg = UserBindHelper.buildBindUserNotifyMessage(openId);
                WeiXinKFMessageUtil.replyMessage(token.getAccessToken(), msg);
            }
        }

        subscribeService.subscribe(openId, event);
    }

}
