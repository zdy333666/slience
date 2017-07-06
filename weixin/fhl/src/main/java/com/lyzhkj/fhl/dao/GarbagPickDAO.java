/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.pojo.GarbagPick;
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
public class GarbagPickDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GarbagPickDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 垃圾分拣记录表查询
     *
     * @param bean 居民信息对象
     */
    public List<GarbagPick> findGarbagPicksByCitizenId(String citizenId) {

        List<GarbagPick> result = new ArrayList<>();
        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT top 10 qrCode,garType,citizenId,pickTime,intPick,picker FROM gar_GarbagPick where citizenId = ? order by pickId desc", citizenId);
            for (Map<String, Object> row : rows) {
                GarbagPick item = new GarbagPick();
                item.setPickTime((Date) row.get("pickTime"));
                item.setQrCode((String) row.get("qrCode"));
                item.setGarType((String) row.get("garType"));
                item.setPickerId((String) row.get("picker"));
                item.setIntPick((int) row.get("intPick"));

                result.add(item);
            }
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return result;
    }

}
