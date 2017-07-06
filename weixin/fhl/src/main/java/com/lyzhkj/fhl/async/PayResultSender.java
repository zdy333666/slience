/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.async;

import com.lyzhkj.fhl.dao.GarStoreOrderDAO;
import com.lyzhkj.fhl.pojo.GarCashier;
import com.lyzhkj.fhl.pojo.GarCitizen;
import com.lyzhkj.fhl.util.ToolsUtil;
import com.lyzhkj.fhl.weixin.util.FHLConst;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinKFMessageUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import com.service.webservice.out.NoticeMessageResult;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 兑换结果发送用线程
 *
 * @author breeze
 */
@Component
public class PayResultSender {

    @Autowired
    private GarStoreOrderDAO garStoreOrderDAO;

    @Async
    public void send(GarCashier garCashier, GarCitizen garCitizen, int payScore) {

        try {
            //消息回复(TO:商户)
            sendtoSj(garCashier, garCitizen, payScore);

            //短信发送给居民
            sendtoYz(garCashier, garCitizen, payScore);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把兑换结果发送给商家
     *
     * @param garCashier
     * @param garCitizen
     * @param payScore
     */
    private void sendtoSj(GarCashier garCashier, GarCitizen garCitizen, int payScore) {
        String month = null;
        Long monthScore = null;
        String message = null;
        try {
            //先查询到该收银员最近的积分总和			
            month = new SimpleDateFormat("yyyyMM").format(new Date());
            monthScore = garStoreOrderDAO.getMonthTotleScorebyId(garCashier.getUserId(), month);

            message = MessageFormat.format(FHLConst.INFO_WEIXINQR_PAY_SJ,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                    garCashier.getMerchantsName(),
                    garCitizen.getHostName(),
                    payScore,
                    new SimpleDateFormat("yyyy/MM").format(new Date()),
                    monthScore);

            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
            WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), garCashier.getOpenId(), message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 把兑换结果发送给业主
     *
     * @param garCashier
     * @param garCitizen
     * @param payScore
     */
    private void sendtoYz(GarCashier garCashier, GarCitizen garCitizen, int payScore) {
        try {
            NoticeMessageResult result = ToolsUtil.sendSMS(garCitizen.getMobilephone(), MessageFormat.format(FHLConst.INFO_WEIXIN_PAY_JM, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()), garCashier.getMerchantsName(), payScore, garCitizen.getEffectiveScore()));
            if (result == null || result.getCode() != 0) {
                //SMS发送失败 不用提示巡检员
                throw new Exception("支付结果发送失败  errcode:" + result.getCode() + " info:" + result.getInfo());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
