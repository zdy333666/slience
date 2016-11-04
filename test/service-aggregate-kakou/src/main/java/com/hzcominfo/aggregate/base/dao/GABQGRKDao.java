/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.dao;

import com.hzcominfo.aggregate.base.conf.AggregateConfiguration;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
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

//    private final static Map<String, String> fieldNameCodeMap = new HashMap<>();//全国人口库字段代码值翻译时对应的字段名映射
//
//    static {
//        /*
//         fieldNameMap.put("SFZH", "身份证号");
//         fieldNameMap.put("RYBH", "人员编号");
//         fieldNameMap.put("XM", "姓名");
//         fieldNameMap.put("CYM", "曾用名");
//         fieldNameMap.put("XB", "性别");
//         fieldNameMap.put("MZ", "民族");
//         fieldNameMap.put("HYZK", "婚姻状况");
//         fieldNameMap.put("WHCD", "文化程度");
//         fieldNameMap.put("CSRQ", "出生日期");
//         fieldNameMap.put("CSD", "出生地");
//         fieldNameMap.put("JGSSX", "籍贯");
//         fieldNameMap.put("HKSZD", "户籍地");
//         fieldNameMap.put("FWCS", "服务场所");
//         fieldNameMap.put("XP", "相片");
//         */
//        // ----------------------------------
//        fieldNameCodeMap.put("XB", "XB");
//        fieldNameCodeMap.put("MZ", "MZ");
//        fieldNameCodeMap.put("HYZK", "HYZK");
//        fieldNameCodeMap.put("WHCD", "WHCD");
//        fieldNameCodeMap.put("JGSSX", "XZQH");
//        fieldNameCodeMap.put("HKSZD", "XZQH");
//        fieldNameCodeMap.put("CSD", "XZQH");
//    }
    private final Logger logger = LoggerFactory.getLogger(GABQGRKDao.class);

    @LoadBalanced
    @Autowired
    private RestTemplate restTemplate;

    /*
     * 在全国人口库中查找人员信息
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
            result = restTemplate.postForObject("http://service-gabinfo/QGRK", input, Map.class);
        } catch (Exception e) {
            logger.error(null, e);
        }

        return result;
    }

    /*
     * 在Mongo数据库中的全国人口信息备份库中查找人员信息
     */
    public Map<String, String> fetchGABQGRKBackup(DB db, Map<String, String> params, Set<String> returnItems) {

        DBObject query = new BasicDBObject(params);
        //query.put("enable", 1);

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        for (String attrName : returnItems) {
            fields.put(attrName, 1);
        }

        DBObject doc = db.getCollection(AggregateConfiguration.AGGREGATE_QGRK_BACKUP).findOne(query, fields);
        if (doc != null) {
            return doc.toMap();
        }

        return null;
    }

//    private static String parseQGRKFieldCode(DB db, String fieldName, String code) {
//        String valueStr = null;
//
//        DBObject query = new BasicDBObject();
//        query.put("field_name", fieldName);
//        query.put("code", code);
//
//        DBObject fields = new BasicDBObject();
//        fields.put("_id", 0);
//        fields.put("value", 1);
//
//        DBObject doc = db.getCollection(AggregateConfiguration.AGGREGATE_QGRK_CODE).findOne(query, fields);
//        if (doc != null) {
//            valueStr = String.valueOf(doc.get("value"));
//        }
//
//        return valueStr;
//    }
}
