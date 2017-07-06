/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarCitizenDAO;
import com.lyzhkj.fhl.dao.GarPickerDAO;
import com.lyzhkj.fhl.dao.GarUserDAO;
import com.lyzhkj.fhl.dao.GarbagPickDAO;
import com.lyzhkj.fhl.pojo.GarCitizen;
import com.lyzhkj.fhl.pojo.GarPicker;
import com.lyzhkj.fhl.pojo.GarUser;
import com.lyzhkj.fhl.pojo.GarbagPick;
import com.lyzhkj.fhl.util.ToolsUtil;
import com.lyzhkj.fhl.weixin.util.FHLConst;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinKFMessageUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 我的分类菜单-处理过程
 *
 * @author breeze
 */
@Service
public class MyCategoryService {

    @Autowired
    private GarUserDAO garUserDAO;

    @Autowired
    private GarCitizenDAO garCitizenDAO;

    @Autowired
    private GarbagPickDAO garbagPickDAO;

    @Autowired
    private GarPickerDAO garPickerDAO;

    public void service(String openId) {

        //测试用
        //String test_openId = "oZ14ywvciRfh2wTxIl7Hyp0pqfI8";
        AccessToken token = WeiXinAccessTokenUtil.getAccessToken();

        GarUser user = garUserDAO.findUserCloudByOpenId(openId);
        if (user == null) {
            WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, "使用此功能需要您绑定业主身份");
            return;
        }

        GarCitizen garCitizen = garCitizenDAO.findGarCitizenByPhone(user.getMobilephone());
        if (garCitizen == null) {
            //回复当前用户不是业主
            WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, "使用此功能需要您绑定业主身份");
            return;
        }

        List<GarbagPick> list = garbagPickDAO.findGarbagPicksByCitizenId(garCitizen.getCitizenId());
        if (list.isEmpty()) {
            String text = MessageFormat.format(FHLConst.INFO_BAGPICK_1, garCitizen.getHostName());
            WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, text);
            return;
        }

        //提示
        String message = MessageFormat.format(FHLConst.INFO_BAGPICK_2, garCitizen.getHostName()) + "\n";

        for (int i = list.size(); i > 0; i--) {
            GarbagPick garbagPick = list.get(i - 1);
            //分拣员信息获得失败时候，用未知代替
            GarPicker garPicker = garPickerDAO.getGarPickerById(garbagPick.getPickerId());
            String pickerName = (garPicker == null) ? FHLConst.INFO_UN_KNOW : garPicker.getPickerName();
            message += "No:" + i + "  " + MessageFormat.format(FHLConst.INFO_BAGPICK_ITEM,
                    new SimpleDateFormat("yy-MM-dd HH:mm").format(garbagPick.getPickTime()),
                    pickerName,
                    ToolsUtil.getGarTypeName(garbagPick.getGarType()),
                    garbagPick.getQrCode().trim(),
                    String.valueOf(garbagPick.getIntPick())) + "\n";
        }
        if (message.length() > 0) {
            //明细
            WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, message);

            message = MessageFormat.format(FHLConst.INFO_USER_INTEGRAL,
                    garCitizen.getHostName(),
                    garCitizen.getThrowExchangeInt(),
                    garCitizen.getRecycleExchangeInt(),
                    garCitizen.getPickExchangeInt(),
                    garCitizen.getAllScore(),
                    garCitizen.getEffectiveScore());

            WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, message);
        }
    }

}
