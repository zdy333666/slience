/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabaggr.dao;

import com.hzcominfo.gabaggr.conf.GABAggrConfiguration;
import com.hzcominfo.gabaggr.util.MongoUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Administrator
 */
@Component
public class GABAggrFetchDao {

    private final Logger logger = LoggerFactory.getLogger(GABAggrFetchDao.class);

    @Autowired
    private GABInfoQuery _GABInfoQuery;

    /**
     *
     * @param db
     * @param dimension
     * @param pServiceIdList
     * @param conditionNode
     * @return
     */
    public ConcurrentMap<Long, Map<String, Object>> getServiceInfos(DB db, String dimension, long[] pServiceIdList, Map<String, String> conditionNode) {

        ConcurrentMap<Long, Map<String, Object>> serviceInfos = new ConcurrentHashMap<>();

        DBObject innerQuery = new BasicDBObject();
        innerQuery.put("$in", pServiceIdList);

        DBObject query = new BasicDBObject();
        query.put("service_id", innerQuery);

        DBObject sort = new BasicDBObject();
        sort.put("serviceset_id", 1);
        sort.put("region_code", 1);
        sort.put("display_order", 1);
        List<DBObject> serviceDocs = db.getCollection(GABAggrConfiguration.table_gab_service_define).find(query).sort(sort).toArray();

        Map<Long, DBObject> servicesetDocCache = new HashMap<>();

        for (DBObject serviceDoc : serviceDocs) {
            long serviceId = MongoUtil.getLongValue(serviceDoc, "service_id");
            String serviceName = MongoUtil.getStringValue(serviceDoc, "service_name");
            String serviceDisplayName = MongoUtil.getStringValue(serviceDoc, "service_display_name");
            String regionCode = MongoUtil.getStringValue(serviceDoc, "region_code");
            long servicesetId = MongoUtil.getLongValue(serviceDoc, "serviceset_id");

            //查询地区信息
            DBObject regionQuery = new BasicDBObject();
            regionQuery.put("CODEID", regionCode);
            DBObject regionDoc = db.getCollection(GABAggrConfiguration.table_gab_region_define).findOne(regionQuery, new BasicDBObject("CITYNAME", 1).append("_id", 0));
            String regionName = "";
            if (regionDoc != null) {
                regionName = MongoUtil.getStringValue(regionDoc, "CITYNAME");
            }

            Map<String, Object> serviceNode = new HashMap<>();
            serviceNode.put("serviceId", serviceId);    //1
            serviceNode.put("serviceName", serviceName);    //2
            serviceNode.put("serviceDisplayName", serviceDisplayName);  //3
            serviceNode.put("regionCode", regionCode);  //4
            serviceNode.put("regionName", regionName);  //5

            //查询服务集信息
            DBObject serviceSetQuery = new BasicDBObject();
            serviceSetQuery.put("dimension_name", dimension);
            serviceSetQuery.put("serviceset_id", servicesetId);

            DBObject servicesetDoc = null;
            if (servicesetDocCache.containsKey(servicesetId)) {
                servicesetDoc = servicesetDocCache.get(servicesetId);
            } else {
                servicesetDoc = db.getCollection(GABAggrConfiguration.table_gab_serviceset_define).findOne(serviceSetQuery);
                servicesetDocCache.put(servicesetId, servicesetDoc);
            }

            String servicesetName = "";
            String servicesetDisplayName = "";
            if (servicesetDoc != null) {
                servicesetName = MongoUtil.getStringValue(servicesetDoc, "serviceset_name");
                servicesetDisplayName = MongoUtil.getStringValue(servicesetDoc, "serviceset_display_name");
            }
            serviceNode.put("servicesetId", servicesetId);  //6
            serviceNode.put("servicesetName", servicesetName);      //7
            serviceNode.put("servicesetDisplayName", servicesetDisplayName);  //8

            //查询服务集参数，将客户端的param转化为本地的param
            DBObject serviceSetParamQuery = new BasicDBObject();
            serviceSetParamQuery.put("serviceset_id", servicesetId);
            serviceSetParamQuery.put("enable", 1);
            List<DBObject> paramDocs = db.getCollection(GABAggrConfiguration.table_gab_serviceset_param).find(serviceSetParamQuery).toArray();

            Map<String, String> localConditionMap = new HashMap<>();
            for (DBObject tmpServiceSetParamBObject : paramDocs) {
                String receiveParamName = (String) tmpServiceSetParamBObject.get("param_name");
                if (conditionNode.get(receiveParamName) != null) {
                    localConditionMap.put((String) tmpServiceSetParamBObject.get("field_name"), conditionNode.get(receiveParamName));
                }
            }

            //查询服务字段信息
            DBObject serviceFieldQuery = new BasicDBObject();
            serviceFieldQuery.put("serviceset_id", servicesetId);
            serviceFieldQuery.put("service_id", serviceId);
            serviceFieldQuery.put("enable", 1);

            DBObject serviceFieldSort = new BasicDBObject();
            serviceFieldSort.put("field_order", 1);
            List<DBObject> serviceFieldDocs = db.getCollection(GABAggrConfiguration.table_gab_service_field_define).find(serviceFieldQuery).sort(serviceFieldSort).toArray();

            List<Map<String, String>> serviceItems = new LinkedList<>();
            Map<String, String> serviceParams = new HashMap<>();

            for (DBObject serviceFieldDoc : serviceFieldDocs) {
                String fieldName = MongoUtil.getStringValue(serviceFieldDoc, "field_name");
                if (fieldName == null || fieldName.trim().isEmpty()) {
                    continue;
                }
                String colFieldName = MongoUtil.getStringValue(serviceFieldDoc, "col_field_name");
                if (colFieldName == null || colFieldName.trim().isEmpty()) {
                    continue;
                }

                String fieldDisplayName = MongoUtil.getStringValue(serviceFieldDoc, "field_display_name");

                int paramFlag = MongoUtil.getIntValue(serviceFieldDoc, "param_flag");
                int displayFlag = MongoUtil.getIntValue(serviceFieldDoc, "display_flag");

                //将查询字段的名称转化为数据源的名称
                if (paramFlag == 1) {
                    if (localConditionMap.containsKey(fieldName)) {
                        serviceParams.put(colFieldName, localConditionMap.get(fieldName));
                    }
                }
                //将需要显示结果的字段的查询
                if (displayFlag == 1) {
                    Map<String, String> tmpServiceFieldNode = new HashMap<>();
                    tmpServiceFieldNode.put("fieldName", fieldName);
                    tmpServiceFieldNode.put("fieldDisplayName", fieldDisplayName);
                    serviceItems.add(tmpServiceFieldNode);
                }
            }

            serviceNode.put("serviceParams", serviceParams);   //9
            serviceNode.put("serviceItems", serviceItems);   //10

            serviceInfos.put(serviceId, serviceNode);
        }

        return serviceInfos;
    }

    /**
     *
     * @param userCardId
     * @param userName
     * @param userDept
     * @param serviceInfo
     * @return
     */
    public Map<String, Object> getServiceResult(String userCardId, String userName, String userDept, Map<String, Object> serviceInfo) {

        String localServiceName = (String) serviceInfo.get("serviceName");
        Map<String, String> localServiceParams = (Map<String, String>) serviceInfo.remove("serviceParams");
        List<Map<String, String>> localServiceItemsNode = (List<Map<String, String>>) serviceInfo.remove("serviceItems");

        Map<String, String> localServiceItemsMap = new HashMap<>();
        for (Map<String, String> serviceItem : localServiceItemsNode) {
            localServiceItemsMap.put(serviceItem.get("fieldName"), serviceItem.get("fieldDisplayName"));
        }

        logger.info("serviceName --> " + localServiceName);
        logger.info("serviceParam --> " + localServiceParams);
        logger.info("userCardId --> " + userCardId);
        logger.info("userName --> " + userName);
        logger.info("userDept --> " + userDept);
        logger.info("\n");
        List<Map<String, String>> queryResList = _GABInfoQuery.queryGABInfo(localServiceName, localServiceParams, userCardId, userName, userDept);

        //System.out.println("queryResList --> " + queryResList);
        //System.out.println();
        if (queryResList.isEmpty()) {
            return null;
        }

        List<List<Map<String, String>>> resServiceItemsNode = new LinkedList<>();
        for (Map<String, String> tmpResMap : queryResList) {

            //一个tmpResMap对应一组service结果，可能返回多组service结果
            //对于每个tmpResMap，遍历操作=》tmpOneFieldRes，得到字段名称，在需要打印的map中是否包含该字段，如果是
            //封装一个oneFieldNode，加入oneServiceResNode,最后将所有oneServiceResNode加入serviceItems   
            List<Map<String, String>> tmpResOneServiceItemNode = new LinkedList<>(); //一组结果节点   
            for (Entry<String, String> entry : tmpResMap.entrySet()) {
                String tmpServiceFieldName = entry.getKey();
                String tmpServiceFieldValue = entry.getValue();

                if (localServiceItemsMap.containsKey(tmpServiceFieldName)) {
                    Map<String, String> tmpResFieldNameNode = new HashMap<>();
                    tmpResFieldNameNode.put("fieldName", tmpServiceFieldName);
                    tmpResFieldNameNode.put("fieldDisplayName", localServiceItemsMap.get(tmpServiceFieldName));
                    tmpResFieldNameNode.put("fieldValue", tmpServiceFieldValue);

                    tmpResOneServiceItemNode.add(tmpResFieldNameNode);
                }
            }

            resServiceItemsNode.add(tmpResOneServiceItemNode);
        }

        serviceInfo.put("serviceItems", resServiceItemsNode);       //9

        return serviceInfo;
    }
}
