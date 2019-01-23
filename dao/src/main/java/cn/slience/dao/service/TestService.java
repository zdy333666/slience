/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.dao.service;

import java.sql.SQLException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Len
 */
@Service
public class TestService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional(rollbackFor = Exception.class)
    public int test() throws Exception {

        String sql = "insert into test (data) values (?)";

        int result1 = jdbcTemplate.update(sql, "hello2");
        int result2 = jdbcTemplate.update(sql, "hello3");

        return result1 & result2;
    }

}
