/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarCashierDAO;
import com.lyzhkj.fhl.dao.GarCitizenDAO;
import com.lyzhkj.fhl.dao.GarPickerDAO;
import com.lyzhkj.fhl.dao.GarUserCitizenDAO;
import com.lyzhkj.fhl.dao.GarUserDAO;
import com.lyzhkj.fhl.dto.UserBindInput;
import com.lyzhkj.fhl.pojo.GarCashier;
import com.lyzhkj.fhl.pojo.GarCitizen;
import com.lyzhkj.fhl.pojo.GarUser;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author breeze
 */
@Service
public class UserService {

    @Autowired
    private GarUserDAO garUserDAO;

    @Autowired
    private GarCitizenDAO garCitizenDAO;

    @Autowired
    private GarCashierDAO garCashierDAO;

    @Autowired
    private GarPickerDAO garPickerDAO;

    @Autowired
    private GarUserCitizenDAO garUserCitizenDAO;

    public GarCashier findGarCashierByOpenId(String openId) {
        GarUser user = garUserDAO.findUserCloudByOpenId(openId);
        if (user == null || user.getMobilephone() == null || user.getMobilephone().trim().isEmpty()) {
            return null;
        }

        return garCashierDAO.findGarCashierByPhone(user.getMobilephone());
    }

    public boolean checkUserBind(String openId) {

        GarUser user = garUserDAO.findUserCloudByOpenId(openId);
        if (user == null || user.getMobilephone() == null || user.getMobilephone().trim().isEmpty()) {
            return false;
        }

        return true;
    }

    //检查用户是否是业主
    public boolean checkUserIsCitizenByOpenId(String openId) {

        return garCitizenDAO.checkUserIsCitizen(openId);
    }

    //检查用户是否是收银员
    public boolean checkUserIsCashierByOpenId(String openId) {

        GarUser user = garUserDAO.findUserCloudByOpenId(openId);
        if (user == null || user.getMobilephone() == null || user.getMobilephone().trim().isEmpty()) {
            return false;
        }

        return garUserDAO.checkUserIsCashierByPhone(user.getMobilephone());
    }

    //检查用户是否是商户
    public boolean checkUserIsMerchantByOpenId(String openId) {

        GarUser user = garUserDAO.findUserCloudByOpenId(openId);
        if (user == null || user.getMobilephone() == null || user.getMobilephone().trim().isEmpty()) {
            return false;
        }

        return garUserDAO.checkUserIsMerchantByPhone(user.getMobilephone());
    }

    //检查用户是否是巡检员
    public boolean checkUserIsPicker(String openId) {

        return garPickerDAO.getGarPickerByOpenId(openId) == null;
    }

    //注册用户
    @Transactional
    public int bindUser(UserBindInput input) {

        int n = 0;
        try {
            n = garUserDAO.bindUser(input);
            if (n < 0) {
                return n;
            }
            GarUser user = garUserDAO.findUserCloudByPhoneno(input.getPhoneno());
            if (user == null) {
                return 0;
            }
            GarCitizen garCitizen = garCitizenDAO.findGarCitizenByPhone(input.getPhoneno());
            if (garCitizen == null) {
                return 1;
            }

            if (garUserCitizenDAO.checkUserIsAdded(garCitizen.getCitizenId(), user.getUserId())) {
                return 1;
            }

            boolean b = garUserCitizenDAO.addMemberOfGroup(garCitizen.getCitizenId(), user.getUserId(), 1);
            if (b) {
                return 1;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return n;
        }

        return n;
    }

    //注销（解绑）用户
    public boolean userLogoff(String openId) {
        try {
            return garUserDAO.logoffUser(openId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 更换用户绑定的手机号码
     */
    public int changeBindPhone(String openId, String phoneno) {
        try {
            if (garUserDAO.findUserCloudByPhoneno(phoneno) != null) {
                return -1;
            }
            boolean b = garUserDAO.changeBindPhone(openId, phoneno);
            if (b) {
                return 1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public GarUser findUserByOpenId(String openId) {

        return garUserDAO.findUserCloudByOpenId(openId);
    }

    public GarCitizen findCitizenByOpenId(String openId) {

        return garCitizenDAO.findByOpenId(openId);
    }

    public GarCitizen findCitizenByPhone(String phone) {

        return garCitizenDAO.findGarCitizenByPhone(phone);
    }

    public GarCitizen findCitizenById(String citizenId) {

        return garCitizenDAO.findGarCitizenById(citizenId);
    }

    public int getTotalCurrencyScoreOfUser(String openId) {

        int score = 0;

        GarUser user = garUserDAO.findUserCloudByOpenId(openId);
        if (user != null) {
            score += garUserCitizenDAO.getTotalCurrencyScoreInGroupsOfUser(user.getUserId());
            score += user.getIntCurrency();
        }

        return score;
    }

}
