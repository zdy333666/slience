/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.dto.ExchangeMachinePayDetail;
import com.lyzhkj.fhl.dto.ExchangeMachinePayParam;
import com.lyzhkj.fhl.pojo.GarCitizen;
import com.lyzhkj.fhl.pojo.GarUser;
import com.lyzhkj.fhl.service.ExchangeMachinePayService;
import com.lyzhkj.fhl.service.UserService;
import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
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
 *
 * @author breeze
 */
@Controller
public class ExchangeMachinePayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeMachinePayController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ExchangeMachinePayService exchangeMachinePayService;

    /**
     * 跳转至售货机扫码兑换首页
     *
     * @param openId
     */
    @RequestMapping(value = "exchangeMachinePay", method = RequestMethod.GET)
    public void exchangeMachinePay(HttpServletResponse response,
            @RequestParam("code") String code,
            @RequestParam("terminalNo") String terminalNo,
            @RequestParam("goodsId") String goodsId,
            @RequestParam("goodsName") String goodsName,
            @RequestParam("score") int score,
            @RequestParam("price") int price) throws IOException {

        WebPageAccessToken webPageAccessToken = WeiXinUserUtil.getWebPageAccessToken(code);
        LOGGER.info("webPageAccessToken--->" + JSONObject.fromObject(webPageAccessToken));

        String openId = webPageAccessToken.getOpenid();

        //检查用户是否已绑定
        int isBind = 0;
        if (userService.checkUserBind(openId)) {
            isBind = 1;
        } else {
            response.sendRedirect("/bindpage.html?openId=" + openId);
            return;
        }

        StringBuilder builder = new StringBuilder("/paydetail.html?");
        builder.append("isBind=").append(isBind);
        builder.append("&openId=").append(openId);
        builder.append("&terminalNo=").append(terminalNo);
        builder.append("&goodsId=").append(goodsId);
        builder.append("&goodsName=").append(URLEncoder.encode(goodsName, "UTF-8"));
        builder.append("&score=").append(score);
        builder.append("&price=").append(price);

        GarUser user = userService.findUserByOpenId(openId);
        if (user != null) {
            GarCitizen garCitizen = userService.findCitizenByPhone(user.getMobilephone());
            if (garCitizen != null) {
                builder.append("&userName=").append(garCitizen.getHostName());
            } else {
                builder.append("&userName=").append(URLEncoder.encode("联运知慧", "UTF-8"));
            }

            builder.append("&phoneNo=").append(user.getMobilephone());
        } else {
            builder.append("&userName=").append(URLEncoder.encode("联运知慧", "UTF-8"));
            builder.append("&phoneNo=").append("");
        }

        LOGGER.info("-- url:{}", builder.toString());

        response.sendRedirect(builder.toString());
    }

    /**
     *
     * @param openId
     * @param terminalNo
     * @param goodsId
     * @param score
     * @return
     */
    @RequestMapping(value = "exchangeMachinePay/detail", method = RequestMethod.GET)
    @ResponseBody
    public ExchangeMachinePayDetail detail(String openId, String terminalNo, String goodsId, int score) {

        LOGGER.info("-------------- exchangeMachinePay/detail ---------------------openId:" + openId);

        ExchangeMachinePayParam param = new ExchangeMachinePayParam();

        return exchangeMachinePayService.getPayDetail(param);
    }

    /**
     *
     * @param openId
     * @param score
     * @return
     */
    @RequestMapping(value = "exchangeMachinePay/pay", method = RequestMethod.GET)
    @ResponseBody
    public int pay(@RequestParam("openId") String openId,
            @RequestParam("terminalNo") String terminalNo,
            @RequestParam("goodsId") String goodsId,
            @RequestParam("score") int score,
            @RequestParam("tradeTime") long tradeTime) {

//        int currScore = userService.getTotalCurrencyScoreOfUser(openId);
//        if (currScore < score) {
//            return -1;
//        }
        ExchangeMachinePayParam param = new ExchangeMachinePayParam();
        param.setOpenId(openId);
        param.setTerminalNo(terminalNo);
        param.setGoodsId(goodsId);
        param.setScore(score);
        param.setTradeTime(tradeTime);

        LOGGER.info("--exchangeMachinePay/pay --param:{}", JSONObject.fromObject(param));

//        try {
//            return exchangeMachinePayService.pay(param);
//        } catch (SQLException ex) {
//            LOGGER.error("", ex);
//        }
        return 1;
    }

}
