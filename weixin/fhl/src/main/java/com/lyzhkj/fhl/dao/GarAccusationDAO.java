/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.dto.ReportInput;
import com.lyzhkj.fhl.pojo.GarAccusation;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
public class GarAccusationDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(GarAccusationDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public boolean addReport(ReportInput input) throws SQLException {

        int n = jdbcTemplate.update("insert into gar_Accusation (UserId,AccusationTitle,AccusationContent,AccusationTime,HandleStatus,IsIndex,notified)values (?,?,?,getdate(),0,1,0)", input.getUserId(), input.getTitle(), input.getContent());
        if (n <= 0) {
            throw new SQLException();
        }

        return true;
    }

    public boolean hasUnHandByUserId(String userId) {

        return jdbcTemplate.queryForRowSet("SELECT UserId FROM gar_Accusation WHERE HandleStatus=0 AND UserId=?", userId).first();
    }

    public GarAccusation findByUserId(String userId) {

        GarAccusation result = null;
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM gar_Accusation WHERE HandleStatus=0 AND UserId=?", userId);

            result = new GarAccusation();
            result.setAccusationId((long) row.get("AccusationId"));
            result.setUserId((String) row.get("UserId"));
            result.setAccusationImage((String) row.get("AccusationImage"));
            result.setAccusationTitle((String) row.get("AccusationTitle"));
            result.setAccusationContent((String) row.get("AccusationContent"));
            result.setAccusationTime((Date) row.get("AccusationTime"));
            result.setLeaveMsg((String) row.get("LeaveMsg"));
            result.setLeaveMsgTime((Date) row.get("LeaveMsgTime"));
            result.setNotified((String) row.get("notified"));

        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

        return result;
    }

    public List<GarAccusation> findHandledButNotNotify() {

        List<GarAccusation> result = new ArrayList<>();

        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT TOP 10 * FROM gar_Accusation WHERE HandleStatus=1 AND notified=0 ORDER BY AccusationTime ASC");

            for (Map<String, Object> row : rows) {
                GarAccusation item = new GarAccusation();
                item.setAccusationId((long) row.get("AccusationId"));
                item.setUserId((String) row.get("UserId"));
                item.setAccusationImage((String) row.get("AccusationImage"));
                item.setAccusationTitle((String) row.get("AccusationTitle"));
                item.setAccusationContent((String) row.get("AccusationContent"));
                item.setAccusationTime((Date) row.get("AccusationTime"));
                item.setLeaveMsg((String) row.get("LeaveMsg"));
                item.setLeaveMsgTime((Date) row.get("LeaveMsgTime"));
                item.setNotified((String) row.get("notified"));

                result.add(item);
            }

        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return result;
    }

    @Transactional
    public boolean markNotified(long id) throws SQLException {
        int n = jdbcTemplate.update("UPDATE gar_Accusation SET notified=1 WHERE AccusationId=?", id);
        if (n <= 0) {
            throw new SQLException();
        }
        return true;
    }

}
