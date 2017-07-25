/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarCitizenDAO;
import com.lyzhkj.fhl.dao.GarUserCitizenDAO;
import com.lyzhkj.fhl.dao.GarUserDAO;
import com.lyzhkj.fhl.dto.GroupMember;
import com.lyzhkj.fhl.dto.GroupMemberOutput;
import com.lyzhkj.fhl.dto.GroupOutput;
import com.lyzhkj.fhl.pojo.GarCitizen;
import com.lyzhkj.fhl.pojo.GarUser;
import com.lyzhkj.fhl.util.QRCreateUtil;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author breeze
 */
@Service
public class GroupService {

    @Autowired
    private GarCitizenDAO garCitizenDAO;

    @Autowired
    private GarUserCitizenDAO garUserCitizenDAO;

    @Autowired
    private GarUserDAO garUserDAO;

    public List<GroupOutput> listIntro(String userId) {

        return garUserCitizenDAO.listIntro(userId);
    }

    public GroupMemberOutput listMemberOfGroup(String groupId) {

        List<GroupMember> rows = garUserCitizenDAO.listMemberOfGroup(groupId);
        GarCitizen garCitizen = garCitizenDAO.findGarCitizenById(groupId);

        String kitchenQR = null;
        String otherQR = null;
        if (!(garCitizen.getKitchenQR() == null || garCitizen.getKitchenQR().trim().isEmpty())) {
            byte[] qrData = QRCreateUtil.createQR(garCitizen.getKitchenQR());
            kitchenQR = new StringBuilder("data:image/webp;base64,").append(Base64.getEncoder().encodeToString(qrData)).toString();
        }
        if (!(garCitizen.getOtherQR() == null || garCitizen.getOtherQR().trim().isEmpty())) {
            byte[] qrData = QRCreateUtil.createQR(garCitizen.getOtherQR());
            otherQR = new StringBuilder("data:image/webp;base64,").append(Base64.getEncoder().encodeToString(qrData)).toString();
        }

        GroupMemberOutput result = new GroupMemberOutput();
        result.setName(garCitizen.getHostNo());
        result.setScore(garCitizen.getIntCurrency());
        result.setKitchenQR(kitchenQR);
        result.setOtherQR(otherQR);
        result.setRows(rows);

        return result;
    }

    public int deleteMemberOfGroup(String openId, String groupId, String id) {

        GarUser user = garUserDAO.findUserCloudByOpenId(openId);
        if (user == null) {
            return 0;
        }

        if (!garUserCitizenDAO.checkUserIsAdmin(groupId, user.getUserId())) {
            return -1;
        }

        if (garUserCitizenDAO.deleteMemberOfGroup(id)) {
            return 1;
        }

        return 0;
    }

    public GroupMember searchMember(String groupId, String phoneno) {

        return garUserCitizenDAO.findMemberOfGroupByPhone(groupId, phoneno);
    }

    @Transactional
    public int addMemberOfGroup(String openId, String groupId, String phoneno) {

        GarUser user = garUserDAO.findUserCloudByOpenId(openId);
        if (user == null) {
            return 0;
        }

        if (!garUserCitizenDAO.checkUserIsAdmin(groupId, user.getUserId())) {
            return -1;
        }

        GarUser toAddUser = garUserDAO.findUserCloudByPhoneno(phoneno);
        if (toAddUser == null) {
            return 0;
        }

        if (garUserCitizenDAO.checkUserIsAdded(groupId, toAddUser.getUserId())) {
            return 0;
        }

        if (garUserCitizenDAO.addMemberOfGroup(groupId, toAddUser.getUserId())) {
            return 1;
        }

        return 0;
    }

}
