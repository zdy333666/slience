/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.handler.WeiXinClickEventHandler;
import com.lyzhkj.fhl.handler.WeiXinLocationEventHandler;
import com.lyzhkj.fhl.handler.WeiXinScanCodePushEventHandler;
import com.lyzhkj.fhl.handler.WeiXinSubscribeEventHandler;
import com.lyzhkj.weixin.common.util.WeiXinMessageConst;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author breeze
 */
@Component
public class WeiXinRequestDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinRequestDispatcher.class);

    @Autowired
    private WeiXinSubscribeEventHandler weiXinSubscribeEventHandler;

    @Autowired
    private WeiXinClickEventHandler weiXinClickEventHandler;

    @Autowired
    private WeiXinScanCodePushEventHandler weiXinScanCodePushEventHandler;
    
    @Autowired
    private WeiXinLocationEventHandler weiXinLocationEventHandler;

    public void dispatch(Map<String, String> body) {

        String msgType = body.get("MsgType");
        //输入
        if (WeiXinMessageConst.MESSAGE_TEXT.equals(msgType)) {
            //用户输入
            //String openId = body.get("FromUserName");

            return;
        }

        //菜单点击
        if (WeiXinMessageConst.MESSAGE_EVNET.equals(msgType)) {
            String event = body.get("Event");
            LOGGER.info("event-->" + event);

            //用户关注公众号
            if (WeiXinMessageConst.MESSAGE_SUBSCRIBE.equals(event)
                    || WeiXinMessageConst.MESSAGE_UNSUBSCRIBE.equals(event)) {

                weiXinSubscribeEventHandler.handle(body);

            } else if (WeiXinMessageConst.MESSAGE_CLICK.equals(event)) {

                //菜单点击
                weiXinClickEventHandler.handle(body);

            } else if (WeiXinMessageConst.MESSAGE_SCANCODE_WAITMSG.equals(event)) {
                //垃圾巡检|扫码领袋

            } else if (WeiXinMessageConst.MESSAGE_SCANCODE_PUSH.equals(event)) {

                //扫码领袋
                weiXinScanCodePushEventHandler.handle(body);

            } else if (WeiXinMessageConst.MERCHANT_ORDER.equals(event)) {

            } else if (WeiXinMessageConst.MESSAGE_SCAN.equals(event)
                    || WeiXinMessageConst.MESSAGE_VIEW.equals(event)) {

            }else if (WeiXinMessageConst.MESSAGE_LOCATION.equals(event)) {

                weiXinLocationEventHandler.handle(body);
            }

        }

    }
}
