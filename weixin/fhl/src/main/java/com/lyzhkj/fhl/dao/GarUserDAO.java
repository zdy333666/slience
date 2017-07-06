/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.dto.UserBindInput;
import com.lyzhkj.fhl.pojo.GarUser;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
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
public class GarUserDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(GarUserDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean checkUserCloudExistByOpenId(String openId) {

        return jdbcTemplate.queryForRowSet("SELECT openId FROM gar_UserCloud WHERE openId=?", openId).first();
    }

    public boolean checkUserIsCashierByPhone(String phoneno) {

        return jdbcTemplate.queryForRowSet("SELECT userId FROM gar_Users WHERE userType=3 AND mobilephone=?", phoneno).first();
    }

    public boolean checkUserIsMerchantByPhone(String phoneno) {

        return jdbcTemplate.queryForRowSet("SELECT userId FROM gar_Users WHERE userType=2 AND mobilephone=?", phoneno).first();
    }

    @Transactional
    public int bindUser(UserBindInput input) throws SQLException {
        int n = 0;

        try {
            Map<String, Object> row = jdbcTemplate.queryForMap("SELECT openId FROM gar_UserCloud WHERE mobilephone=?", input.getPhoneno());
            if (!(row.get("openId") == null || row.get("openId").toString().trim().isEmpty())) {
                return -1;
            }

            n = jdbcTemplate.update("update gar_UserCloud set openId=?,city=? WHERE mobilephone=?", input.getOpenId(), input.getRegion(), input.getPhoneno());
            if (n <= 0) {
                throw new SQLException();
            }

            return 1;
        } catch (Exception e) {

        }

        try {
            Map<String, Object> row = jdbcTemplate.queryForMap("SELECT mobilephone FROM gar_UserCloud WHERE openId=?", input.getOpenId());
            if (!(row.get("mobilephone") == null || row.get("mobilephone").toString().trim().isEmpty())) {
                return -1;
            }

            n = jdbcTemplate.update("update gar_UserCloud set mobilephone=?,city=? WHERE openId=?", input.getPhoneno(), input.getRegion(), input.getOpenId());
            if (n <= 0) {
                throw new SQLException();
            }

            return 1;
        } catch (Exception e) {

        }

        n = jdbcTemplate.update("insert into gar_UserCloud(userId,city,mobilephone,openId,registerTime) values (?,?,?,?,getdate())", UUID.randomUUID().toString(), input.getRegion(), input.getPhoneno(), input.getOpenId());
        if (n <= 0) {
            throw new SQLException();
        }
        GarUser user = findUserCloudByOpenId(input.getOpenId());
        if (user == null) {
            throw new SQLException();
        }

        n = jdbcTemplate.update("insert into gar_UserRoleCloud (userid, roleid)values (?, 11)", user.getUserId());
        if (n <= 0) {
            throw new SQLException();
        }

        return 1;
    }

    @Transactional
    public boolean logoffUser(String openId) throws SQLException {

        int n = jdbcTemplate.update("update gar_UserCloud set mobilephone='' WHERE openId=?", openId);
        if (n <= 0) {
            throw new SQLException();
        }
        return true;
    }

    @Transactional
    public boolean changeBindPhone(String openId, String phoneno) throws SQLException {

        int n = jdbcTemplate.update("update gar_UserCloud set mobilephone=? WHERE openId=?", phoneno, openId);
        if (n <= 0) {
            throw new SQLException();
        }
        return true;
    }

    public GarUser findUserCloudById(String userId) {

        GarUser user = null;
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM gar_UserCloud WHERE userId=?", userId);

            user = new GarUser();
            user.setUserId((String) row.get("userId"));
            user.setUserName((String) row.get("userName"));
            user.setMobilephone((String) row.get("mobilephone"));
            user.setUserType((String) row.get("userType"));
            user.setOpenId((String) row.get("openId"));

            user.setIntCurrency(row.get("intCurrency") == null ? 0 : (int) row.get("intCurrency"));
            user.setIntSpend(row.get("intSpend") == null ? 0 : (int) row.get("intSpend"));
            user.setIntExpired(row.get("intExpired") == null ? 0 : (int) row.get("intExpired"));
            user.setIntTotal(row.get("intTotal") == null ? 0 : (int) row.get("intTotal"));
            user.setThrowExchangeInt(row.get("throwExchangeInt") == null ? 0 : (int) row.get("throwExchangeInt"));
            user.setRecycleExchangeInt(row.get("recycleExchangeInt") == null ? 0 : (int) row.get("recycleExchangeInt"));
            user.setPickExchangeInt(row.get("pickExchangeInt") == null ? 0 : (int) row.get("pickExchangeInt"));
            user.setActivityExchangeInt(row.get("activityExchangeInt") == null ? 0 : (int) row.get("activityExchangeInt"));
            user.setCity((String) row.get("city"));

        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

        return user;
    }

    public GarUser findUserCloudByOpenId(String openId) {

        GarUser user = null;
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM gar_UserCloud WHERE openId=?", openId);

            user = new GarUser();
            user.setUserId((String) row.get("userId"));
            user.setUserName((String) row.get("userName"));
            user.setMobilephone((String) row.get("mobilephone"));
            user.setUserType((String) row.get("userType"));
            user.setOpenId(openId);

            user.setIntCurrency(row.get("intCurrency") == null ? 0 : (int) row.get("intCurrency"));
            user.setIntSpend(row.get("intSpend") == null ? 0 : (int) row.get("intSpend"));
            user.setIntExpired(row.get("intExpired") == null ? 0 : (int) row.get("intExpired"));
            user.setIntTotal(row.get("intTotal") == null ? 0 : (int) row.get("intTotal"));
            user.setThrowExchangeInt(row.get("throwExchangeInt") == null ? 0 : (int) row.get("throwExchangeInt"));
            user.setRecycleExchangeInt(row.get("recycleExchangeInt") == null ? 0 : (int) row.get("recycleExchangeInt"));
            user.setPickExchangeInt(row.get("pickExchangeInt") == null ? 0 : (int) row.get("pickExchangeInt"));
            user.setActivityExchangeInt(row.get("activityExchangeInt") == null ? 0 : (int) row.get("activityExchangeInt"));
            user.setCity((String) row.get("city"));

        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

//        if (user == null) {
//            try {
//                Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM gar_Users WHERE openId=?", openId);
//
//                user = new GarUser();
//                user.setUserId((String) row.get("userId"));
//                user.setUserName((String) row.get("userName"));
//                user.setMobilephone((String) row.get("mobilephone"));
//                user.setUserType((String) row.get("userType"));
//
//                user.setIntCurrency(row.get("intCurrency") == null ? 0 : (int) row.get("intCurrency"));
//                user.setIntSpend(row.get("intSpend") == null ? 0 : (int) row.get("intSpend"));
//                user.setIntExpired(row.get("intExpired") == null ? 0 : (int) row.get("intExpired"));
//                user.setIntTotal(row.get("intTotal") == null ? 0 : (int) row.get("intTotal"));
//                user.setThrowExchangeInt(row.get("throwExchangeInt") == null ? 0 : (int) row.get("throwExchangeInt"));
//                user.setRecycleExchangeInt(row.get("recycleExchangeInt") == null ? 0 : (int) row.get("recycleExchangeInt"));
//                user.setPickExchangeInt(row.get("pickExchangeInt") == null ? 0 : (int) row.get("pickExchangeInt"));
//                user.setCity((String) row.get("city"));
//
//            } catch (Exception e) {
//                LOGGER.error("", e);
//                return null;
//            }
//        }
        return user;
    }

    public GarUser findUserCloudByPhoneno(String phoneno) {

        GarUser user = null;
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM gar_UserCloud WHERE mobilephone=?", phoneno);

            user = new GarUser();
            user.setUserId((String) row.get("userId"));
            user.setUserName((String) row.get("userName"));
            user.setMobilephone((String) row.get("mobilephone"));
            user.setUserType((String) row.get("userType"));
            user.setOpenId((String) row.get("openId"));

            user.setIntCurrency(row.get("intCurrency") == null ? 0 : (int) row.get("intCurrency"));
            user.setIntSpend(row.get("intSpend") == null ? 0 : (int) row.get("intSpend"));
            user.setIntExpired(row.get("intExpired") == null ? 0 : (int) row.get("intExpired"));
            user.setIntTotal(row.get("intTotal") == null ? 0 : (int) row.get("intTotal"));
            user.setThrowExchangeInt(row.get("throwExchangeInt") == null ? 0 : (int) row.get("throwExchangeInt"));
            user.setRecycleExchangeInt(row.get("recycleExchangeInt") == null ? 0 : (int) row.get("recycleExchangeInt"));
            user.setPickExchangeInt(row.get("pickExchangeInt") == null ? 0 : (int) row.get("pickExchangeInt"));
            user.setActivityExchangeInt(row.get("activityExchangeInt") == null ? 0 : (int) row.get("activityExchangeInt"));
            user.setCity((String) row.get("city"));

        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

        return user;
    }

    public boolean deductScore(String userId, int integral) {

        String sql = "UPDATE gar_UserCloud SET intTotal = (intTotal-?), intCurrency = (intCurrency-?),intSpend=(intSpend+?) WHERE userId=? AND intCurrency-? >=0";
        int n = jdbcTemplate.update(sql, integral, integral, integral, userId, integral);

        return n > 0;
    }

}
