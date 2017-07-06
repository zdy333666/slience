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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 扫码领袋菜单-处理
 *
 * @author breeze
 */
@Service
public class ScanCodeAndGetGarbageBagService {

    @Autowired
    private GarCitizenDAO garCitizenDAO;

    @Autowired
    private GarUserDAO garUserDAO;

    @Autowired
    private GarTerminalDAO garTerminalDAO;

    public void service(String openId, String scanResult) {

        AccessToken token = WeiXinAccessTokenUtil.getAccessToken();

        //测试用
        //String test_openId = "oZ14ywpD0nisKFUXutC0dtm5sCrI";
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

//        AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
//        //String ticket = WeiXinQrUtil.getQrTicket(token.getAccessToken(), garCitizen.getKitchenQR());
//        //String qrUrl = WeiXinQrUtil.getQrURL(ticket);
//        byte[] qrData = QRCreateUtil.createQR(garCitizen.getKitchenQR());
//        String mediaId = WeiXinMediaUtil.upload(token.getAccessToken(), "image", qrData);
//        WeiXinKFMessageUtil.replyImageMessage(token.getAccessToken(), openId, mediaId);
        try {
            //从扫描的机器二维码获得对应的jpushregid
            Map<String, String> jpushmap = garTerminalDAO.getJpushIdByTerminalNo(scanResult);
            String jpushregid = jpushmap.get("jpushregid");
            if (jpushregid == null) {
                WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, FHLConst.INFO_VOID_QR);
                return;
            }

            //提示扫描结果去服务器验证 是否可以领取垃圾袋
            String result = getCheckResult(scanResult, garCitizen.getKitchenQR());
            if ("ok".equals(result)) {
                if (callTerminal(garCitizen.getKitchenQR(), jpushregid)) {
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
    public boolean callTerminal(String kitchenQR, String jpushregid) {
        //给垃圾袋发放机推送掉垃圾袋的消息 采用极光推送
        boolean rtn = true;
        //验证密钥是固定的
        String pushstr = "wxtranid=" + kitchenQR + "&sign=83bc48f2cef78fd6ea8efc6e68a4bfd6&nonce=zbgzt3xcj0yfhqg3";
        try {
            System.out.println("---------给垃圾袋发放机推送掉垃圾袋的消息 采用极光推送 >" + kitchenQR);
            new JPushClient(FHLConfig.JPUSH_MASTER_SECRET, FHLConfig.JPUSH_APP_KEY).sendAndroidMessageWithRegistrationID("title", pushstr, jpushregid);
        } catch (Exception e) {
            e.printStackTrace();
            rtn = false;
        }

        return rtn;
    }
}
