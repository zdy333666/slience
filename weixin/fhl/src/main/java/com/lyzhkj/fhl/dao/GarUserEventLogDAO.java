/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author breeze
 */
@Repository
public class GarUserEventLogDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void subscribe(String openId, String event) {
        jdbcTemplate.update("insert into gar_user_event_log(id,open_id,event,create_time) values (?,?,?,getdate())", UUID.randomUUID().toString(), openId, event);
    }

}
