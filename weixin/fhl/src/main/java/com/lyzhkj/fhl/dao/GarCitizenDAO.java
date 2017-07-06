/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.dto.UserBindInput;
import com.lyzhkj.fhl.pojo.GarCitizen;
import java.sql.SQLException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author breeze
 */
@Repository
public class GarCitizenDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(GarCitizenDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public boolean bindUser(UserBindInput input) throws SQLException {
        int n = jdbcTemplate.update("update gar_Citizen set openId=? WHERE mobilephone=?", input.getOpenId(), input.getPhoneno());
        if (n <= 0) {
            throw new SQLException();
        }
        return false;
    }

    public boolean checkUserIsCitizen(String openId) {

        return jdbcTemplate.queryForRowSet("SELECT citizenId FROM gar_Citizen WHERE openId=?", openId).first();
    }

    /**
     *
     * @param openId
     * @return
     */
    public GarCitizen findByOpenId(String openId) {

        GarCitizen result = null;
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap("SELECT citizenId FROM gar_Citizen WHERE openId=?", openId);
            String citizenId = (String) row.get("citizenId");
            if (!(citizenId == null || citizenId.trim().isEmpty())) {
                result = findGarCitizenById(citizenId);
            }
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

        return result;
    }

    /**
     *
     * @param openId
     * @return
     */
    public GarCitizen findGarCitizenById(String citizenId) {

        GarCitizen result = null;
        try {

            String strSQL = "SELECT GC.citizenId,GC.hostName,GC.hostNo,GC.mobilephone,GC.kitchenQR,"
                    + " GC.otherQR,GC.intCurrency,GC.openId,GC.weixinQR,GC.kitchenMid,GC.otherMid,"
                    + " GC.munit,GC.munitid,GC.throwExchangeInt,GC.recycleExchangeInt,GC2.intExchangeType,GC.pickExchangeInt "
                    + " FROM gar_Citizen GC,gar_Community GC2 "
                    + " where GC.isDel = 0 "
                    + " AND GC.communityId = GC2.id  "
                    + " AND GC.citizenId = ?"
                    + " order by GC.citizenId";

            Map<String, Object> row = jdbcTemplate.queryForMap(strSQL, citizenId);

            result = new GarCitizen();
            result.setCitizenId((String) row.get("citizenId"));
            result.setHostName((String) row.get("hostName"));
            result.setHostNo((String) row.get("hostNo"));
            result.setMobilephone((String) row.get("mobilephone"));
            result.setKitchenQR((String) row.get("kitchenQR"));
            result.setOtherQR((String) row.get("otherQR"));
            result.setIntCurrency((int) row.get("intCurrency"));
            result.setOpenId((String) row.get("openId"));
            result.setWeixinQR((String) row.get("weixinQR"));
            result.setKitchenMid((String) row.get("kitchenMid"));
            result.setOtherMid((String) row.get("otherMid"));
            result.setMunit((String) row.get("munit"));
            result.setMunitid((String) row.get("munitid"));
            result.setThrowExchangeInt((int) row.get("throwExchangeInt"));
            result.setRecycleExchangeInt((int) row.get("recycleExchangeInt"));
            result.setIntExchangeType((String) row.get("intExchangeType"));
            result.setPickExchangeInt((int) row.get("pickExchangeInt"));

        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

        return result;
    }

    /**
     *
     * @param openId
     * @return
     */
    public GarCitizen findGarCitizenByPhone(String phone) {

        GarCitizen result = null;
        try {

            String strSQL = "SELECT GC.citizenId,GC.hostName,GC.hostNo,GC.mobilephone,GC.kitchenQR,"
                    + " GC.otherQR,GC.intCurrency,GC.openId,GC.weixinQR,GC.kitchenMid,GC.otherMid,"
                    + " GC.munit,GC.munitid,GC.throwExchangeInt,GC.recycleExchangeInt,GC2.intExchangeType,GC.pickExchangeInt "
                    + " FROM gar_Citizen GC,gar_Community GC2 "
                    + " where GC.isDel = 0 "
                    + " AND GC.communityId = GC2.id  "
                    + " AND GC.mobilephone = ?"
                    + " order by GC.citizenId";

            Map<String, Object> row = jdbcTemplate.queryForMap(strSQL, phone);

            result = new GarCitizen();
            result.setCitizenId((String) row.get("citizenId"));
            result.setHostName((String) row.get("hostName"));
            result.setHostNo((String) row.get("hostNo"));
            result.setMobilephone((String) row.get("mobilephone"));
            result.setKitchenQR((String) row.get("kitchenQR"));
            result.setOtherQR((String) row.get("otherQR"));
            result.setIntCurrency((int) row.get("intCurrency"));
            result.setOpenId((String) row.get("openId"));
            result.setWeixinQR((String) row.get("weixinQR"));
            result.setKitchenMid((String) row.get("kitchenMid"));
            result.setOtherMid((String) row.get("otherMid"));
            result.setMunit((String) row.get("munit"));
            result.setMunitid((String) row.get("munitid"));
            result.setThrowExchangeInt((int) row.get("throwExchangeInt"));
            result.setRecycleExchangeInt((int) row.get("recycleExchangeInt"));
            result.setIntExchangeType((String) row.get("intExchangeType"));
            result.setPickExchangeInt((int) row.get("pickExchangeInt"));

        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

        return result;
    }

    /**
     *
     * @param qrStr
     * @return
     */
    public GarCitizen findGarCitizenByQR(String qrStr) {
        GarCitizen result = null;

        try {
            String strSQL = "SELECT GC.citizenId,GC.hostName,GC.hostNo,GC.mobilephone,"
                    + " GC.kitchenQR,GC.otherQR,GC.intCurrency,GC.openId,GC.weixinQR,"
                    + " GC.kitchenMid,GC.otherMid,GC.munit,GC.munitid,GC.throwExchangeInt,"
                    + " GC.recycleExchangeInt,GC2.intExchangeType,GC.pickExchangeInt "
                    + " FROM gar_Citizen GC,"
                    + " gar_Community GC2 "
                    + " where GC.isDel = 0 "
                    + " AND GC.communityId = GC2.id AND (GC.kitchenQR=? OR GC.otherQR=? )"
                    + " order by GC.citizenId";

            Map<String, Object> row = jdbcTemplate.queryForMap(strSQL, qrStr, qrStr);
            result = new GarCitizen();
            result.setCitizenId((String) row.get("citizenId"));
            result.setHostName((String) row.get("hostName"));
            result.setHostNo((String) row.get("hostNo"));
            result.setMobilephone((String) row.get("mobilephone"));
            result.setKitchenQR((String) row.get("kitchenQR"));
            result.setOtherQR((String) row.get("otherQR"));
            result.setIntCurrency((int) row.get("intCurrency"));
            result.setOpenId((String) row.get("openId"));
            result.setWeixinQR((String) row.get("weixinQR"));
            result.setKitchenMid((String) row.get("kitchenMid"));
            result.setOtherMid((String) row.get("otherMid"));
            result.setMunit((String) row.get("munit"));
            result.setMunitid((String) row.get("munitid"));
            result.setThrowExchangeInt((int) row.get("throwExchangeInt"));
            result.setRecycleExchangeInt((int) row.get("recycleExchangeInt"));
            result.setIntExchangeType((String) row.get("intExchangeType"));
            result.setPickExchangeInt((int) row.get("pickExchangeInt"));

        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

        return result;
    }

    public boolean scoreExchange(String citizenId, int intExchange, int throwExchangeInt, int recycleExchangeInt, int pickExchangeInt) {

        String sql = "update gar_Citizen set intTotal = (intTotal-?), intCurrency = (intCurrency-?),intSpend=(intSpend+?), throwExchangeInt=(throwExchangeInt-?),recycleExchangeInt=(recycleExchangeInt-?),pickExchangeInt=(pickExchangeInt-?) where citizenId = ?";

        int n = jdbcTemplate.update(sql, intExchange, intExchange, intExchange, throwExchangeInt, recycleExchangeInt, pickExchangeInt, citizenId);
        return n > 0;
    }
}
