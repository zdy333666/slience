/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.pojo.GarCashier;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author breeze
 */
@Repository
public class GarCashierDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GarCashierDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    
    public GarCashier findGarCashierByPhone(String phoneno) {

        GarCashier garCashier = null;

        StringBuilder strSQL = new StringBuilder();
        strSQL.append(" SELECT GU.userId,GU.userName,GU.mobilephone,GU.openId,GU.munitid,GU.munit,GU.userType,"
                + "GU.merchantsNum,GU.valid,GM.name as merchantsName ");
        strSQL.append(" FROM gar_Users GU,");
        strSQL.append(" gar_Merchants GM");
        strSQL.append(" WHERE GU.merchantsNum = GM.merchantsNum ");
        strSQL.append(" AND  GU.userType = '3' ");
        strSQL.append(" AND GU.valid = '1' AND GU.mobilephone=?");

        try {
            Map<String, Object> row = jdbcTemplate.queryForMap(strSQL.toString(), phoneno);

            garCashier = new GarCashier();
            garCashier.setUserId((String) row.get("userId"));
            garCashier.setUserName((String) row.get("userName"));
            garCashier.setMobilephone((String) row.get("mobilephone"));
            garCashier.setOpenId((String) row.get("openId"));
            garCashier.setMunitid((String) row.get("munitid"));
            garCashier.setMunit((String) row.get("munit"));
            garCashier.setUserType((String) row.get("userType"));
            garCashier.setMerchantsNum((String) row.get("merchantsNum"));
            garCashier.setValid((String) row.get("valid"));
            garCashier.setMerchantsName((String) row.get("merchantsName"));

        } catch (Exception e) {
            LOGGER.error("",e);
            return null;
        }

        return garCashier;
    }

    
    
//    public GarCashier findGarCashierByOpenId(String openId) {
//
//        GarCashier garCashier = null;
//
//        StringBuilder strSQL = new StringBuilder();
//        strSQL.append(" SELECT GU.userId,GU.userName,GU.mobilephone,GU.openId,GU.munitid,GU.munit,GU.userType,"
//                + "GU.merchantsNum,GU.valid,GM.name as merchantsName ");
//        strSQL.append(" FROM gar_Users GU,");
//        strSQL.append(" gar_Merchants GM");
//        strSQL.append(" WHERE GU.merchantsNum = GM.merchantsNum ");
//        strSQL.append(" AND (GU.userType = '2' or GU.userType = '3') ");
//        strSQL.append(" AND GU.valid = '1' AND GU.openId=?");
//
//        try {
//            Map<String, Object> row = jdbcTemplate.queryForMap(strSQL.toString(), openId);
//
//            garCashier = new GarCashier();
//            garCashier.setUserId((String) row.get("userId"));
//            garCashier.setUserName((String) row.get("userName"));
//            garCashier.setMobilephone((String) row.get("mobilephone"));
//            garCashier.setOpenId((String) row.get("openId"));
//            garCashier.setMunitid((String) row.get("munitid"));
//            garCashier.setMunit((String) row.get("munit"));
//            garCashier.setUserType((String) row.get("userType"));
//            garCashier.setMerchantsNum((String) row.get("merchantsNum"));
//            garCashier.setValid((String) row.get("valid"));
//            garCashier.setMerchantsName((String) row.get("merchantsName"));
//
//        } catch (Exception e) {
//            LOGGER.error("",e);
//            return null;
//        }
//
//        return garCashier;
//    }

}
