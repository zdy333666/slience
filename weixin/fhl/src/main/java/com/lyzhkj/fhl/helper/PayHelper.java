/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.helper;

import com.lyzhkj.fhl.pojo.GarCitizen;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author breeze
 */
public class PayHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayHelper.class);

    /**
     * 计算打分分布
     *
     * @param garCitizen
     * @param payScore
     * @return
     * @throws LogicException
     */
    public static HashMap<String, Integer> getScore(GarCitizen garCitizen, int payScore) throws Exception {
        int recycleExchange = 0;
        int throwExchange = 0;
        int pickExchange = 0;
        int tempExchange = 0;	//计算用剩余
        HashMap<String, Integer> scoreMap = new HashMap<>();

        if (payScore > garCitizen.getEffectiveScore()) {
            throw new Exception("getScore_ERR "
                    + "citizenId:" + garCitizen.getCitizenId()
                    + " payScore:" + payScore
                    + " EffectiveScore:" + garCitizen.getEffectiveScore());
        }

        if (garCitizen.getIntExchangeType() != null) {
            tempExchange = payScore;
            // 先口可回收积分
            if (garCitizen.getIntExchangeType().contains("2")) {

                if (tempExchange >= garCitizen.getRecycleExchangeInt()) {
                    recycleExchange = garCitizen.getRecycleExchangeInt();
                    tempExchange = tempExchange - garCitizen.getRecycleExchangeInt();
                } else {
                    recycleExchange = tempExchange;
                    tempExchange = 0;
                }
            }
            // 再扣巡检积分 (tempExchange < 0 是积分不够扣）
            if (garCitizen.getIntExchangeType().contains("3") && tempExchange > 0) {

                if (tempExchange >= garCitizen.getPickExchangeInt()) {
                    pickExchange = garCitizen.getPickExchangeInt();
                    tempExchange = tempExchange - garCitizen.getPickExchangeInt();
                } else {
                    pickExchange = tempExchange;
                    tempExchange = 0;
                }

            }
            // 最后扣投放积分 (tempExchange < 0 是积分不够扣）
            if (garCitizen.getIntExchangeType().contains("1") && tempExchange > 0) {
                if (tempExchange >= garCitizen.getThrowExchangeInt()) {
                    throwExchange = garCitizen.getThrowExchangeInt();
                    tempExchange = tempExchange - garCitizen.getThrowExchangeInt();
                } else {
                    throwExchange = tempExchange;
                    tempExchange = 0;
                }
            }
        }
        scoreMap.put("recycleExchange", recycleExchange);
        scoreMap.put("throwExchange", throwExchange);
        scoreMap.put("pickExchange", pickExchange);
        LOGGER.info("getScore_OK "
                + "citizenId:" + garCitizen.getCitizenId()
                + " payScore:" + payScore
                + " EffectiveScore:" + garCitizen.getEffectiveScore()
                + " recycleExchange:" + recycleExchange
                + " throwExchange:" + throwExchange
                + " pickExchange:" + pickExchange); //兑换分解日志记录

        return scoreMap;
    }

}
