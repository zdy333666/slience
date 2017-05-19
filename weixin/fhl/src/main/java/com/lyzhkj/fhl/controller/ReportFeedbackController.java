/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 举报反馈
 *
 * @author breeze
 */
@Controller
public class ReportFeedbackController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportFeedbackController.class);

    /**
     * 跳转到举报反馈首页
     *
     * @param request
     * @param code
     * @return
     */
    @ApiOperation(value = "跳转到举报反馈首页", notes = "")
    @ApiImplicitParam(name = "code", value = "用户访问公众号的网页授权Code", required = true, paramType = "query", dataType = "String")

    @RequestMapping(value = "report-feedback", method = RequestMethod.GET)
    public void index(HttpServletResponse response, @RequestParam("code") String code) throws IOException {

        LOGGER.info("-------------- report-feedback index---------------------code:" + code);

        WebPageAccessToken webPageAccessToken = WeiXinUserUtil.getAccessToken(code);

        LOGGER.info("webPageAccessToken--->" + JSONObject.fromObject(webPageAccessToken));

        response.sendRedirect("/report.html?openId=" + webPageAccessToken.getOpenid());
    }

}
