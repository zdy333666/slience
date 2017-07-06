/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import java.util.HashMap;
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
public class GarTerminalDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(GarTerminalDAO.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据机器ID获得极光推送ID
     */
    public Map<String, String> getJpushIdByTerminalNo(String terminalNo) {

        Map<String, String> map = new HashMap<String, String>();
        String strSQL = "SELECT jpushregid,ISNULL(jpushtype,'0') jpushtype FROM gar_Terminal where terminalNo = ? ";

        try {
            Map<String, Object> row = jdbcTemplate.queryForMap(strSQL, terminalNo);
            map.put("jpushregid", (String) row.get("jpushregid"));
            map.put("jpushtype", (String) row.get("jpushtype"));

        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return map;
    }

}
