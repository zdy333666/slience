/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import cn.jpush.api.JPushClient;
import com.lyzhkj.fhl.conf.FHLConfig;
import com.lyzhkj.fhl.dao.GarCitizenDAO;
import com.lyzhkj.fhl.dao.GarTerminalDAO;
import com.lyzhkj.fhl.dao.GarUserDAO;
import com.lyzhkj.fhl.pojo.GarCitizen;
import com.lyzhkj.fhl.pojo.GarUser;
import com.lyzhkj.fhl.weixin.util.FHLConst;
import com.lyzhkj.fhl.weixin.util.HttpUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinKFMessageUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.lyzhkj.fhl.util.OtherUtil;

/**
 * 扫码领袋菜单-处理
 *
 * @author breeze
 */
@Service
public class ScanCodeAndGetGarbageBagService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScanCodeAndGetGarbageBagService.class);

    @Autowired
    private GarCitizenDAO garCitizenDAO;

    @Autowired
    private GarUserDAO garUserDAO;

    @Autowired
    private GarTerminalDAO garTerminalDAO;

    public void service(String openId, String scanResult) {

        AccessToken token = WeiXinAccessTokenUtil.getAccessToken();

        //测试用
        GarUser user = garUserDAO.findUserCloudByOpenId(openId);
        if (user == null) {
            WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, "使用此功能需要您绑定业主身份");
            return;
        }

        GarCitizen garCitizen = garCitizenDAO.findGarCitizenByPhone(user.getMobilephone());
        if (garCitizen == null) {
            //回复当前用户不是业主
            WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, "使用此功能需要您绑定业主身份");
            return;
        }

        try {
            //从扫描的机器二维码获得对应的jpushregid
            Map<String, String> jpushmap = garTerminalDAO.getJpushIdByTerminalNo(scanResult);
            LOGGER.info(scanResult + " --> " + jpushmap);

            String jpushregid = jpushmap.get("jpushregid");
            if (jpushregid == null) {
                WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, FHLConst.INFO_VOID_QR);
                return;
            }

            //提示扫描结果去服务器验证 是否可以领取垃圾袋
            String result = getCheckResult(scanResult, garCitizen.getKitchenQR());
            if ("ok".equals(result)) {

                String jpushtype = jpushmap.get("jpushtype");

                if (callTerminal(garCitizen.getKitchenQR(), jpushregid, jpushtype)) {
                    WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, FHLConst.INFO_TERMINAL_SUCESS);
                } else {
                    WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, FHLConst.INFO_TERMINAL_SYSTEMERR);
                }
            } else {
                //提示 不能领取
                WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, FHLConst.INFO_TERMINAL_FAILURE);
            }

        } catch (Exception e) {
            WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, FHLConst.ERR_TERMINAL);
        }
    }

    /**
     * 和NET服务器确认该二维码是否可以领取垃圾袋
     *
     * @param androiddeviceid
     * @param kitchenQR
     * @return
     */
    public String getCheckResult(String androiddeviceid, String kitchenQR) {
        StringBuilder url = new StringBuilder();
        url.append("http://" + FHLConfig.NET_SERVER_IP + ":" + FHLConfig.NET_SERVER_PORT);
        url.append("/vmservice/checkcode?androiddeviceid=" + androiddeviceid);
        url.append("&apkversion=101&code=" + kitchenQR);

        String result = HttpUtil.doPostStr(url.toString());
        return result;
    }

    /**
     * 推送消息给垃圾袋发放机，可以掉垃圾袋了。
     *
     * @param androiddeviceid
     * @param kitchenQR
     * @return
     */
    public boolean callTerminal(String kitchenQR, String jpushregid, String jpushtype) {
        //给垃圾袋发放机推送掉垃圾袋的消息 采用极光推送
        boolean rtn = true;
        //验证密钥是固定的
        String pushstr = "wxtranid=" + kitchenQR + "&sign=83bc48f2cef78fd6ea8efc6e68a4bfd6&nonce=zbgzt3xcj0yfhqg3";
        try {
            LOGGER.info("callTerminal", "pushstr:" + pushstr + "  jpushtype:" + jpushtype + "  jpushregid:" + jpushregid);
            if ("1".equals(jpushtype)) {
                OtherUtil.iGtPush(pushstr, jpushregid, kitchenQR);
            } else {
                new JPushClient(FHLConfig.JPUSH_MASTER_SECRET, FHLConfig.JPUSH_APP_KEY).sendAndroidMessageWithRegistrationID("title", pushstr, jpushregid);
            }
        } catch (Exception e) {
            LOGGER.error("", e);
            rtn = false;
        }

        return rtn;
    }
}
