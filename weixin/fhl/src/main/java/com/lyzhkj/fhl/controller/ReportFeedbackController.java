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
import com.lyzhkj.fhl.weixin.util.WeiXinMediaUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;
import com.lyzhkj.weixin.common.pojo.AccessToken;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
    @RequestMapping(value = "/report-feedback/upload", method = RequestMethod.POST)
    @ResponseBody
    public boolean upload(HttpServletRequest request) {

        String openId = request.getParameter("openId");
        String userId = request.getParameter("userId");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String imgIds = request.getParameter("imgIds");

        LOGGER.info("--------------------- upload openId -->" + openId);
        LOGGER.info("--------------------- upload userId -->" + userId);
        LOGGER.info("--------------------- upload title -->" + title);
        LOGGER.info("--------------------- upload content -->" + content);
        LOGGER.info("--------------------- upload imgIds -->" + imgIds);

        ReportInput input = new ReportInput();
        input.setUserId(userId);
        input.setTitle(title);
        input.setContent(content);

        AccessToken token = WeiXinAccessTokenUtil.getAccessToken();

        if (!(imgIds == null || imgIds.trim().isEmpty())) {
            //存储多张图片数据
            JSONArray imgs = new JSONArray();

            String[] imgIdsArray = imgIds.split(",");
            for (String imgId : imgIdsArray) {
                LOGGER.info("---------------------  imgId -->" + imgId);

                byte[] data = WeiXinMediaUtil.download(token.getAccessToken(), imgId);
                String imgBase64Str = Base64.getEncoder().encodeToString(data);

                LOGGER.info("---------------------  img -->" + Base64.getEncoder().encodeToString(data));

                imgs.add(new StringBuilder("data:image/webp;base64,").append(imgBase64Str).toString());
            }

            input.setImage(imgs.toString());
        }

        boolean b = reportFeedbackService.report(input);
        if (b) {
            String message = MessageFormat.format(FHLConst.INFO_REPORT_SUCCESS,
                    new SimpleDateFormat("yyyy年MM月dd日").format(new Date()),
                    title);

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
