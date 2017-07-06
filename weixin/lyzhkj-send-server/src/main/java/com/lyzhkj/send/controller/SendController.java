/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.send.controller;

import com.lyzhkj.send.dto.MailParam;
import com.lyzhkj.send.dto.SMSParam;
import com.lyzhkj.send.service.SendService;
import com.service.webservice.out.NoticeMessageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author breeze
 */
@Controller
public class SendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendController.class);

    @Autowired
    private SendService sendService;

    /**
     * 发送短信
     *
     * @param telNo
     * @param sms
     * @return
     */
    @RequestMapping(value = "send/sms", method = RequestMethod.POST)
    @ResponseBody
    public NoticeMessageResult sendSMS(@RequestBody SMSParam param) {

        LOGGER.info("--sms--telNo->" + param.getTelNo());
        LOGGER.info("--sms--sms->" + param.getSms());

        return sendService.sendSMS(param.getTelNo(), param.getSms());
    }

    /**
     * 发送邮件
     *
     * @param receiver
     * @param subject
     * @param content
     * @return
     */
    @RequestMapping(value = "send/mail", method = RequestMethod.POST)
    @ResponseBody
    public NoticeMessageResult sendMail(@RequestBody MailParam param) {

        LOGGER.info("--mail--receiver->" + param.getReceiver());
        LOGGER.info("--mail--subject->" + param.getSubject());
        LOGGER.info("--mail--content->" + param.getContent());

        return sendService.sendMail(param.getReceiver(), param.getSubject(), param.getContent());
    }

}
