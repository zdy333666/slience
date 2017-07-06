/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.pojo.GarPicker;
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
public class GarPickerDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(GarPickerDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     *
     * @param pickerId
     * @return
     */
    public GarPicker getGarPickerByOpenId(String openId) {

        GarPicker garPicker = null;

        StringBuilder strSQL = new StringBuilder();
        strSQL.append(" SELECT GU.userId as pickerId,GU.userName as pickerName,GU.mobilephone,GU.openId,GU.munitid,GU.munit ");
        strSQL.append(" FROM gar_Users GU,");
        strSQL.append(" gar_UserRole GUR,");
        strSQL.append(" gar_Role GR");
        strSQL.append(" WHERE GU.userId = GUR.userId");
        strSQL.append(" AND GU.openId =? ");
        strSQL.append(" AND GUR.roleid = GR.roleid ");
        strSQL.append(" AND GR.wxrole like '%001%' ");

        try {
            Map<String, Object> row = jdbcTemplate.queryForMap(strSQL.toString(), openId);

            garPicker = new GarPicker();
            garPicker.setPickerId((String) row.get("pickerId"));
            garPicker.setPickerName((String) row.get("pickerName"));
            garPicker.setMobilephone((String) row.get("mobilephone"));
            garPicker.setOpenId((String) row.get("openId"));
            garPicker.setMunitid((String) row.get("munitid"));
            garPicker.setMunit((String) row.get("munit"));

        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

        return garPicker;
    }

    /**
     *
     * @param pickerId
     * @return
     */
    public GarPicker getGarPickerById(String userId) {

        GarPicker garPicker = null;

        StringBuilder strSQL = new StringBuilder();
        strSQL.append(" SELECT GU.userId as pickerId,GU.userName as pickerName,GU.mobilephone,GU.openId,GU.munitid,GU.munit ");
        strSQL.append(" FROM gar_Users GU,");
        strSQL.append(" gar_UserRole GUR,");
        strSQL.append(" gar_Role GR");
        strSQL.append(" WHERE GU.userId = GUR.userId");
        strSQL.append(" AND GU.userId =? ");
        strSQL.append(" AND GUR.roleid = GR.roleid ");
        strSQL.append(" AND GR.wxrole like '%001%' ");

        try {
            Map<String, Object> row = jdbcTemplate.queryForMap(strSQL.toString(), userId);

            garPicker = new GarPicker();
            garPicker.setPickerId((String) row.get("pickerId"));
            garPicker.setPickerName((String) row.get("pickerName"));
            garPicker.setMobilephone((String) row.get("mobilephone"));
            garPicker.setOpenId((String) row.get("openId"));
            garPicker.setMunitid((String) row.get("munitid"));
            garPicker.setMunit((String) row.get("munit"));

        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

        return garPicker;
    }

}
