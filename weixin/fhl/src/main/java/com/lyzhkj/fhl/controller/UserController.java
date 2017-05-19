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
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "跳转用户绑定页面", notes = "")
    @ApiImplicitParam(name = "openId", value = "用户的openId", required = true, paramType = "query", dataType = "String")

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
    @ApiOperation(value = "用户绑定", notes = "执行用户绑定数据操作")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "openId", value = "用户的openId", required = true, paramType = "query", dataType = "String")
        ,
        @ApiImplicitParam(name = "username", value = "用户名称", required = true, paramType = "query", dataType = "String")
        ,
        @ApiImplicitParam(name = "phoneno", value = "用户手机号码", required = true, paramType = "query", dataType = "String")
    })

    @RequestMapping(value = "/user-bind/add", method = RequestMethod.GET)
    @ResponseBody
    public boolean userBind(@RequestParam String openId, @RequestParam String username, @RequestParam String phoneno) {

        LOGGER.info("userBind--openId-->" + openId);
        LOGGER.info("userBind--username-->" + username);
        LOGGER.info("userBind--phoneno-->" + phoneno);

        UserBindInput input = new UserBindInput();
        input.setOpenId(openId);
        input.setUsername(username);
        input.setPhoneno(phoneno);

        //检查用户是否已绑定
        if (userService.checkUserBind(openId)) {
            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
            String message = UserBindHelper.buildUserBindRepeatNotifyMessage(openId);
            WeiXinKFUtil.sendMessage(token.getAccessToken(), message);
            return true;
        }
        //
        if (userService.userBind(input)) {
            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
            String message = UserBindHelper.buildUserBindSuccessNotifyMessage(openId);
            WeiXinKFUtil.sendMessage(token.getAccessToken(), message);
        }

        return true;
    }

}
