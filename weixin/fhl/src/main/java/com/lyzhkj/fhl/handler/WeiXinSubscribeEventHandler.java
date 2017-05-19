/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.handler;

import com.lyzhkj.fhl.helper.UserBindHelper;
import com.lyzhkj.fhl.service.SubscribeService;
import com.lyzhkj.fhl.service.UserService;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinMessageUtil;
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
        long createTime = Long.parseLong(body.get("CreateTime"));

        //发生用户关注事件并且此用户未曾绑定时向用户发生绑定提醒消息
        if ("subscribe".equals(event) && (!userService.checkUserBind(openId))) {

            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
            String msg = UserBindHelper.buildUserBindNotifyMessage(openId);
            WeiXinMessageUtil.replyMessage(token.getAccessToken(), msg);
        }

        subscribeService.subscribe(openId, event, createTime);
    }

}
