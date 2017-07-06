/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.util.ToolsUtil;
import com.service.webservice.out.NoticeMessageResult;
import net.sf.json.JSONObject;
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
     * 生成并发送手机验证码
     *
     * @return
     */
    @RequestMapping(value = "/captcha/phone", method = RequestMethod.GET)
    @ResponseBody
    public String createPhoneCaptcha(@RequestParam String phoneno) {

        String captcha = ToolsUtil.createCaptcha();

        LOGGER.error("-- send captcha:" + captcha + " to " + phoneno);

        NoticeMessageResult result = ToolsUtil.sendSMS(phoneno, captcha);
        if (result == null || result.getCode() != 0) {
            LOGGER.error("-- send captcha:" + captcha + " to " + phoneno + " failed >>" + JSONObject.fromObject(result).toString());
        }

        return captcha;
    }
}
