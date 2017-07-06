/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.handler;

import com.lyzhkj.fhl.helper.UserBindHelper;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinKFMessageUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinMessageUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import java.util.Map;

/**
 *
 * @author breeze
 */
public class WeiXinOrderEventHandler implements WeiXinEventHandler {
    
     @Override
    public void handle(Map<String, String> body) {

        String eventKey = body.get("EventKey");
        String openId = body.get("FromUserName");
        //扫码领袋
        if ("".equals(eventKey)) {

            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
            String msg = UserBindHelper.buildBindUserNotifyKFMessage(openId);
            WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(),openId, msg);
        }
    }
    
}
