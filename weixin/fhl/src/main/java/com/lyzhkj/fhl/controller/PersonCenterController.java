/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.dto.PersonCenterMainOutput;
import com.lyzhkj.fhl.pojo.GarRole;
import com.lyzhkj.fhl.pojo.GarUser;
import com.lyzhkj.fhl.pojo.IntegralOverview;
import com.lyzhkj.fhl.service.IntegralService;
import com.lyzhkj.fhl.service.RoleService;
import com.lyzhkj.fhl.service.UserService;
import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import java.io.IOException;
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
    private IntegralService integralService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "person-center", method = RequestMethod.GET)
    public void personCenterIndex(HttpServletResponse response, @RequestParam("code") String code) throws IOException {

        LOGGER.info("-------------- person-center index---------------------code:" + code);

        WebPageAccessToken webPageAccessToken = WeiXinUserUtil.getAccessToken(code);
        LOGGER.info("webPageAccessToken--->" + JSONObject.fromObject(webPageAccessToken));

        String openId = webPageAccessToken.getOpenid();

        //检查用户是否已绑定
//        if (!userService.checkUserBind(openId)) {
//            response.sendRedirect("/bindpage.html?openId=" + openId);
//            return;
//        }
        response.sendRedirect("/personalcenter.html?openId=" + openId);

    }

    @RequestMapping(value = "person-center/main", method = RequestMethod.GET)
    @ResponseBody
    public PersonCenterMainOutput listMainInfo(@RequestParam String openId) {
        LOGGER.info("-------------- person-center/main ---------------------openId:" + openId);

        IntegralOverview integral = integralService.getIntegralOverview(openId);
        GarUser user = userService.findUserByOpenId(openId);
        List<GarRole> roles = roleService.findRolesByUserId(user.getUserId());

        PersonCenterMainOutput out = new PersonCenterMainOutput();
        out.setHeadPic("");
        out.setUsername("");
        out.setIntegral(integral);
        out.setQrPic("");
        out.setPhoneno("");
        out.setRole("");

        return null;
    }
}
