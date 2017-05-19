/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.pojo.GarRole;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author breeze
 */
@Repository
public class GarRoleDAO {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public  List<GarRole>  findRolesByUserId(String userId){
         List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT r.roleid,r.name FROM gar_UserRole ur LEFT JOIN gar_Role r ON ur.roleid=r.roleid WHERE ur.userid=?",userId);

        List<GarRole> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            GarRole role = new GarRole();
            role.setRoleId((int) row.get("roleid"));
            role.setName((String) row.get("name"));

            result.add(role);
        }
        
        return result;
    }
    
    
}
