/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.weixin.util.WeiXinSignatureCheckUtil;
import com.lyzhkj.fhl.util.FHLUtil;
import com.lyzhkj.fhl.weixin.util.FHLConst;
import com.lyzhkj.fhl.weixin.util.SignUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinMessageUtil;
import com.lyzhkj.weixin.common.pojo.ReplyTextMessage;
import com.lyzhkj.weixin.common.util.WeiXinMessageConst;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 与微信服务器交互
 *
 * @author breeze
 */
@Controller
@RequestMapping("WeixinServer")
public class WeiXinController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WeiXinController.class);

    @Autowired
    private WeiXinRequestDispatcher weiXinRequestDispatcher;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public String getPage(String signature, String timestamp,
            String nonce, String echostr) {
//@RequestParam("signature")

        LOGGER.info("signature:" + signature);
        LOGGER.info("timestamp:" + timestamp);
        LOGGER.info("nonce:" + nonce);
        LOGGER.info("echostr:" + echostr);

        LOGGER.info("get hello----------------------");

        if (WeiXinSignatureCheckUtil.checkSignature(signature, timestamp, nonce)) {
            //原样返回echostr参数内容，接入生效
            return echostr;
        }

        return "";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public void postPage(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException, IOException {//,@RequestBody String body) {
        //request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();

        Map<String, String> body = null;
        try {
            InputStream ins = request.getInputStream();
            body = FHLUtil.xmlToMap(ins);
            ins.close();
        } catch (IOException | DocumentException ex) {
            LOGGER.error("", ex);
        }

        LOGGER.info("body-->" + body);
        if (body == null || body.isEmpty()) {
            return;
        }

        //直接回复 防止重发 消息由客服自动下发
        writer.write("");
        writer.flush();

        try {
            weiXinRequestDispatcher.dispatch(body);
        } catch (Exception e) {
            LOGGER.error("", e);

            ReplyTextMessage message = new ReplyTextMessage();
            message.setFromUserName(body.get("ToUserName"));
            message.setToUserName(body.get("FromUserName"));
            message.setMsgType(WeiXinMessageConst.MESSAGE_TEXT);
            message.setContent(FHLConst.ERR_SYSTEM);
            message.setCreateTime(System.currentTimeMillis());

            String replyMsg = WeiXinMessageUtil.messageToXml(message);
            writer.write(replyMsg);
        }

        writer.close();
    }

    /**
     * 扫码权限开通
     *
     * @param apiRoll
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "/apiRoll", method = RequestMethod.POST)
    @ResponseBody
    public String apiRoll(String apiRoll) throws IOException {

        LOGGER.info("-------------- weixin apiRoll:" + apiRoll);

        return SignUtil.sign(apiRoll);
    }

}
