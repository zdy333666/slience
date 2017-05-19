/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.handler;

import com.lyzhkj.fhl.helper.UserBindHelper;
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
public class WeiXinClickEventHandler implements WeiXinEventHandler {

    @Autowired
    private UserService userService;

    @Override
    public void handle(Map<String, String> body) {

        String eventKey = body.get("EventKey");
        String openId = body.get("FromUserName");
        //业主验证
        if ("104".equals(eventKey)) {
            //以上处理需要绑定后才能使用

            if (!userService.checkUserIsCitizen(openId)) {
                //回复当前用户不是业主

                AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
                String msg = UserBindHelper.buildUserBindNotifyKFMessage(openId);
                WeiXinMessageUtil.replyMessage(token.getAccessToken(), msg);
                return;
            }
            
            //处理逻辑

        }

        //商户验证
        if ("203".equals(eventKey)) {
            //以上处理需要绑定后才能使用

            if (!userService.checkUserIsMerchant(openId)) {
                //回复当前用户不是商户

                AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
                String msg = UserBindHelper.buildUserBindNotifyKFMessage(openId);
                WeiXinMessageUtil.replyMessage(token.getAccessToken(), msg);
                return;
            }
            
            //处理逻辑

        }

    }

}
