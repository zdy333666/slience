/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarCitizenDAO;
import com.lyzhkj.fhl.dao.GarProductDAO;
import com.lyzhkj.fhl.dao.GarScoreChangeRecordDAO;
import com.lyzhkj.fhl.dao.GarUserCitizenDAO;
import com.lyzhkj.fhl.dao.GarUserDAO;
import com.lyzhkj.fhl.dto.ExchangedPage;
import com.lyzhkj.fhl.dto.ExchangedRecord;
import com.lyzhkj.fhl.dto.GroupOutput;
import com.lyzhkj.fhl.helper.PayHelper;
import com.lyzhkj.fhl.pojo.GarCitizen;
import com.lyzhkj.fhl.pojo.GarUser;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author breeze
 */
@Service
public class IntegralService {

    @Autowired
    private GarUserDAO garUserDAO;

    @Autowired
    private GarProductDAO garProductDAO;

    @Autowired
    private GarScoreChangeRecordDAO garScoreChangeRecordDAO;

    @Autowired
    private GarUserCitizenDAO garUserCitizenDAO;

    @Autowired
    private GarCitizenDAO garCitizenDAO;

    /**
     *
     * @param openId
     * @param productId
     * @param integral
     * @return
     */
    @Transactional
    public boolean exchange(String openId, String productId, int integral) throws SQLException {

        int needScore = integral;

        GarUser user = garUserDAO.findUserCloudByOpenId(openId);
        if (user == null) {
            return false;
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
                ex.printStackTrace();
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

        if (garProductDAO.deductProductStore(productId)
                && garScoreChangeRecordDAO.addExchangeRecord(userId, productId, needScore)) {
            return true;
        } else {
            throw new SQLException();
        }
    }

    /**
     *
     * @param openId
     * @return
     */
    public ExchangedPage listExchanged(String openId, int page, int size) {

        ExchangedPage result = new ExchangedPage();

        GarUser user = garUserDAO.findUserCloudByOpenId(openId);
        if (user == null) {
            return result;
        }

        String userId = user.getUserId();
        try {
            int count = garScoreChangeRecordDAO.countWithUser(userId);
            List<ExchangedRecord> rows = garScoreChangeRecordDAO.listExchanged(userId, page, size);

            result.setCount(count);
            result.setRows(rows);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     *
     * @param openId
     * @param id
     * @param productId
     * @param code
     * @return
     */
    public boolean makeExchangeUsed(String openId, String id, String productId, String code) {

        try {
            String exchangeCode = garProductDAO.findExchangeCodeById(productId);
            if (!(code != null && code.equals(exchangeCode))) {
                return false;
            }

            return garScoreChangeRecordDAO.makeRecordStatusUsed(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
