/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.dto.GroupOutput;
import com.lyzhkj.fhl.dto.PersonCenterMainOutput;
import com.lyzhkj.fhl.pojo.GarCitizen;
import com.lyzhkj.fhl.pojo.GarUser;
import com.lyzhkj.fhl.pojo.IntegralOverview;
import com.lyzhkj.fhl.service.GroupService;
import com.lyzhkj.fhl.service.UserService;
import com.lyzhkj.fhl.util.QRCreateUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import com.lyzhkj.weixin.common.pojo.WeiXinUserBaseInfo;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 个人中心
 *
 * @author breeze
 */
@Controller
public class PersonCenterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonCenterController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "person-center", method = RequestMethod.GET)
    public void personCenterIndex(HttpServletResponse response, @RequestParam("code") String code) throws IOException {

        LOGGER.info("-------------- person-center index---------------------code:" + code);

        WebPageAccessToken webPageAccessToken = WeiXinUserUtil.getWebPageAccessToken(code);
        LOGGER.info("webPageAccessToken--->" + JSONObject.fromObject(webPageAccessToken));

        String openId = webPageAccessToken.getOpenid();

        //检查用户是否已绑定
        if (!userService.checkUserBind(openId)) {
            response.sendRedirect("/bindpage.html?openId=" + openId);
            return;
        }
        response.sendRedirect("/personalcenter.html?openId=" + openId);
    }

    @RequestMapping(value = "person-center/overview", method = RequestMethod.GET)
    @ResponseBody
    public PersonCenterMainOutput overview(@RequestParam String openId) {

        LOGGER.info("-------------- person-center/overview ---------------------openId:" + openId);

        WeiXinUserBaseInfo userInfo = WeiXinUserUtil.getWeiXinUserBaseInfo(openId);
        LOGGER.info("weixin-userInfo-->" + JSONObject.fromObject(userInfo).toString());

        IntegralOverview integral = new IntegralOverview();
        GarUser user = userService.findUserByOpenId(openId);

        String phoneno = null;
        StringBuilder roleBuilder = new StringBuilder();
        if (user != null) {
            roleBuilder.append("市民");

            if (userService.checkUserIsMerchantByOpenId(openId)) {
                roleBuilder.append("|商户");
            }

            if (userService.checkUserIsCashierByOpenId(openId)) {
                roleBuilder.append("|收银员");
            }

            phoneno = user.getMobilephone();

            List<GroupOutput> groups = groupService.listIntro(user.getUserId());
            for (GroupOutput group : groups) {
                GarCitizen garCitizen = userService.findCitizenById(group.getId());
                if (garCitizen != null) {
                    integral.setCurr(garCitizen.getIntCurrency());
                    integral.setTotal(garCitizen.getAllScore());
                    integral.setPick(garCitizen.getPickExchangeInt());
                    integral.setRecycle(garCitizen.getRecycleExchangeInt());
                    integral.setPut(garCitizen.getThrowExchangeInt());
                    integral.setActivity(0);
                }
            }

            integral.setCurr(integral.getCurr() + user.getIntCurrency());
            integral.setTotal(integral.getTotal() + user.getIntTotal());
            integral.setPick(integral.getPick() + user.getPickExchangeInt());
            integral.setRecycle(integral.getRecycle() + user.getRecycleExchangeInt());
            integral.setPut(integral.getPut() + user.getThrowExchangeInt());
            integral.setActivity(integral.getActivity() + user.getActivityExchangeInt());
        }

//                AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
//        String ticket = WeiXinQrUtil.getQrTicket(token.getAccessToken(), qrStr);
//        String qrUrl = WeiXinQrUtil.getQrURL(ticket);
        byte[] qrData = QRCreateUtil.createQR(phoneno);
        String qrStr = new StringBuilder("data:image/webp;base64,").append(Base64.getEncoder().encodeToString(qrData)).toString();

        PersonCenterMainOutput out = new PersonCenterMainOutput();
        out.setHeadPic(userInfo.getHeadimgurl());
        out.setUsername(userInfo.getNickname());
        out.setIntegral(integral);
        out.setQrPic(qrStr);
        out.setPhoneno(phoneno);
        out.setRole(roleBuilder.toString());

        //LOGGER.info("PersonCenterMainOutput-->" + JSONObject.fromObject(out).toString());
        return out;
    }
}
