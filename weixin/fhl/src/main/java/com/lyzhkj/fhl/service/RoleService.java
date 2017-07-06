/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarRoleDAO;
import com.lyzhkj.fhl.pojo.GarRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class RoleService {
    
    @Autowired
    private GarRoleDAO garRoleDAO;
    
    public GarRole findRoleByUserId(String userId){
    
        return garRoleDAO.findRoleByUserId(userId);
}
    
}
