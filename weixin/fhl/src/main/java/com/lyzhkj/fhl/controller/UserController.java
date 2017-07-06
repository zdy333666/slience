/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.dto.UserBindInput;
import com.lyzhkj.fhl.helper.UserBindHelper;
import com.lyzhkj.fhl.service.UserService;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinKFUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 用户相关
 *
 * @author breeze
 */
@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * 跳转用户绑定页面
     *
     * @param openId
     * @param username
     * @param phoneno
     * @return
     */
    @RequestMapping(value = "/user-bind", method = RequestMethod.GET)
    public void userBindIndex(HttpServletResponse response, @RequestParam("openId") String openId) throws IOException {
        LOGGER.info("-------------- user-bind index---------------------openId:" + openId);

        response.sendRedirect("/bindpage.html?openId=" + openId);
    }

    /**
     * 执行用户绑定数据操作
     *
     * @param openId
     * @param username
     * @param phoneno
     * @return
     */
    @RequestMapping(value = "/user-bind/add", method = RequestMethod.GET)
    @ResponseBody
    public int userBind(@RequestParam String openId, @RequestParam String phoneno, @RequestParam String region) {//, @RequestParam String roleType) {

        LOGGER.info("userBind--openId-->" + openId);
        LOGGER.info("userBind--phoneno-->" + phoneno);
        LOGGER.info("userBind--region-->" + region);

        UserBindInput input = new UserBindInput();
        input.setOpenId(openId);
        input.setPhoneno(phoneno);
        input.setRegion(region);

        int n = 0;

        //检查用户是否已绑定
        if (userService.checkUserBind(openId)) {
            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
            String message = UserBindHelper.buildBindUserRepeatNotifyMessage(openId);
            WeiXinKFUtil.sendMessage(token.getAccessToken(), message);
            return 1;
        }

        //
        n = userService.bindUser(input);
        if (n == 1) {
            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
            String message = UserBindHelper.buildBindUserSuccessNotifyMessage(openId);
            WeiXinKFUtil.sendMessage(token.getAccessToken(), message);
        }

        return n;
    }

    /**
     * 用户解绑
     *
     * @param openId
     * @return
     */
    @RequestMapping(value = "/user-bind/logoff", method = RequestMethod.GET)
    @ResponseBody
    public boolean userLogoff(@RequestParam String openId) {

        LOGGER.info("userLogoff--openId-->" + openId);

        return userService.userLogoff(openId);
    }

    /**
     * 用户更换绑定的手机号码
     *
     * @param openId
     * @return
     */
    @RequestMapping(value = "/user-bind/changephone", method = RequestMethod.GET)
    @ResponseBody
    public int changeBindPhone(@RequestParam String openId, @RequestParam String phoneno) {

        LOGGER.info("changephone--openId-->" + openId);
        LOGGER.info("changephone--phoneno-->" + phoneno);

        return userService.changeBindPhone(openId, phoneno);
    }

}
