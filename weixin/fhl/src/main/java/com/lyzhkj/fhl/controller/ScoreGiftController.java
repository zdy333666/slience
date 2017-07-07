/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.dto.ExchangedPage;
import com.lyzhkj.fhl.dto.GoodsPage;
import com.lyzhkj.fhl.pojo.GarProduct;
import com.lyzhkj.fhl.service.IntegralService;
import com.lyzhkj.fhl.service.ProductService;
import com.lyzhkj.fhl.service.UserService;
import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import java.io.IOException;
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
 * 积分相关
 *
 * @author breeze
 */
@Controller
public class ScoreGiftController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScoreGiftController.class);

    @Autowired
    private IntegralService integralService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    /**
     * 跳转至积分礼遇首页
     *
     * @param openId
     */
    @RequestMapping(value = "/integral", method = RequestMethod.GET)
    public void integralIndex(HttpServletResponse response, @RequestParam("code") String code) throws IOException {

        LOGGER.info("-------------- integralIndex ---------------------code:" + code);

        WebPageAccessToken webPageAccessToken = WeiXinUserUtil.getWebPageAccessToken(code);
        LOGGER.info("webPageAccessToken--->" + JSONObject.fromObject(webPageAccessToken));

        String openId = webPageAccessToken.getOpenid();

//        //检查用户是否已绑定
//        if (!userService.checkUserBind(openId)) {
//            response.sendRedirect("/bindpage.html?openId=" + openId);
//            return;
//        }
        int isBind = 0;
        if (userService.checkUserBind(openId)) {
            isBind = 1;
        }
        response.sendRedirect("/scoregift.html?openId=" + openId + "&isBind=" + isBind);
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/integral/goods", method = RequestMethod.GET)
    @ResponseBody
    public GoodsPage listGoods(String openId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        long st = System.currentTimeMillis();
        GoodsPage result = productService.list(openId, page, size);
        LOGGER.info("listGoods -- spend time : " + (System.currentTimeMillis() - st) + " ms");

        return result;
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/integral/goodsDetail", method = RequestMethod.GET)
    @ResponseBody
    public GarProduct goodsDetail(String openId, String productId) {

        return productService.findById(productId);
    }

    /**
     *
     * @param openId
     * @param productId
     * @param unitprice
     * @return
     */
    @RequestMapping(value = "/integral/exchange", method = RequestMethod.GET)
    @ResponseBody
    public int integralExchange(String openId, String productId, int unitprice) {

        LOGGER.info("/integral/exchange--openId -->" + openId);
        LOGGER.info("/integral/exchange--productId -->" + productId);
        LOGGER.info("/integral/exchange--integral -->" + unitprice);

        int currScore = userService.getTotalCurrencyScoreOfUser(openId);
        if (currScore < unitprice) {
            return -1;
        }

        boolean b = false;
        try {
            b = integralService.exchange(openId, productId, unitprice);
        } catch (SQLException ex) {
            LOGGER.error("", ex);
        }

        return b ? 1 : 0;
    }

    /**
     * 获取用户的兑换记录
     *
     * @param openId
     * @return
     */
    @RequestMapping(value = "/integral/listExchanged", method = RequestMethod.GET)
    @ResponseBody
    public ExchangedPage listExchanged(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam String openId) {

        LOGGER.info("/integral/listExchanged--openId -->" + openId);

        //LOGGER.info("/integral/listExchanged--result -->" + JSONArray.fromObject(result));
        return integralService.listExchanged(openId, page, size);
    }

    /**
     * 用户使用兑换的商品
     *
     * @param openId
     * @return
     */
    @RequestMapping(value = "/integral/makeExchangeUsed", method = RequestMethod.GET)
    @ResponseBody
    public boolean makeExchangeUsed(String openId, String id, String productId, String code) {

        LOGGER.info("/integral/makeExchangeUsed--openId -->" + openId);
        LOGGER.info("/integral/makeExchangeUsed--id -->" + id);
        LOGGER.info("/integral/makeExchangeUsed--productId -->" + productId);
        LOGGER.info("/integral/makeExchangeUsed--code -->" + code);

        return integralService.makeExchangeUsed(openId, id, productId, code);
    }
}
