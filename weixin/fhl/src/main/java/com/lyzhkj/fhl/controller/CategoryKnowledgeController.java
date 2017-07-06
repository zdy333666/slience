/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.pojo.GarKnowledge;
import com.lyzhkj.fhl.service.CategoryKnowledgeService;
import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import java.io.IOException;
import java.util.List;
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
 * 分类信息
 *
 * @author breeze
 */
@Controller
public class CategoryKnowledgeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryKnowledgeController.class);

    @Autowired
    private CategoryKnowledgeService categoryKnowledgeService;

    /**
     * 跳转到分类知识库首页
     *
     * @param request
     * @param code
     * @return
     */
    @RequestMapping(value = "category-repository", method = RequestMethod.GET)
    public void knowlegeIndex(HttpServletResponse response, @RequestParam(value = "code", required = false) String code) throws IOException {

        LOGGER.info("-------------- category-repository index---------------------code:" + code);

        String openId = "";
        if (code != null) {
            WebPageAccessToken webPageAccessToken = WeiXinUserUtil.getWebPageAccessToken(code);
            LOGGER.info("webPageAccessToken--->" + JSONObject.fromObject(webPageAccessToken));
            openId = webPageAccessToken.getOpenid();
        }

        response.sendRedirect("/knowlege.html?openId=" + openId);
    }

    /**
     * 获取分类知识文章列表
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "category-repository/list", method = RequestMethod.GET)
    @ResponseBody
    public List<GarKnowledge> listRepository() {

        return categoryKnowledgeService.list();
    }

    /**
     * 根据关键字搜索分类知识文章
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "category-repository/search", method = RequestMethod.GET)
    @ResponseBody
    public GarKnowledge repositorySearch(@RequestParam("word") String word) {

        return categoryKnowledgeService.search(word);
    }

}
