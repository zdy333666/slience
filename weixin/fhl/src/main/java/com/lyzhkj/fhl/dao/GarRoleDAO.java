/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.pojo.GarRole;
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
public class GarRoleDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GarRoleDAO.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 
     * @param userId
     * @return 
     */
    public GarRole findRoleByUserId(String userId){
        GarRole role=null;
        
        try{
             Map<String, Object> row = jdbcTemplate.queryForMap("SELECT r.roleid,r.name  FROM gar_UserRoleCloud ur,gar_RoleCloud r WHERE ur.roleid=r.roleid AND ur.userid=?", userId);

            role = new GarRole();
            role.setId((int) row.get("roleid"));
            role.setName((String) row.get("name"));
            
        }catch(Exception e){
            LOGGER.error("", e);
            return null;
        }
        
        return role;
    }
}
