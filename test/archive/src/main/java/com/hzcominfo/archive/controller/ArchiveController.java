/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.archive.controller;

import com.hzcominfo.archive.pojo.ArchiveInput;
import com.hzcominfo.archive.service.ArchiveService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
public class ArchiveController {

    private final Logger logger = LoggerFactory.getLogger(ArchiveController.class);

    @Autowired
    private ArchiveService _service;

    @LoadBalanced
    @Autowired
    private RestTemplate restTemplate;

    /**
     *
     * @param request
     * @param response
     * @param setId
     * @param sfzh
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "set/{setId}/{sfzh}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> GetAggregateSetResult(HttpServletRequest request, HttpServletResponse response,
            @PathVariable long setId, @PathVariable String sfzh) throws Exception {

        Map<String, String> auth = new HashMap<>();
        auth.put("userName", "华晓阳");
        auth.put("userCardId", "330103199107170010");
        auth.put("userDept", "330100230500");

        Map<String, String> condition = new HashMap<>();
        condition.put("zjhm", sfzh);

        Map<String, Object> input = new HashMap<>();
        input.put("dimension", "person");
        input.put("attrSetId", setId);
        input.put("condition", condition);
        input.put("auth", auth);
        //input.put("rows", 1000);

        Map<String, Object> result = null;
        try {
            result = restTemplate.postForObject("http://aggregate-base/set", input, Map.class);
        } catch (Exception e) {
            logger.error(null, e);
        }

        return result == null ? new HashMap<>() : result;
    }

    /**
     *
     * @param request
     * @param response
     * @param input
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "set", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> PostAggregateSetResult(HttpServletRequest request, HttpServletResponse response, @RequestBody ArchiveInput input) throws Exception {

        Map<String, Object> result = null;
        try {
            result = restTemplate.postForObject("http://aggregate-base/set", input, Map.class);
        } catch (Exception e) {
            logger.error(null, e);
        }

        return result == null ? new HashMap<>() : result;
    }

    /**
     *
     * @param request
     * @param response
     * @param sfzh
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "aggr/{sfzh}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> GetAggregateDimensionResult(HttpServletRequest request, HttpServletResponse response,
            @PathVariable String sfzh) throws Exception {
        //, @RequestBody Map<String, Object> body

        String dimension = "person";

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("requestId", dimension);

        try {
            List<Long> attrSetIds = _service.fetchAttrSetIds(dimension);
            Map<Long, Map<String, Object>> fetchResult = new LinkedHashMap<>();

            attrSetIds.parallelStream().forEach((attrSetId) -> {
                Map<String, String> auth = new HashMap<>();
                auth.put("userName", "华晓阳");
                auth.put("userCardId", "330103199107170010");
                auth.put("userDept", "330100230500");
                Map<String, String> condition = new HashMap<>();
                condition.put("zjhm", sfzh);
                Map<String, Object> input = new HashMap<>();
                input.put("dimension", dimension);
                input.put("attrSetId", attrSetId);
                input.put("condition", condition);
                input.put("auth", auth);
                //input.put("rows", 1000);

                Map<String, Object> attrSetResult = null;
                try {
                    attrSetResult = restTemplate.postForObject("http://aggregate-base/set", input, Map.class);
                } catch (Exception e) {
                    logger.error(null, e);
                }

                if (!(attrSetResult == null || attrSetResult.get("fields") == null || attrSetResult.get("rows") == null)) {
                    fetchResult.put(attrSetId, attrSetResult);
                }
            });

            List<Map<String, Object>> fields = new ArrayList<>();
            List<Map<String, Object>> rows = new ArrayList<>();
            for (long attrSetId : attrSetIds) {
                Map<String, Object> item = fetchResult.get(attrSetId);
                if (item == null || item.isEmpty()) {
                    continue;
                }
                fields.add((Map<String, Object>) item.get("fields"));
                rows.add((Map<String, Object>) item.get("rows"));
            }

            result.put("resultCode", 1);
            result.put("resultMsg", "查询成功");
            result.put("fields", fields);
            result.put("rows", rows);

        } catch (Exception e) {
            logger.error(null, e);
            result.put("resultCode", 0);
            result.put("resultMsg", "查询失败");
        }

        return result;
    }

    /**
     *
     * @param request
     * @param response
     * @param input
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "dimension", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> PostAggregateDimensionResult(HttpServletRequest request, HttpServletResponse response, @RequestBody ArchiveInput input) throws Exception {

        String dimension = input.getDimension();
        Map<String, Object> condition = input.getCondition();
        Map<String, String> auth = input.getAuth();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("requestId", dimension);

        try {
            List<Long> attrSetIds = _service.fetchAttrSetIds(dimension);

            Map<Long, Map<String, Object>> fetchResult = new LinkedHashMap<>();
            attrSetIds.parallelStream().forEach((attrSetId) -> {
                Map<String, Object> param = new HashMap<>();
                param.put("dimension", dimension);
                param.put("attrSetId", attrSetId);
                param.put("condition", condition);
                param.put("auth", auth);
                //input.put("rows", 1000);

                Map<String, Object> attrSetResult = null;
                try {
                    attrSetResult = restTemplate.postForObject("http://service-aggregate-base/set", param, Map.class);
                } catch (Exception e) {
                    logger.error(null, e);
                }
                if (!(attrSetResult == null || attrSetResult.get("fields") == null || attrSetResult.get("rows") == null)) {
                    fetchResult.put(attrSetId, attrSetResult);
                }
            });

            List<Map<String, Object>> fields = new ArrayList<>();
            List<Map<String, Object>> rows = new ArrayList<>();
            for (long attrSetId : attrSetIds) {
                Map<String, Object> item = fetchResult.get(attrSetId);
                if (item == null || item.isEmpty()) {
                    continue;
                }
                fields.add((Map<String, Object>) item.get("fields"));
                rows.add((Map<String, Object>) item.get("rows"));
            }

            result.put("resultCode", 1);
            result.put("resultMsg", "查询成功");
            result.put("fields", fields);
            result.put("rows", rows);

        } catch (Exception e) {
            logger.error(null, e);
            result.put("resultCode", 0);
            result.put("resultMsg", "查询失败");
        }

        return result;
    }

}
