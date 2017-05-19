/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.dto.UserBindInput;
import com.lyzhkj.fhl.pojo.GarUser;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author breeze
 */
@Repository
public class GarUserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public boolean checkUserExist(String openId) {

        return jdbcTemplate.queryForRowSet("SELECT openId FROM gar_Users WHERE openId=?", openId).first();
    }

    public boolean checkUserIsMerchant(String openId) {

        return jdbcTemplate.queryForRowSet("SELECT u.userId FROM gar_Users u LEFT JOIN gar_UserRole ur ON u.userId=ur.userid WHERE ur.roleid=11 AND u.openId=?", openId).first();
    }

    public boolean checkUserIsCitizen(String openId) {

        return jdbcTemplate.queryForRowSet("SELECT u.userId FROM gar_Users u LEFT JOIN gar_UserRole ur ON u.userId=ur.userid WHERE ur.roleid=20 AND u.openId=?", openId).first();
    }

    public boolean bindUser(UserBindInput input) {

        int n = jdbcTemplate.update("insert into gar_Users(userId,userName,mobilephone,openId) values (?,?,?,?)", "test", input.getUsername(), input.getPhoneno(), input.getOpenId());

        return n > 0;
    }

    public GarUser findUserByOpenId(String openId) {
        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM gar_Users WHERE openId=?", openId);
        if (row == null) {
            return null;
        }

        GarUser user = new GarUser();
        user.setUserId((String) row.get("userId"));
        user.setUserName((String) row.get("userName"));
        user.setMobilephone((String) row.get("mobilephone"));

        return user;
    }

}
