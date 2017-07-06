/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.pojo.GarMerchants;
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
public class GarMerchantsDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(GarMerchantsDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 商户信息查询，兑换记录查询用
     *
     * @param merchantsNum
     * @return
     */
    public GarMerchants findMerchantsById(String merchantsNum) {

        GarMerchants garMerchants = null;
        String strSQL = "SELECT munitid,name,exchangeInt,withdrawInt,hasWithdrawInt "
                + "FROM gar_Merchants "
                + "where merchantsNum = ? ";
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap(strSQL, merchantsNum);

            garMerchants = new GarMerchants();
            garMerchants.setMunitid((String) row.get("munitid"));
            garMerchants.setName((String) row.get("name"));
            garMerchants.setExchangeInt((int) row.get("exchangeInt"));
            garMerchants.setWithdrawInt((int) row.get("withdrawInt"));
            garMerchants.setHasWithdrawInt((int) row.get("hasWithdrawInt"));

        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

        return garMerchants;
    }

}
