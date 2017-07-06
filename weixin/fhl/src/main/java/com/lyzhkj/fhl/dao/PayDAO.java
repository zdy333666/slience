/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.pojo.DbResult;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author breeze
 */
@Repository
public class PayDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 兑换BATCH处理
     *
     * @return
     */
    @Transactional
    public DbResult runBatch_insert_storeExchange(String citizenId, int throwExchangeInt, int recycleExchangeInt, int pickExchangeInt, String cashierId) {
        DbResult dbResult = null;

        String strSQL = "{call dbo.proc_insert_storeExchange(?, ?, ?, ?, ?, ?,?)}";
        try {
            dbResult = (DbResult) jdbcTemplate.execute(
                    new CallableStatementCreator() {
                public CallableStatement createCallableStatement(Connection con) throws SQLException {

                    CallableStatement cs = con.prepareCall(strSQL);
                    cs.setString(1, citizenId);
                    cs.setInt(2, throwExchangeInt);
                    cs.setInt(3, recycleExchangeInt);
                    cs.setInt(4, pickExchangeInt);
                    cs.setString(5, cashierId);
                    cs.registerOutParameter(6, Types.INTEGER);
                    cs.registerOutParameter(7, Types.VARCHAR);
                    return cs;
                }
            }, new CallableStatementCallback() {
                public DbResult doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                    cs.execute();
                    DbResult dbResult = new DbResult();
                    dbResult.errCode = cs.getInt(6);
                    dbResult.errMsg = cs.getString(7);
                    return dbResult;// 获取输出参数的值   
                }
            });
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return dbResult;
    }

}
