/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarCashierDAO;
import com.lyzhkj.fhl.dao.GarCitizenDAO;
import com.lyzhkj.fhl.dao.GarMerchantsDAO;
import com.lyzhkj.fhl.dao.GarStoreOrderDAO;
import com.lyzhkj.fhl.dao.GarUserDAO;
import com.lyzhkj.fhl.pojo.GarCashier;
import com.lyzhkj.fhl.pojo.GarCitizen;
import com.lyzhkj.fhl.pojo.GarMerchants;
import com.lyzhkj.fhl.pojo.GarStoreOrder;
import com.lyzhkj.fhl.pojo.GarUser;
import com.lyzhkj.fhl.weixin.util.FHLConst;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinKFMessageUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 兑换查询菜单-处理
 *
 * @author breeze
 */
@Service
public class ExchangeViewService {

    @Autowired
    private GarUserDAO garUserDAO;

    @Autowired
    private GarCitizenDAO GarCitizenDAO;

    @Autowired
    private GarCashierDAO garCashierDAO;

    @Autowired
    private GarMerchantsDAO garMerchantsDAO;

    @Autowired
    private GarStoreOrderDAO garStoreOrderDAO;

    public void service(String openId) {

        //测试用
        //String test_openId = "oZ14ywpKhiuplBdKfKfrBosgehHI";
        //处理逻辑
        String message = null;
        String message2 = null;
        String month = null;
        Long monthScore = null;
        Long totalScore = null;
        try {
            GarUser user = garUserDAO.findUserCloudByOpenId(openId);
            if (user == null || user.getMobilephone() == null || user.getMobilephone().trim().isEmpty()) {
                //回复当前用户不是商户或收银员
                AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
                WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, "使用此功能需要您绑定商户或收银员身份");
                return;
            }

            GarCashier garCashier = garCashierDAO.findGarCashierByPhone(user.getMobilephone());
            if (garCashier == null) {
                //回复当前用户不是商户或收银员
                AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
                WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, "使用此功能需要您绑定商户或收银员身份");
                return;
            }

            //获得商户信息
            GarMerchants garMerchants = garMerchantsDAO.findMerchantsById(garCashier.getMerchantsNum());
            if (garMerchants == null) {
                //没有商户信息
                message = MessageFormat.format(FHLConst.INFO_EXCHANGE_4, garCashier.getUserName());
                AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
                WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, message);
                return;
            }

            if ("2".equals(garCashier.getUserType())) {
                //下属所有门店的月积分总和
                month = new SimpleDateFormat("yyyyMM").format(new Date());
                monthScore = garStoreOrderDAO.getAllStoreMonthTotleScorebyId(garMerchants.getMunitid(), month);

                //商户管理员
                message = MessageFormat.format(FHLConst.INFO_EXCHANGE_3,
                        garCashier.getUserName(),
                        garMerchants.getName(),
                        new DecimalFormat("#,###").format(garMerchants.getExchangeInt()),
                        new DecimalFormat("#,###").format(monthScore),
                        new DecimalFormat("#,###").format(garMerchants.getWithdrawInt()),
                        new DecimalFormat("#,###").format(garMerchants.getHasWithdrawInt()));

                AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
                WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, message);

            } else if ("3".equals(garCashier.getUserType())) {
                //收银员
                month = new SimpleDateFormat("yyyyMM").format(new Date());
                monthScore = garStoreOrderDAO.getMonthTotleScorebyId(garCashier.getUserId(), month);
                totalScore = garStoreOrderDAO.getTotleScorebyId(garCashier.getUserId());

                //显示最近5条兑换记录
                List<GarStoreOrder> lstGarStoreOrder = garStoreOrderDAO.getGarStoreOrderList(garCashier.getUserId());
                if (lstGarStoreOrder.isEmpty()) {
                    //没有兑换记录
                    //提示
                    message2 = MessageFormat.format(FHLConst.INFO_EXCHANGE_1, garCashier.getUserName());
                } else {
                    //提示
                    message2 = MessageFormat.format(FHLConst.INFO_EXCHANGE_2, garCashier.getUserName()) + "\n";
                    for (int i = lstGarStoreOrder.size(); i > 0; i--) {
                        GarStoreOrder garStoreOrder = lstGarStoreOrder.get(i - 1);
                        //居民信息
                        GarCitizen garCitizen = GarCitizenDAO.findGarCitizenById(garStoreOrder.getCitizenId());
                        //居民信息获得失败时候，用未知代替
                        String hostName = (garCitizen == null) ? FHLConst.INFO_UN_KNOW : garCitizen.getHostName();
                        message2 += "No:" + i + "  " + MessageFormat.format(FHLConst.INFO_EXCHANGE_ITEM,
                                new SimpleDateFormat("yy-MM-dd HH:mm").format(garStoreOrder.getExchangetime()),
                                hostName,
                                String.valueOf(garStoreOrder.getExchangeInt())) + "\n";
                    }

                }

                //显示总信息
                message = MessageFormat.format(FHLConst.INFO_EXCHANGE_5,
                        garCashier.getUserName(),
                        garMerchants.getName(),
                        garCashier.getMobilephone(),
                        new DecimalFormat("#,###").format(totalScore),
                        new DecimalFormat("#,###").format(monthScore),
                        message2);
                AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
                WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
            WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, FHLConst.ERR_SHOWVIEW);
        }
    }

}
