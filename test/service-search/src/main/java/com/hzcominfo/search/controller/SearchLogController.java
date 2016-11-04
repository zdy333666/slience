/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.controller;

import static com.hzcominfo.search.controller.SearchController.GetMongo;
import com.hzcominfo.search.log.SeekSearchLogService;
import com.hzcominfo.search.service.SearchService;
import com.hzcominfo.search.pojo.SearchInput;
import com.hzcominfo.search.service.SearchLogService;
import com.mongodb.DB;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author zdy
 */
@Controller
public class SearchLogController {

    private final Logger logger = LoggerFactory.getLogger(SearchLogController.class);

   
     // 查询搜索日志
    @RequestMapping("/searchLog.htm")
    protected void handleSearchLogRequest(HttpServletRequest request, HttpServletResponse response,
            @RequestBody String body) throws Exception {
        // url参数 无
        // 返回json数据

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        DB db = GetMongo(1, getMongoConnInfoSearch());
        db.requestStart();

        JsonNode resultNode = com.hzcominfo.search.log.SearchLogService.service(request, db, body);

        db.requestDone();

        String resultStr = null;

        if (resultNode != null) {
            resultStr = resultNode.toString();
        }

        response.getWriter().write(resultStr);
    }

    // 查询存在的相同的搜索日志
    @RequestMapping("/searchSameLog.htm")
    protected void handleSearchSameLogRequest(HttpServletRequest request, HttpServletResponse response,
            @RequestBody String body) throws Exception {
        // url参数 无
        // 返回json数据

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        DB db = GetMongo(1, getMongoConnInfoSearch());
        db.requestStart();

        JsonNode resultNode = SeekSearchLogService.service(db, body);

        db.requestDone();

        String resultStr = null;

        if (resultNode != null) {
            resultStr = resultNode.toString();
        }

        response.getWriter().write(resultStr);
    }

    // 根据身份证号码查询flag=1的user信息记录
    @RequestMapping("/queryUserBySFZH.htm")
    protected void handleQueryUserBySFZHRequest(HttpServletRequest request, HttpServletResponse response,
            @RequestBody String body) throws Exception {
        // url参数 无
        // 返回json数据

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        DB db = GetMongo(1, getMongoConnInfoSearch());
        db.requestStart();

        JsonNode resultNode = QueryUserService.service(db, body);

        db.requestDone();

        String resultStr = null;

        if (resultNode != null) {
            resultStr = resultNode.toString();
        }

        response.getWriter().write(resultStr);
    }

}
