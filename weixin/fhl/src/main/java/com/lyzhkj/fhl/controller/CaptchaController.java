/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.util.ToolsUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 获取验证码
 *
 * @author breeze
 */
@Controller
public class CaptchaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CaptchaController.class);

    /**
     * 获取手机验证码
     *
     * @return
     */
    @ApiOperation(value = "获取手机验证码", notes = "")
    @ApiImplicitParam(name = "phoneno", value = "接收验证码的手机号码", required = true, paramType = "query", dataType = "String")

    @RequestMapping(value = "/captcha/phone", method = RequestMethod.GET)
    @ResponseBody
    public String createPhoneCaptcha(@RequestParam String phoneno) {

        String captcha = ToolsUtil.createCaptcha();
//        NoticeMessageResult result = ToolsUtil.sendSMS(phoneno, captcha);
//        if (result == null || result.getCode() != 0) {
//            LOGGER.error("--- 发送短信验证码至手机" + phoneno + "失败！");
//        }

        return captcha;
    }
}
