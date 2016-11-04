/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabinfo.controller;

import com.hzcominfo.gabinfo.conf.GABInfoConfiguration;
import com.hzcominfo.gabinfo.pojo.GABInfoInput;
import com.hzcominfo.gabinfo.service.GABInfoQGRKBackupService;
import com.hzcominfo.gabinfo.service.GABInfoService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author zdy
 */
@Controller
public class GABInfoController {

    private final Logger logger = LoggerFactory.getLogger(GABInfoController.class);

    @Autowired
    private GABInfoService _GABInfoService;

    @Autowired
    private GABInfoQGRKBackupService _GABInfoQGRKBackupService;

    /**
     * 获取全国人口信息
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "QGRK", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> queryQGRK(@RequestBody GABInfoInput input) {

        Map<String, String> condition = input.getCondition();
        if (condition == null) {
            return null;
        }

        String sfzh = condition.get("SFZH");
        if (sfzh == null || sfzh.trim().isEmpty() || (sfzh.length() != 15 && sfzh.length() != 18) || sfzh.startsWith("00")) {
            return null;
        }

        Map<String, String> result = null;

        String[] fields = input.getFields();
        try {
            result = _GABInfoQGRKBackupService.find(sfzh, fields);
        } catch (Exception e) {
            logger.error(null, e);
        }
        if (result != null) {
            return result;
        }

        input.setServerId(GABInfoConfiguration.SERVER_ID_QGRK);
        input.setDataObjectCode("A001"); //A090
        input.setFields(new String[0]);

        long startTime = System.currentTimeMillis();

        try {
            result = _GABInfoService.fetchRow(input);
        } catch (Exception e) {
            logger.error(null, e);
        }

        if (result == null) {
            return result;
        }

        String CSDStr = result.get("CSD");
        if (CSDStr != null && CSDStr.contains("/")) {
            String[] csdStrs = CSDStr.split("/");
            CSDStr = (csdStrs.length == 2) ? csdStrs[1] : CSDStr.substring(0, CSDStr.indexOf("/"));
            result.put("CSD", CSDStr);
        }

        //备份查询结果
        _GABInfoQGRKBackupService.save(result);

        if ((fields != null) && (fields.length > 0)) {
            Map<String, String> temp = new HashMap<>();
            for (String field : fields) {
                temp.put(field, result.get(field));
            }
            result = temp;
        }

        logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- GABInfo Query -- ").append("QGRK").append(" -- spend time --> ").append(System.currentTimeMillis() - startTime).append(" ms").toString());

        return result;
    }

    /**
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "fetchRow", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> fetchRow(@RequestBody GABInfoInput input) {

        Map<String, String> result = null;
        try {
            result = _GABInfoService.fetchRow(input);
        } catch (Exception e) {
            logger.error(null, e);
        }

        return result == null ? new HashMap<>() : result;
    }

    /**
     *
     * @param input
     * @return
     */
    @RequestMapping(value = "fetchRows", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, String>> fetchRows(@RequestBody GABInfoInput input) {

        List<Map<String, String>> result = null;
        try {
            result = _GABInfoService.fetchRows(input);
        } catch (Exception e) {
            logger.error(null, e);
        }

        return result == null ? new ArrayList<>() : result;
    }

}
