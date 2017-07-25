/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarCitizenDAO;
import com.lyzhkj.fhl.dao.GarUserCitizenDAO;
import com.lyzhkj.fhl.dao.GarUserDAO;
import com.lyzhkj.fhl.dto.ExchangeMachinePayDetail;
import com.lyzhkj.fhl.dto.ExchangeMachinePayParam;
import com.lyzhkj.fhl.dto.GroupOutput;
import com.lyzhkj.fhl.helper.PayHelper;
import com.lyzhkj.fhl.pojo.GarCitizen;
import com.lyzhkj.fhl.pojo.GarUser;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author breeze
 */
@Service
public class ExchangeMachinePayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeMachinePayService.class);

    @Autowired
    private GarUserDAO garUserDAO;

    @Autowired
    private GarCitizenDAO garCitizenDAO;

    @Autowired
    private GarUserCitizenDAO garUserCitizenDAO;

    /**
     *
     * @param param
     * @return
     */
    public ExchangeMachinePayDetail getPayDetail(ExchangeMachinePayParam param) {

        String userName = "";

        GarUser user = garUserDAO.findUserCloudByOpenId(param.getOpenId());
        if (user == null) {
            return null;
        }
        GarCitizen garCitizen = garCitizenDAO.findGarCitizenByPhone(user.getMobilephone());
        if (garCitizen != null && !(garCitizen.getHostName() == null || garCitizen.getHostName().trim().isEmpty())) {
            userName = garCitizen.getHostName();
        }

        return null;
    }

    /**
     *
     * @param openId
     * @param integral
     * @return
     */
    @Transactional
    public int pay(ExchangeMachinePayParam param) throws SQLException {
        String openId = param.getOpenId();
        int integral = param.getScore();

        //int needScore = integral;
        GarUser user = garUserDAO.findUserCloudByOpenId(openId);
        if (user == null) {
            return 0;
        }

        String userId = user.getUserId();

        List<GroupOutput> groups = garUserCitizenDAO.listIntro(userId);
        for (GroupOutput group : groups) {

            int groupScore = group.getScore();
            if (!(groupScore > 0)) {
                continue;
            }

            GarCitizen garCitizen = garCitizenDAO.findGarCitizenById(group.getId());
            int payScore = (garCitizen.getEffectiveScore() >= integral) ? integral : garCitizen.getEffectiveScore();

            try {
                HashMap<String, Integer> scoreMap = PayHelper.getScore(garCitizen, payScore);

                if (garCitizen.getRecycleExchangeInt() < scoreMap.get("recycleExchange")) {
                    continue;
                }
                if (garCitizen.getThrowExchangeInt() < scoreMap.get("throwExchange")) {
                    continue;
                }
                if (garCitizen.getPickExchangeInt() < scoreMap.get("pickExchange")) {
                    continue;
                }

                boolean b = garCitizenDAO.scoreExchange(garCitizen.getCitizenId(), payScore, scoreMap.get("throwExchange"), scoreMap.get("recycleExchange"), scoreMap.get("pickExchange"));
                if (!b) {
                    continue;
                }

                integral = integral - payScore;
                if (integral == 0) {
                    break;
                }

            } catch (Exception ex) {
                LOGGER.error("", ex);
                continue;
            }
        }

        if (integral > 0) {
            if (garUserDAO.deductScore(userId, integral)) {
                integral = 0;
            }
        }

        if (integral != 0) {
            throw new SQLException();
        }

        //将积分支付结果推送至终处
        pushPayResult();
        
        //记录兑换记录

        return 1;
    }

    private void pushPayResult() {

    }

}
