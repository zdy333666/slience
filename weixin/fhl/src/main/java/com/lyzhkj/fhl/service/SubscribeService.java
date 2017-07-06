/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarUserEventLogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 记录微信用户关注日志
 *
 * @author breeze
 */
@Service
public class SubscribeService {

    @Autowired
    private GarUserEventLogDAO garUserEventLogDAO;

    public void subscribe(String openId, String event) {
        garUserEventLogDAO.subscribe(openId, event);
    }

}
