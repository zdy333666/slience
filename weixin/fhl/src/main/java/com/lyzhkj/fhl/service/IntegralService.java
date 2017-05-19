/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.IntegralDAO;
import com.lyzhkj.fhl.pojo.IntegralOverview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class IntegralService {
 
     @Autowired
    private IntegralDAO integralDAO;
    
        public IntegralOverview getIntegralOverview(String openId) {
            
            return null;
        }
    
}
