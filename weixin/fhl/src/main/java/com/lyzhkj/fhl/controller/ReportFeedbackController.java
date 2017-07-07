/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.dto.ReportInput;
import com.lyzhkj.fhl.pojo.GarAccusation;
import com.lyzhkj.fhl.pojo.GarUser;
import com.lyzhkj.fhl.service.ReportFeedbackService;
import com.lyzhkj.fhl.service.UserService;
import com.lyzhkj.fhl.weixin.util.FHLConst;
import com.lyzhkj.fhl.weixin.util.WeiXinAccessTokenUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinKFMessageUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
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
 * 举报反馈
 *
 * @author breeze
 */
@Controller
public class ReportFeedbackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportFeedbackController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ReportFeedbackService reportFeedbackService;

    /**
     * 跳转到举报反馈首页
     *
     * @param request
     * @param code
     * @return
     */
    @RequestMapping(value = "report-feedback", method = RequestMethod.GET)
    public void index(HttpServletResponse response, @RequestParam("code") String code) throws IOException {

        LOGGER.info("-------------- report-feedback index---------------------code:" + code);

        WebPageAccessToken webPageAccessToken = WeiXinUserUtil.getWebPageAccessToken(code);
        LOGGER.info("webPageAccessToken--->" + JSONObject.fromObject(webPageAccessToken));

        GarUser garUser = userService.findUserByOpenId(webPageAccessToken.getOpenid());
        if (garUser == null) {
            response.sendRedirect("/bindpage.html?openId=" + webPageAccessToken.getOpenid());
            return;
        }

        if (reportFeedbackService.hasUnHandByUserId(garUser.getUserId())) {
            response.sendRedirect("/garbage.html?userId=" + garUser.getUserId());
            return;
        }

        response.sendRedirect("/report.html?openId=" + webPageAccessToken.getOpenid() + "&userId=" + garUser.getUserId());
    }

    /**
     *
     * @param openId
     * @param productId
     * @param unitprice
     * @return
     */
    @RequestMapping(value = "/report-feedback/upload", method = RequestMethod.GET)
    @ResponseBody
    public boolean upload(HttpServletRequest request) {

        String openId = request.getParameter("openId");
        String userId = request.getParameter("userId");
        String title = request.getParameter("title");
        String content = request.getParameter("content");

        LOGGER.info("--------------------- upload openId -->" + openId);
        LOGGER.info("--------------------- upload userId -->" + userId);
        LOGGER.info("--------------------- upload title -->" + title);
        LOGGER.info("--------------------- upload content -->" + content);

        ReportInput input = new ReportInput();
        input.setUserId(userId);
        input.setTitle(title);
        input.setContent(content);

        boolean b = reportFeedbackService.report(input);
        if (b) {
            String message = MessageFormat.format(FHLConst.INFO_REPORT_SUCCESS,
                    new SimpleDateFormat("yyyy年MM月dd日").format(new Date()),
                    title);
            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
            WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, message);
        }

        return b;
    }

    /**
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/report-feedback/detail", method = RequestMethod.GET)
    @ResponseBody
    public GarAccusation detail(@RequestParam String userId) {

        LOGGER.info("------- report-feedback detail----- userId:" + userId);

        long st = System.currentTimeMillis();
        GarAccusation result = reportFeedbackService.viewDetail(userId);
        LOGGER.info("spend time : " + (System.currentTimeMillis() - st) + " ms");
        //LOGGER.info("detail-->" + JSONObject.fromObject(result).toString());

        return result;
    }

}
