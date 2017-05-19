/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarUserDAO;
import com.lyzhkj.fhl.dto.UserBindInput;
import com.lyzhkj.fhl.pojo.GarUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class UserService {

    @Autowired
    private GarUserDAO garUserDAO;

    //检查用户是否已进行绑定
    public boolean checkUserBind(String openId) {

        return garUserDAO.checkUserExist(openId);
    }
    
    //检查用户是否是业主
    public boolean checkUserIsCitizen(String openId) {

        return garUserDAO.checkUserIsCitizen(openId);
    }

    //检查用户是否是商户
    public boolean checkUserIsMerchant(String openId) {

        return garUserDAO.checkUserIsMerchant(openId);
    }

    //注册用户
    public boolean userBind(UserBindInput input) {

        return garUserDAO.bindUser(input);
    }

    public GarUser findUserByOpenId(String openId) {

        return garUserDAO.findUserByOpenId(openId);
    }

}
