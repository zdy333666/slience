/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabaggr.controller;

import com.hzcominfo.gabaggr.conf.GABAggrConfiguration;
import com.hzcominfo.gabaggr.conf.MongoSource;
import com.hzcominfo.gabaggr.conf.MongoSourceFactory;
import com.hzcominfo.gabaggr.dao.AuthQuery;
import com.hzcominfo.gabaggr.pojo.SubmitInput;
import com.hzcominfo.gabaggr.service.GABAggrService;
import com.hzcominfo.gabaggr.service.SubmitQueue;
import com.mongodb.DB;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.codehaus.jackson.map.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zdy
 */
@Controller
public class GABAggrController {

    private final Logger logger = LoggerFactory.getLogger(GABAggrController.class);

    @Autowired
    private GABAggrService _GABAggrService;

    @Autowired
    private AuthQuery _AuthQuery;

    /**
     * 获取全国人口信息
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "meta", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> handMeta(@RequestParam(required = true) String dimension, @RequestParam(required = false) String type) {

        if (type == null) {
            return _GABAggrService.queryServicesetOfDimension(dimension);
        } else if ("region".equals(type)) {
            return _GABAggrService.queryServiceOfDimensionGroupByRegion(dimension);
        }

        return null;
    }

    /**
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "submit", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> handSubmit(@RequestParam(required = true) String param) {

        Map<String, Object> result = new HashMap<>();

        try {
            SubmitInput input = new ObjectMapper().readValue(param, SubmitInput.class);

            MongoSource source = MongoSourceFactory.getSource(GABAggrConfiguration.mongo_uri_rtdb);
            DB db = source.getClient().getDB(source.getClientURI().getDatabase());

            String userId = input.getUserId();
            Map<String, Object> userInfo = _AuthQuery.getUserInfo(db, userId);
            if (userInfo == null) {
                result.put("resultCode", -1);
                result.put("resultMsg", "请求失败，用户信息不存在");
                return result;
            }

            input.setUserCardId((String) userInfo.get("pkiSfzh"));
            input.setUserName((String) userInfo.get("pkiName"));
            input.setUserDept((String) userInfo.get("userDeptCode"));
            input.setSessionId(UUID.randomUUID().toString());

            if (SubmitQueue.offer(input)) {
                result.put("resultCode", 1);
                result.put("resultMsg", "请求成功，会话已建立");
                result.put("sessionId", input.getSessionId());
            } else {
                result.put("resultCode", -2);
                result.put("resultMsg", "请求失败，当前服务器繁忙,请稍后尝试");
                result.put("sessionId", input.getSessionId());
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            result.put("resultCode", -1);
            result.put("resultMsg", "请求失败");
        }

        return result;
    }

    /**
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> handSubmit(@RequestBody SubmitInput input) {

        Map<String, Object> result = new HashMap<>();

        try {
            MongoSource source = MongoSourceFactory.getSource(GABAggrConfiguration.mongo_uri_rtdb);
            DB db = source.getClient().getDB(source.getClientURI().getDatabase());

            String userId = input.getUserId();
            Map<String, Object> userInfo = _AuthQuery.getUserInfo(db, userId);
            if (userInfo == null) {
                result.put("resultCode", -1);
                result.put("resultMsg", "请求失败，用户信息不存在");
                return result;
            }

            input.setUserCardId((String) userInfo.get("pkiSfzh"));
            input.setUserName((String) userInfo.get("pkiName"));
            input.setUserDept((String) userInfo.get("userDeptCode"));
            input.setSessionId(UUID.randomUUID().toString());

            if (SubmitQueue.offer(input)) {
                result.put("resultCode", 1);
                result.put("resultMsg", "请求成功，会话已建立");
                result.put("sessionId", input.getSessionId());
            } else {
                result.put("resultCode", -2);
                result.put("resultMsg", "请求失败，当前服务器繁忙,请稍后尝试");
                result.put("sessionId", input.getSessionId());
            }
        } catch (Exception ex) {
            logger.error(null, ex);
            result.put("resultCode", -1);
            result.put("resultMsg", "请求失败");
        }

        return result;
    }

    /**
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "fetch", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> handFetch(@RequestParam String sessionId) {

        Map<String, Object> result = new HashMap<>();
        result.put("sessionId", sessionId);
        try {

            Map<String, Object> fetchData = _GABAggrService.fetch(sessionId);
            if (fetchData == null) {
                result.put("resultCode", 1);
                result.put("resultMsg", "请求尚未处理");
                result.put("dealedCount", 0);
                result.put("rows", new ArrayList<>());
            } else {
                boolean ok = (boolean) fetchData.get("ok");
                //int size = (int) fetchData.get("size");
                int dealed = (int) fetchData.get("dealed");
                Collection<Map<String, Object>> items = (Collection<Map<String, Object>>) fetchData.get("items");
                if (ok) {
                    result.put("resultCode", 3);
                    result.put("resultMsg", "查询完毕");
                    //result.put("size", size);
                    result.put("dealedCount", dealed);
                    result.put("rows", items);
                } else {
                    result.put("resultCode", 1);
                    result.put("resultMsg", "此次轮询成功");
                    //result.put("size", size);
                    result.put("dealedCount", dealed);
                    result.put("rows", items);
                }
            }

        } catch (Exception ex) {
            logger.error(null, ex);
            result.put("resultCode", -1);
            result.put("resultMsg", "查询出错");
        }

        return result;
    }

}
