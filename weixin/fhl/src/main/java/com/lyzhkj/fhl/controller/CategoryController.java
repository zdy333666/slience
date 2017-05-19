/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.controller;

import com.lyzhkj.fhl.pojo.CategoryArticleDetail;
import com.lyzhkj.fhl.pojo.CategoryArticleIntro;
import com.lyzhkj.fhl.service.CategoryPolicyService;
import com.lyzhkj.fhl.service.CategoryRepositoryService;
import com.lyzhkj.fhl.weixin.util.WeiXinUserUtil;
import com.lyzhkj.weixin.common.pojo.WebPageAccessToken;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
public class CategoryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryRepositoryService categoryRepositoryService;

    @Autowired
    private CategoryPolicyService categoryPolicyService;

    /**
     * 跳转到分类知识库首页
     *
     * @param request
     * @param code
     * @return
     */
    @ApiOperation(value = "跳转到分类知识库首页", notes = "")
    @ApiImplicitParam(name = "code", value = "用户访问公众号的网页授权Code", required = true, paramType = "query", dataType = "String")
    @RequestMapping(value = "category-repository", method = RequestMethod.GET)
    public void knowlegeIndex(HttpServletResponse response, @RequestParam("code") String code) throws IOException {

        LOGGER.info("-------------- category-repository index---------------------code:" + code);

        WebPageAccessToken webPageAccessToken = WeiXinUserUtil.getAccessToken(code);
        LOGGER.info("webPageAccessToken--->" + JSONObject.fromObject(webPageAccessToken));

        response.sendRedirect("/knowlege.html?openId=" + webPageAccessToken.getOpenid());
    }

    /**
     * 根据类别获取分类知识文章简介列表
     *
     * @param request
     * @param openId
     * @return
     */
    @ApiOperation(value = "根据类别获取分类知识文章简介列表", notes = "")
    @ApiImplicitParam(name = "categoryId", value = "分类知识文章类别ID", required = true, paramType = "query", dataType = "int")
    @RequestMapping(value = "category-repository/category", method = RequestMethod.GET)
    @ResponseBody
    public List<CategoryArticleIntro> findRepositoryByCategoryId(@RequestParam("categoryId") int categoryId) {

        return categoryRepositoryService.findByCategoryId(categoryId);
    }

    /**
     * 获取分类知识文章详情
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "获取分类知识文章详情", notes = "")
    @ApiImplicitParam(name = "id", value = "分类知识文章ID", required = true, paramType = "query", dataType = "long")
    @RequestMapping(value = "category-repository/detail", method = RequestMethod.GET)
    @ResponseBody
    public CategoryArticleDetail repositoryDetail(@RequestParam("id") long id) {

        return categoryRepositoryService.knowledgeDetail(id);
    }

    /**
     * 根据关键字搜索分类知识文章
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据关键字搜索分类知识文章", notes = "")
    @ApiImplicitParam(name = "word", value = "搜索关键字", required = true, paramType = "query", dataType = "String")
    @RequestMapping(value = "category-repository/search", method = RequestMethod.GET)
    @ResponseBody
    public List<CategoryArticleIntro> repositorySearch(@RequestParam("word") String word) {

        return categoryRepositoryService.search(word);
    }

    //**************************************************************************
    /**
     * 跳转到分类政策首页
     *
     * @param request
     * @param code
     * @return
     */
    @ApiOperation(value = "跳转到分类政策首页", notes = "")
    @ApiImplicitParam(name = "code", value = "用户访问公众号的网页授权Code", required = true, paramType = "query", dataType = "String")
    @RequestMapping(value = "category-policy", method = RequestMethod.GET)
    public void policyIndex(HttpServletResponse response, @RequestParam("code") String code) throws IOException {

        LOGGER.info("-------------- category-policy index---------------------code:" + code);

        WebPageAccessToken webPageAccessToken = WeiXinUserUtil.getAccessToken(code);
        LOGGER.info("webPageAccessToken--->" + JSONObject.fromObject(webPageAccessToken));

        response.sendRedirect("/articallist.html?openId=" + webPageAccessToken.getOpenid());
    }

    /**
     * 获取分类政策简介列表
     *
     * @param request
     * @param openId
     * @return
     */
    @ApiOperation(value = "获取分类政策简介列表", notes = "")
    @RequestMapping(value = "category-policy/list", method = RequestMethod.GET)
    @ResponseBody
    public Object policyList() {

        return categoryPolicyService.listPolicy();
    }

    /**
     * 获取分类政策文章详情
     *
     * @param request
     * @param openId
     * @return
     */
    @ApiOperation(value = "获取分类政策文章详情", notes = "")
    @ApiImplicitParam(name = "id", value = "分类政策文章ID", required = true, dataType = "long")
    @RequestMapping(value = "category-policy/detail", method = RequestMethod.GET)
    @ResponseBody
    public Object policyDetail(@RequestParam("id") long id) {

        return categoryPolicyService.policyDetail(id);
    }

//    @RequestMapping(value = "category-detail", method = RequestMethod.GET)
//    public String detailIndex(HttpServletRequest request, @RequestParam("code") String code) {
//
//        LOGGER.info("-------------- category-detail ---------------------code:" + code);
//
//        WebPageAccessToken webPageAccessToken = WeiXinUserUtil.getAccessToken(code);
//        LOGGER.info("webPageAccessToken--->" + JSON.toJSON(webPageAccessToken));
//
//        return "/category-detail.html?openId=" + webPageAccessToken.getOpenid();
//    }
//
//    @RequestMapping(value = "detail/{openId}", method = RequestMethod.GET)
//    @ResponseBody
//    public Object detail(HttpServletRequest request, @PathVariable("openId") String openId) {
//
//        return "category detail";
//    }
}
