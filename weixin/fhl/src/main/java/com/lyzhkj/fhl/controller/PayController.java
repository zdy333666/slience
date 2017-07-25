/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.async.PayResultSender;
import com.lyzhkj.fhl.helper.PayHelper;
import com.lyzhkj.fhl.pojo.DbResult;
import com.lyzhkj.fhl.pojo.GarCashier;
import com.lyzhkj.fhl.pojo.GarCitizen;
import com.lyzhkj.fhl.service.GarCitizenService;
import com.lyzhkj.fhl.service.PayService;
import com.lyzhkj.fhl.service.UserService;
import com.lyzhkj.fhl.weixin.util.SignUtil;
import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
public class PayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private GarCitizenService garCitizenService;

    @Autowired
    private UserService userService;

    @Autowired
    private PayService payService;

    @Autowired
    private PayResultSender payResultSender;

    /**
     * 跳转到扫码付款首页
     *
     * @param request
     * @param code
     * @return
     */
    @RequestMapping(value = "pay", method = RequestMethod.GET)
    public void index(HttpServletResponse response, @RequestParam(value = "code") String code) throws IOException {

        LOGGER.info("-------------- pay index---------------------code:" + code);

        WebPageAccessToken webPageAccessToken = WeiXinUserUtil.getWebPageAccessToken(code);
        LOGGER.info("webPageAccessToken--->" + JSONObject.fromObject(webPageAccessToken));
        String openId = webPageAccessToken.getOpenid();

        StringBuilder urlSearchBuilder = new StringBuilder("openId=").append(openId);

        //判断是否是收银员
        if (!userService.checkUserIsCashierByOpenId(openId)) {
            urlSearchBuilder.append("&windowclose=0");

//            AccessToken token = WeiXinAccessTokenUtil.getAccessToken();
//            //不是收银员，提示一下
//            if (userService.checkUserIsMerchantByOpenId(openId)) { //检查是否是商户
//                WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, FHLConst.WARN_NO_ROLE_3);
//            } else {
//                WeiXinKFMessageUtil.replyTextMessage(token.getAccessToken(), openId, MessageFormat.format(FHLConst.WARN_NO_ROLE_2, FHLConst.CASHIER_USERTYPE_3));
//            }
        } else {
            urlSearchBuilder.append("&windowclose=1");
        }

        response.sendRedirect("/sjReceiveMain.html?" + urlSearchBuilder.toString());
    }

    /**
     * 扫码后获取用户信息
     *
     * @param scanRsValue
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "pay/getUserInfo", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getUserInfo(String scanRsValue) throws IOException {

        LOGGER.info("-------------- pay/getUserInfo--scanRsValue:" + scanRsValue); //扫码日志记录

        Map<String, Object> result = new HashMap<>();
        try {
            //根据扫码结果，获得业主信息			
            GarCitizen garCitizen = garCitizenService.findGarCitizenByQR(scanRsValue);
            if (garCitizen == null) {
                throw new Exception("getUserInfo_ERR scanRsValue:" + scanRsValue);
            } else {
                //用户信息
                Map<String, Object> userinfo = new HashMap<>();
                userinfo.put("username", garCitizen.getHostName());
                userinfo.put("useraddr", garCitizen.getHostNo());
                userinfo.put("usertel", garCitizen.getMobilephone());
                userinfo.put("userscoreShow", new DecimalFormat("#,###").format(garCitizen.getEffectiveScore()));
                userinfo.put("userscore", garCitizen.getEffectiveScore());
                userinfo.put("citizenId", garCitizen.getCitizenId());
                //返回页面json
                result.put("code", "ok");
                result.put("userinfo", userinfo);

                LOGGER.info("-------------- pay/getUserInfo--result:" + result);
                return result;
            }
        } catch (Exception e) {
            LOGGER.error("", e);
            result.put("code", "fail");
        }

        return result;
    }

    /**
     * 兑换确认画面的立即兑换
     *
     * @param scanRsValue
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "paynow", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> paynow(String citizenId, int payScore, String storeopenid, HttpServletResponse resp) throws IOException {

        LOGGER.info("-------------- paynow--citizenId:" + citizenId);
        LOGGER.info("-------------- paynow--payScore:" + payScore);
        LOGGER.info("-------------- paynow--storeopenid:" + storeopenid);

        //测试用
        //storeopenid = "oZ14ywqeew_QCzl53QP4S6il9oww";
        Map<String, Object> obj = new HashMap<>();
        Map<String, Integer> scoreMap = null;
        Long startTime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            //根据业主的openid获得业主情报
            GarCitizen garCitizen = garCitizenService.findGarCitizenById(citizenId);
            if (garCitizen == null) {
                throw new Exception("paynow_ERR citizenId:" + citizenId);
            }

            //根据收银员的openid获得收银员情报
            GarCashier garCashier = userService.findGarCashierByOpenId(storeopenid);
            if (garCashier == null) {
                throw new Exception("paynow_ERR storeopenid:" + storeopenid);
            }

            //积分计算（根据输入的总积分，分配到各个项目上，如果无法分配，则报错）
            scoreMap = PayHelper.getScore(garCitizen, payScore);

            //调用存储过程，进行积分兑换			
            DbResult dbResult = payService.runBatch_insert_storeExchange(garCitizen.getCitizenId(),
                    scoreMap.get("throwExchange"), scoreMap.get("recycleExchange"), scoreMap.get("pickExchange"),
                    garCashier.getUserId());
            if (dbResult == null || dbResult.errCode != 0) {
                String message = "兑换失败 业主id:" + citizenId
                        + " 兑换积分:" + payScore
                        + " 兑换投放积分:" + scoreMap.get("throwExchange")
                        + " 兑换回收积分:" + scoreMap.get("recycleExchange")
                        + " 兑换巡检积分:" + scoreMap.get("pickExchange")
                        + " 兑换时间:" + sdf.format(new Date());
                LOGGER.debug(garCashier.getMerchantsNum(), message);
                throw new Exception();
            }
            //更新业主缓存
            //garCitizen = CacheManager.instance().refeshGarCitizenById(garCitizen.citizenId);

            obj.put("code", "ok");
            obj.put("usedScore", new DecimalFormat("#,###").format(payScore));
            obj.put("lastScore", new DecimalFormat("#,###").format(garCitizen.getEffectiveScore() - payScore));

            //结果相关 可以考虑扔到线程池里去处理（因为太慢需要3秒） 
            payResultSender.send(garCashier, garCitizen, payScore);
            String message = "兑换成功 业主id:" + citizenId
                    + " 兑换积分:" + payScore
                    + " 兑换投放积分:" + scoreMap.get("throwExchange")
                    + " 兑换回收积分:" + scoreMap.get("recycleExchange")
                    + " 兑换巡检积分:" + scoreMap.get("pickExchange")
                    + " 兑换时间:" + sdf.format(new Date());
            LOGGER.debug(garCashier.getMerchantsNum(), message);
        } catch (Exception e) {
            LOGGER.error("", e);	//兑换异常日志
            obj.put("code", "fail");
        } finally {
            System.out.println("paynow(Cost Time):" + (System.currentTimeMillis() - startTime) + "ms");
        }
        return obj;
    }

}
