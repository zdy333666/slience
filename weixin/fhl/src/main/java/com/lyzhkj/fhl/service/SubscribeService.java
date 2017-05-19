/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.SubscribeDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class SubscribeService {
    
    @Autowired
    private SubscribeDAO subscribeDAO;
    
    public void subscribe(String openId,String event,long createTime){
        subscribeDAO.insert(openId, event, createTime);
    }
}
