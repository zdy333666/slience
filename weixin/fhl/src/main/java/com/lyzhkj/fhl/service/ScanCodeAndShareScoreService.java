/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.helper.UserBindHelper;
import com.lyzhkj.fhl.util.QRCreateUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinKFMessageUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinMediaUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 扫码进行家庭积分共享-处理
 *
 * @author breeze
 */
@Service
public class ScanCodeAndShareScoreService {

    @Autowired
    private UserService userService;

    public void service(String openId) {

        if (!userService.checkUserBind(openId)) {
            String msg = UserBindHelper.buildBindUserNotifyMessage(openId);
            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
            WeiXinKFMessageUtil.replyMessage(token.getAccessToken(), msg);
            return;
        }

        AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
        byte[] qrData = QRCreateUtil.createQR("家庭积分共享");
        String mediaId = WeiXinMediaUtil.upload(token.getAccessToken(), "image", qrData);

        WeiXinKFMessageUtil.replyImageMessage(token.getAccessToken(), openId, mediaId);
    }
}
