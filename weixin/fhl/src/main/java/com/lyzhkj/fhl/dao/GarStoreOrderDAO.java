/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.pojo.GarStoreOrder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class GarStoreOrderDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GarStoreOrderDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据商户menuid获得下属所有门店，本月的累计积分
     *
     * @param bean 商户信息对象
     * @throws SQLException
     */
    public Long getAllStoreMonthTotleScorebyId(String menuid, String month) {

        Long monthScore = null;
        try {
            StringBuilder strSQL = new StringBuilder();
            strSQL.append(" SELECT SUM(exchangeInt) as monthScroe ");
            strSQL.append(" FROM gar_StoreOrder");
            strSQL.append(" WHERE storemunitid like ? ");
            strSQL.append(" AND convert(varchar(10),exchangetime,112) >= ? ");
            strSQL.append(" AND convert(varchar(10),exchangetime,112) <= ? ");

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(strSQL.toString(), menuid + "%", month + "01", month + "31");
            for (Map<String, Object> row : rows) {
                monthScore = (long) row.get("monthScroe");
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return monthScore;
    }

    /**
     * 根据收银员ID获得本月的累计积分
     *
     * @param bean 商户信息对象
     * @throws SQLException
     */
    public long getMonthTotleScorebyId(String userId, String month) {

       long monthScore = 0;
        try {
            StringBuilder strSQL = new StringBuilder();
            strSQL.append(" SELECT SUM(exchangeInt) as monthScroe ");
            strSQL.append(" FROM gar_StoreOrder");
            strSQL.append(" WHERE cashierId = ?");
            strSQL.append(" AND convert(varchar(10),exchangetime,112) >= ? ");
            strSQL.append(" AND convert(varchar(10),exchangetime,112) <= ? ");

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(strSQL.toString(), userId, month + "01", month + "31");
            for (Map<String, Object> row : rows) {
                monthScore = (int) row.get("monthScroe");
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return monthScore;
    }

    /**
     * 根据收银员ID获得de累计积分
     *
     * @param bean 商户信息对象
     * @throws SQLException
     */
    public long getTotleScorebyId(String userId) {

       long monthScore = 0;
        try {
            StringBuilder strSQL = new StringBuilder();
            strSQL.append(" SELECT SUM(exchangeInt) as monthScroe ");
            strSQL.append(" FROM gar_StoreOrder");
            strSQL.append(" WHERE cashierId = ?");

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(strSQL.toString(), userId);
            for (Map<String, Object> row : rows) {
                monthScore = (int) row.get("monthScroe");
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return monthScore;
    }

    /**
     * 兑换明细
     *
     * @param bean 居民信息对象
     */
    public List<GarStoreOrder> getGarStoreOrderList(String cashierId) {

        List<GarStoreOrder> lstGarStoreOrder = new ArrayList<>();
        GarStoreOrder garStoreOrder = null;
        String strSQL = "SELECT top 5 citizenId,exchangeInt,exchangetime "
                + "FROM gar_StoreOrder "
                + "where cashierId = ? "
                + "order by storeorderid desc";
        try {

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(strSQL, cashierId);
            for (Map<String, Object> row : rows) {
                garStoreOrder = new GarStoreOrder();
                garStoreOrder.setCitizenId((String) row.get("citizenId"));
                garStoreOrder.setExchangeInt((int) row.get("exchangeInt"));
                garStoreOrder.setExchangetime((Date) row.get("exchangetime"));
                lstGarStoreOrder.add(garStoreOrder);
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return lstGarStoreOrder;
    }

}
