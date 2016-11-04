/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author breeze
 */
@Repository
public class GABQGRKDao {

    /*
     fieldNameMap.put("SFZH", "身份证号");
     fieldNameMap.put("XM", "姓名");
     fieldNameMap.put("CYM", "曾用名");
     fieldNameMap.put("XB", "性别");
     fieldNameMap.put("MZ", "民族");
     fieldNameMap.put("HYZK", "婚姻状况");
     fieldNameMap.put("WHCD", "文化程度");
     fieldNameMap.put("CSRQ", "出生日期");
     fieldNameMap.put("CSD", "出生地");
     fieldNameMap.put("JGSSX", "籍贯");
     fieldNameMap.put("HKSZD", "户籍地");
     fieldNameMap.put("FWCS", "服务场所");
     fieldNameMap.put("ZY", "职业");
     fieldNameMap.put("ZZXZ", "住址详址");
     fieldNameMap.put("XP", "相片");
     */
    private final Logger logger = LoggerFactory.getLogger(GABQGRKDao.class);

    @LoadBalanced
    @Autowired
    private RestTemplate restTemplate;

    /**
     * 在全国人口库中查找人员信息
     *
     * @param auth
     * @param params
     * @param returnItems
     * @return
     */
    public Map<String, String> fetchGABQGRK(Map<String, String> auth, Map<String, String> params, Set<String> returnItems) {

        String userCardId = auth.get("userCardId");//用户身份证号
        String userName = auth.get("userName"); //用户名
        String userDept = auth.get("userDept");//用户单位

        Map<String, Object> input = new HashMap<>();
        input.put("condition", params);
        input.put("userName", userName);
        input.put("userCardId", userCardId);
        input.put("userDept", userDept);
        input.put("fields", returnItems.toArray());

        Map<String, String> result = null;
        try {
            result = restTemplate.postForObject("http://gabinfo/QGRK", input, Map.class);
        } catch (Exception e) {
            logger.error(null, e);
        }

        return result;
    }

}
