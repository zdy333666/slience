/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.scheduler;

import com.lyzhkj.fhl.conf.WeiXinConfig;
import com.lyzhkj.fhl.dao.GarAccusationDAO;
import com.lyzhkj.fhl.dao.GarUserDAO;
import com.lyzhkj.fhl.pojo.GarAccusation;
import com.lyzhkj.fhl.pojo.GarUser;
import com.lyzhkj.fhl.weixin.util.FHLConst;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinKFMessageUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author breeze
 */
@Component
public class FeedBackNotifyScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedBackNotifyScheduler.class);

    @Autowired
    private GarAccusationDAO garAccusationDAO;

    @Autowired
    private GarUserDAO garUserDAO;

    /**
     * 对已处理的举报信息进行反馈提醒
     *
     */
    @Scheduled(fixedDelay = 1000 * 60 * 30)
    public void getAccessToken() {

        Map<String, String> userId_openId_mapper = new HashMap<>();

        List<GarAccusation> reports = garAccusationDAO.findHandledButNotNotify();

        LOGGER.info("------------ this time get " + reports.size() + " reports to notify ---------------------");

        for (GarAccusation report : reports) {

            String userId = report.getUserId();
            GarUser user = garUserDAO.findUserCloudById(userId);
            if (user == null) {
                continue;
            }

            String openId = null;
            if (userId_openId_mapper.containsKey(userId)) {
                openId = userId_openId_mapper.get(userId);
            } else {
                openId = user.getOpenId();
            }
            if (openId == null || openId.trim().isEmpty()) {
                continue;
            }

            userId_openId_mapper.put(userId, openId);

            String message = MessageFormat.format(FHLConst.INFO_REPORT_FEEDBACK,
                    new SimpleDateFormat("yyyy年MM月dd日").format(new Date()),
                    report.getAccusationTitle(), report.getLeaveMsg());

            AccessToken token = WeiXinAccessTokenUtil.getAccessToken(WeiXinConfig.ACCESS_TOKEN_URL, WeiXinConfig.APP_ID, WeiXinConfig.APP_SECRET);
            JSONObject result = WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, message);
            if (result.getInt("errcode") != 0) {
                continue;
            }

            try {
                //不是收银员，提示一下
                garAccusationDAO.markNotified(report.getAccusationId());
            } catch (SQLException ex) {
                LOGGER.error("", ex);
            }
        }

    }

}
