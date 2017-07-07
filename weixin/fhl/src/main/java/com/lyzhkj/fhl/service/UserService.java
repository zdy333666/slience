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

//    //检查用户是否已进行绑定
//    public boolean checkUserBind(String openId, String roleType) {
//
//        if ("1".equals(roleType)) {  //业主
//            return garCitizenDAO.checkUserIsCitizen(openId);
//        } else if ("2".equals(roleType)) { //商户
//            return garUserDAO.checkUserIsMerchant(openId);
//        } else if ("3".equals(roleType)) {//收银员
//            return garUserDAO.checkUserIsCashier(openId);
//        } else if ("4".equals(roleType)) {//巡检员
//            return garPickerDAO.getGarPickerById(openId) == null;
//        } else if ("5".equals(roleType)) {//市民
//            return garUserDAO.checkUserBind(openId);
//        }
//
//        return false;
//    }
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
    public int bindUser(UserBindInput input) {
        try {
            return garUserDAO.bindUser(input);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

//    //注册用户
//    public boolean userBind(UserBindInput input) {
//
//        String roleType = input.getRoleType();
//
//        try {
//            if ("1".equals(roleType)) {  //业主
//
//            } else if ("2".equals(roleType)) { //商户
//
//            } else if ("3".equals(roleType)) {//收银员
//
//            } else if ("4".equals(roleType)) {//巡检员
//
//            } else if ("5".equals(roleType)) {//市民
//                return garUserDAO.bindUser(input);
//            }
//
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//
//        return false;
//    }
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
