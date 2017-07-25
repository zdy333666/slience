/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.dto.ExchangedRecord;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
public class GarScoreChangeRecordDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(GarScoreChangeRecordDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int countWithUser(String userId) {

        String sql = "SELECT count(*) AS count FROM gar_ScoreChangeRecord scr,gar_Product p WHERE scr.ProductId=p.productId AND scr.Status IN('0','1') AND UserId=?";
        Map<String, Object> row = jdbcTemplate.queryForMap(sql, userId);

        return (int) row.get("count");
    }

    public List<ExchangedRecord> listExchanged(String userId, int page, int size) {

        List<ExchangedRecord> result;

        try {
            int start = size * (page - 1);
            int end = size * page;

            //分页
            String sql = "SELECT w2.n,w1.RecordId,w1.ProductId,w1.Status,p.productName,p.productThumb FROM gar_ScoreChangeRecord w1,gar_Product p, "
                    + " (SELECT TOP " + end + "  row_number() OVER (ORDER BY ChangeTime DESC) n, RecordId FROM gar_ScoreChangeRecord WHERE Status IN('0','1') AND UserId=?) w2"
                    + " WHERE w1.RecordId = w2.RecordId AND w1.ProductId=p.productId AND w2.n > ? ORDER BY w2.n ASC";

            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, userId, start);

            result = new ArrayList<>();
            for (Map<String, Object> row : rows) {
                ExchangedRecord item = new ExchangedRecord();
                item.setId((String) row.get("RecordId"));
                item.setProductId((String) row.get("ProductId"));
                item.setName((String) row.get("productName"));
                item.setPic((String) row.get("productThumb"));
                item.setStatus((String) row.get("Status"));
                result.add(item);
            }
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

        return result;
    }

    /**
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Transactional
    public boolean makeRecordStatusUsed(String id) throws SQLException {
        int n = jdbcTemplate.update("UPDATE gar_ScoreChangeRecord SET Status='1' WHERE RecordId=?", id);
        if (n <= 0) {
            throw new SQLException();
        }

        return true;
    }

    /**
     *
     * @param id
     * @return
     * @throws SQLException
     */
    @Transactional
    public boolean makeRecordStatusExpired(String id) throws SQLException {
        int n = jdbcTemplate.update("UPDATE gar_ScoreChangeRecord SET Status='2' WHERE RecordId=?", id);
        if (n <= 0) {
            throw new SQLException();
        }

        return true;
    }

    public boolean addExchangeRecord(String userId, String productId, int integral) {

        String sql = "INSERT INTO gar_ScoreChangeRecord(RecordId,UserId,ChangeTime,Score,ProductId,Status) VALUES (?,?,getdate(),?,?,'0')";
        int n = jdbcTemplate.update(sql, UUID.randomUUID().toString(), userId, integral, productId);

        return n > 0;
    }

}
