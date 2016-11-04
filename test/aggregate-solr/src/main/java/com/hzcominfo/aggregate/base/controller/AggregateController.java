/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.controller;

import com.hzcominfo.aggregate.base.service.AggregateService;
import com.hzcominfo.aggregate.base.pojo.AggregateInput;
import com.hzcominfo.aggregate.base.service.AggregateLogService;
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
public class AggregateController {

    private final Logger logger = LoggerFactory.getLogger(AggregateController.class);

    @Autowired
    private AggregateService _service;

    @Autowired
    private AggregateLogService _logService;

    @LoadBalanced
    @Autowired
    private RestTemplate restTemplate;

//    @LoadBalanced
//    @Autowired
//    private RestTemplate restTemplate;
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
    public Map<String, Object> getAggregateSet(HttpServletRequest request, HttpServletResponse response,
            @PathVariable long setId, @PathVariable String sfzh) throws Exception {

        Map<String, String> auth = new HashMap<>();
        auth.put("userName", "华晓阳");
        auth.put("userCardId", "330103199107170010");
        auth.put("userDept", "330100230500");

        Map<String, Object> condition = new HashMap<>();
        condition.put("zjhm", sfzh);

        AggregateInput input = new AggregateInput();
        input.setDimensionId(1L);
        input.setSetId(setId);
        input.setCondition(condition);
        input.setAuth(auth);
        //input.put("rows", 1000);

        long startTime = System.currentTimeMillis();
        Map<String, Object> result = _service.service(input);
        logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- Aggregate Query total -- ").append("spend time ---> ").append(System.currentTimeMillis() - startTime).append(" ms").toString());

        return result;
    }

    /**
     * 按维度进行查询
     *
     * @param request
     * @param response
     * @param input
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "set", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> handleInfoQueryRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody AggregateInput input) throws Exception {

        String userCardId = "330103199107170010";
        String userName = "华晓阳";
        String userDept = "330100230500";

        Map<String, String> auth = new HashMap<>();
        auth.put("userCardId", userCardId);
        auth.put("userName", userName);
        auth.put("userDept", userDept);

        input.setAuth(auth);

        long startTime = System.currentTimeMillis();
        Map<String, Object> result = _service.service(input);
        logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- Aggregate Query total -- ").append("spend time ---> ").append(System.currentTimeMillis() - startTime).append(" ms").toString());

        //添加访问日志-------------------------------------------------------
        Map<String, Object> log = new HashMap<>();
        log.put("USER_ID", userCardId);
        log.put("USER_NAME", userName);
        log.put("ORGANIZATION", "科通局");
        log.put("ORGANIZATION_ID", userDept);
        log.put("TERMINAL_ID", request.getRemoteAddr());
        log.put("OPERATE_NAME", "QueryAggrService");
        log.put("OPERATE_CONDITION", input.getCondition());
        log.put("OPERATE_TYPE", 1);

        log.put("OPERATE_RESULT", 1); //1:0

        log.put("ERROR_CODE", null);
        log.put("BZ1", "userId"); //用户编号
        log.put("BZ2", "role"); //角色名称
        log.put("BZ3", null);
        log.put("BZ4", null);
        log.put("BZ5", null);

        _logService.log(log);

        return result;
    }

//    /**
//     *
//     * @param request
//     * @param response
//     * @param sfzh
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping(value = "aggr/{sfzh}", method = RequestMethod.GET)
//    @ResponseBody
//    public Map<String, String> handleQGRKQuery(HttpServletRequest request, HttpServletResponse response,
//            @PathVariable String sfzh) throws Exception {
//        //, @RequestBody Map<String, Object> body
//
//        Map<String, String> condition = new HashMap<>();
//        condition.put("SFZH", sfzh);
//
//        Map<String, Object> input = new HashMap<>();
//        //input.put("dataObjectCode", "A090");
//        input.put("condition", condition);
//        input.put("userName", "华晓阳");
//        input.put("userCardId", "330103199107170010");
//        input.put("userDept", "330100230500");
//        input.put("fields", new String[]{});
//
//        Map<String, String> result = null;
//        try {
//            result = restTemplate.postForObject("http://service-gabinfo/QGRK", input, Map.class);
//        } catch (Exception e) {
//            logger.error(null, e);
//        }
//
//        return result == null ? new HashMap<>() : result;
//    }
}
