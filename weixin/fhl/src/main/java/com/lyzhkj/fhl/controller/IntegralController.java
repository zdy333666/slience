/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.service.IntegralService;
import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 积分相关
 *
 * @author breeze
 */
@Controller
public class IntegralController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegralController.class);

    @Autowired
    private IntegralService integralService;

    /**
     * 跳转至积分礼遇首页
     *
     * @param openId
     */
    @RequestMapping(value = "/integral", method = RequestMethod.GET)
    public void integralIndex(HttpServletResponse response, @RequestParam("code") String code) throws IOException {

        LOGGER.info("-------------- integral ---------------------code:" + code);

        WebPageAccessToken webPageAccessToken = WeiXinUserUtil.getAccessToken(code);

        LOGGER.info("webPageAccessToken--->" + JSONObject.fromObject(webPageAccessToken));

        response.sendRedirect("/scoregift.html?openId=" + webPageAccessToken.getOpenid());
    }

//    @RequestMapping(value = "/integral/{openId}", method = RequestMethod.GET)
//    public void listIntegral(@PathVariable String openId) {
//
//        integralService.listIntegral(openId);
//
//    }
}
