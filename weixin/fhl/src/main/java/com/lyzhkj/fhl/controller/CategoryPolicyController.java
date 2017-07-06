/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.dto.PolicyPage;
import com.lyzhkj.fhl.pojo.CategoryArticleDetail;
import com.lyzhkj.fhl.pojo.GarUser;
import com.lyzhkj.fhl.service.CategoryPolicyService;
import com.lyzhkj.fhl.service.UserService;
import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import com.lyzhkj.weixin.common.pojo.WeiXinUserBaseInfo;
import java.io.IOException;
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
public class CategoryPolicyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryPolicyController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryPolicyService categoryPolicyService;

    /**
     * 跳转到分类政策首页
     *
     * @param request
     * @param code
     * @return
     */
    @RequestMapping(value = "category-policy", method = RequestMethod.GET)
    public void index(HttpServletResponse response, @RequestParam(value = "code", required = true) String code) throws IOException {

        LOGGER.info("-------------- category-repository index---------------------code:" + code);

        WebPageAccessToken webPageAccessToken = WeiXinUserUtil.getWebPageAccessToken(code);
        LOGGER.info("webPageAccessToken--->" + JSONObject.fromObject(webPageAccessToken));
        String openId = webPageAccessToken.getOpenid();

        //-----------------------------
        response.sendRedirect("/articallist.html?openId=" + openId);
    }

    /**
     * 获取分类政策文章列表
     *
     * @param request
     * @param openId
     * @return
     */
    @RequestMapping(value = "category-policy/list", method = RequestMethod.GET)
    @ResponseBody
    public PolicyPage listPolicy(@RequestParam String openId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        String city = null;

        //long st = System.currentTimeMillis();
        GarUser user = userService.findUserByOpenId(openId);
        if (user != null) {
            city = user.getCity();
        } else {
            WeiXinUserBaseInfo userInfo = WeiXinUserUtil.getWeiXinUserBaseInfo(openId);
            LOGGER.info("weixin-userInfo-->" + JSONObject.fromObject(userInfo).toString());

            city = userInfo.getCity();
        }
        if (city == null || city.trim().isEmpty()) {
            city = "杭州";
        }

        PolicyPage result = categoryPolicyService.listPolicy(city, page, size);
        //LOGGER.info("spend time : " + (System.currentTimeMillis() - st) + " ms");

        return result;
    }

    /**
     * 获取分类政策文章详情
     *
     * @param request
     * @param openId
     * @return
     */
    @RequestMapping(value = "category-policy/detail", method = RequestMethod.GET)
    @ResponseBody
    public CategoryArticleDetail policyDetail(long id) {

        //long st = System.currentTimeMillis();
        CategoryArticleDetail result = categoryPolicyService.policyDetail(id);
        //LOGGER.info("spend time : " + (System.currentTimeMillis() - st) + " ms");

        return result;
    }

}
