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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * 调用公安部信息查询接口
 *
 * @author breeze
 */
@Repository
public class GABAggrMetaDao {

    /**
     *
     * @param dimension
     * @return
     */
    public List<Map<String, Object>> queryServicesetOfDimension(DB db, String dimension) {

        List<Map<String, Object>> rowsNode = new LinkedList<>();

        //查询当前维度下属的所有服务集信息
        DBObject query = new BasicDBObject();
        query.put("dimension_name", dimension);

        DBObject sort = new BasicDBObject();
        sort.put("display_order", 1);

        List<DBObject> servicesetDocList = db.getCollection(GABAggrConfiguration.table_gab_serviceset_define).find(query).sort(sort).toArray();

        List<Long> servicesetIdList = new ArrayList<>();

        for (DBObject servicesetDoc : servicesetDocList) {
            long servicesetId = MongoUtil.getLongValue(servicesetDoc, "serviceset_id");
            if (servicesetIdList.contains(servicesetId)) {
                continue;
            }
            servicesetIdList.add(servicesetId);
        }

        //查询当前维度下属的所有服务信息
        DBObject innerQuery = new BasicDBObject();
        innerQuery.put("$in", servicesetIdList.toArray());

        query = new BasicDBObject();
        query.put("serviceset_id", innerQuery);
        query.put("enable", 1);

        sort = new BasicDBObject();
        sort.put("region_code", 1);
        sort.put("display_order", 1);

        List<DBObject> allServiceList = db.getCollection(GABAggrConfiguration.table_gab_service_define).find(query).sort(sort).toArray();

        Map<Long, List<DBObject>> allServiceMap = new HashMap<>();//缓存当前服务集下属的所有服务信息，并按区域分组
        for (DBObject serviceDoc : allServiceList) {
            long servicesetId = MongoUtil.getLongValue(serviceDoc, "serviceset_id");

            List<DBObject> itemServiceList = allServiceMap.get(servicesetId);
            if (itemServiceList == null) {
                itemServiceList = new ArrayList<>();
            }
            itemServiceList.add(serviceDoc);
            allServiceMap.put(servicesetId, itemServiceList);
        }

        //查询所有的区域信息
        query = new BasicDBObject();

        sort = new BasicDBObject();
        sort.put("PARENTID", 1);

        List<DBObject> allRegionDocList = db.getCollection(GABAggrConfiguration.table_gab_region_define).find(query).sort(sort).toArray();

        Map<String, List<DBObject>> allRegionMap = new HashMap<>();//缓存所有的区域信息，并按上级区域分组
        for (DBObject regionDoc : allRegionDocList) {
            String PARENTID = MongoUtil.getStringValue(regionDoc, "PARENTID");
            List<DBObject> itemRegionList = allRegionMap.get(PARENTID);
            if (itemRegionList == null) {
                itemRegionList = new ArrayList<>();
            }
            itemRegionList.add(regionDoc);
            allRegionMap.put(PARENTID, itemRegionList);
        }

        for (DBObject servicesetDoc : servicesetDocList) {

            String servicesetName = MongoUtil.getStringValue(servicesetDoc, "serviceset_name");
            if (servicesetName == null || servicesetName.trim().isEmpty()) {
                continue;
            }

            String servicesetDisplayName = MongoUtil.getStringValue(servicesetDoc, "serviceset_display_name");
            if (servicesetDisplayName == null || servicesetDisplayName.trim().isEmpty()) {
                continue;
            }
            long servicesetId = MongoUtil.getLongValue(servicesetDoc, "serviceset_id");

            List<DBObject> allServiceDocList = allServiceMap.get(servicesetId);

            int count = 0;
            Map<String, List<DBObject>> regionServiceMap = new HashMap<>();//缓存当前服务集下属的所有服务信息，并按区域分组       
            if (allServiceDocList != null) {
                for (DBObject serviceDoc : allServiceDocList) {
                    String regionCode = MongoUtil.getStringValue(serviceDoc, "region_code");
                    List<DBObject> regionServiceList = regionServiceMap.get(regionCode);
                    if (regionServiceList == null) {
                        regionServiceList = new ArrayList<>();
                    }
                    regionServiceList.add(serviceDoc);
                    regionServiceMap.put(regionCode, regionServiceList);
                }

                count = allServiceDocList.size();
            }

            //-------------------------------
            //查询顶级区域信息
            String topRegionCode = "0";

            query = new BasicDBObject();
            query.put("CODEID", topRegionCode);
            DBObject regionDoc = db.getCollection(GABAggrConfiguration.table_gab_region_define).findOne(query);

            List<DBObject> serviceDocList = regionServiceMap.get(topRegionCode);

            List<Map<String, Object>> serviceItemsNode = new LinkedList<>();
            if (serviceDocList != null) {
                for (DBObject serviceDoc : serviceDocList) {

                    Map<String, Object> serviceNode = new HashMap<>();
                    serviceNode.put("serviceId", MongoUtil.getLongValue(serviceDoc, "service_id"));
                    serviceNode.put("serviceName", MongoUtil.getStringValue(serviceDoc, "service_name"));
                    serviceNode.put("serviceDisplayName", MongoUtil.getStringValue(serviceDoc, "service_type_display_name"));

                    serviceItemsNode.add(serviceNode);
                }
            }

            List<Map<String, Object>> itemsNode = queryItemOfRegion(db, servicesetId, topRegionCode, allRegionMap, regionServiceMap);

            if (serviceItemsNode.isEmpty() && itemsNode.isEmpty()) {
                continue;
            }

            Map<String, Object> regionNode = new HashMap<>();
            regionNode.put("regionCode", topRegionCode);
            regionNode.put("regionName", MongoUtil.getStringValue(regionDoc, "CITYNAME"));
            regionNode.put("serviceCount", serviceItemsNode.size());
            regionNode.put("serviceItems", serviceItemsNode);
            regionNode.put("items", itemsNode);

            List<Map<String, Object>> servicesetItemsNode = new LinkedList<>();
            servicesetItemsNode.add(regionNode);

            Map<String, Object> rowNode = new HashMap<>();
            rowNode.put("servicesetId", servicesetId);
            rowNode.put("servicesetName", servicesetName);
            rowNode.put("servicesetDisplayName", servicesetDisplayName);
            rowNode.put("count", count);
            rowNode.put("servicesetItems", servicesetItemsNode);
            //--------------------------------

            rowsNode.add(rowNode);
        }

        return rowsNode;
    }

    /**
     *
     * @param db
     * @param pServicesetId
     * @param pParentId
     * @param allRegionMap
     * @param regionServiceMap
     * @return
     */
    private List<Map<String, Object>> queryItemOfRegion(DB db, long pServicesetId, String pParentId, Map<String, List<DBObject>> allRegionMap, Map<String, List<DBObject>> regionServiceMap) {
        List<Map<String, Object>> rowsNode = new LinkedList<>();

        List<DBObject> itemRegionDocList = allRegionMap.get(pParentId);
        if (itemRegionDocList == null || itemRegionDocList.isEmpty()) {
            return rowsNode;
        }

        for (DBObject regionDoc : itemRegionDocList) {

            String regionCode = MongoUtil.getStringValue(regionDoc, "CODEID");

            List<DBObject> serviceDocList = regionServiceMap.get(regionCode);

            List<Map<String, Object>> serviceItemsNode = new LinkedList<>();
            if (serviceDocList != null) {
                for (DBObject doc : serviceDocList) {
                    Map<String, Object> serviceNode = new HashMap<>();
                    serviceNode.put("serviceId", MongoUtil.getLongValue(doc, "service_id"));
                    serviceNode.put("serviceName", MongoUtil.getStringValue(doc, "service_name"));
                    serviceNode.put("serviceDisplayName", MongoUtil.getStringValue(doc, "service_type_display_name"));

                    serviceItemsNode.add(serviceNode);
                }
            }

            List<Map<String, Object>> itemsNode = queryItemOfRegion(db, pServicesetId, regionCode, allRegionMap, regionServiceMap);
            if (serviceItemsNode.isEmpty() && itemsNode.isEmpty()) {
                continue;
            }

            Map<String, Object> rowNode = new HashMap<>();
            rowNode.put("regionCode", regionCode);
            rowNode.put("regionName", MongoUtil.getStringValue(regionDoc, "CITYNAME"));
            rowNode.put("serviceCount", serviceItemsNode.size());
            rowNode.put("serviceItems", serviceItemsNode);
            rowNode.put("items", itemsNode);

            rowsNode.add(rowNode);
        }

        return rowsNode;
    }

    /**
     *
     * @param db
     * @param pServicesetIdList
     * @param pParentId
     * @return
     */
//    public List<Map<String, Object>> queryItemOfRegion(DB db, List<Long> pServicesetIdList, String pParentId) {
//
//        List<Map<String, Object>> rowsNode = new LinkedList<>();
//
//        if (pServicesetIdList.isEmpty()) {
//            return rowsNode;
//        }
//
//        DBObject query = new BasicDBObject();
//        query.put("PARENTID", pParentId);
//
//        DBObject sort = new BasicDBObject();
//        sort.put("CODEID", 1);
//
//        List<DBObject> itemRegionDocList = db.getCollection(GABAggrConfiguration.table_gab_region_define).find(query).sort(sort).toArray();
//        if (itemRegionDocList.isEmpty()) {
//            return rowsNode;
//        }
//
//        List<String> itemRegionIdList = new ArrayList<>();
//        for (DBObject regionDoc : itemRegionDocList) {
//            String regionId = MongoUtil.getStringValue(regionDoc, "CODEID");
//            if (itemRegionIdList.contains(regionId)) {
//                continue;
//            }
//            itemRegionIdList.add(regionId);
//        }
//
//        DBObject innerQuery1 = new BasicDBObject();
//        innerQuery1.put("$in", pServicesetIdList.toArray());
//
//        DBObject innerQuery2 = new BasicDBObject();
//        innerQuery2.put("$in", itemRegionIdList.toArray());
//
//        query = new BasicDBObject();
//        query.put("serviceset_id", innerQuery1);
//        query.put("region_code", innerQuery2);
//
//        sort = new BasicDBObject();
//        sort.put("region_code", 1);
//
//        List<DBObject> docList = db.getCollection(GABAggrConfiguration.table_gab_service_define).find(query).sort(sort).toArray();
//        if (docList.isEmpty()) {
//            return rowsNode;
//        }
//
//        for (DBObject doc : docList) {
//            String regionCode = MongoUtil.getStringValue(doc, "region_code");
//            if (regionCode == null || regionCode.trim().isEmpty()) {
//                continue;
//            }
//
//            /*
//             * 统计服务数目的代码
//             */
//            Pattern ssxqPattern = Pattern.compile("^" + regionCode); //构建正则表达式
//
//            query = new BasicDBObject();
//            query.put("serviceset_id", innerQuery1);
//            query.put("region_code", ssxqPattern);
//            query.put("enable", 1);
//
//            long serviceCount = db.getCollection(GABAggrConfiguration.table_gab_service_define).count(query);
//
//            query = new BasicDBObject();
//            query.put("CODEID", regionCode);
//            DBObject regionDoc = db.getCollection(GABAggrConfiguration.table_gab_region_define).findOne(query);
//
//            Map<String, Object> rowNode = new HashMap<>();
//            rowNode.put("serviceId", MongoUtil.getLongValue(doc, "service_id"));
//            rowNode.put("serviceName", MongoUtil.getStringValue(doc, "service_name"));
//            rowNode.put("serviceDisplayName", MongoUtil.getStringValue(doc, "service_display_name"));
//            rowNode.put("regionCode", regionCode);
//            rowNode.put("regionName", MongoUtil.getStringValue(regionDoc, "CITYNAME"));
//            rowNode.put("serviceCount", serviceCount);
//
//            List<Map<String, Object>> itemsNode = queryItemOfRegion(db, pServicesetIdList, regionCode);
//            rowNode.put("items", itemsNode);
//
//            rowsNode.add(rowNode);
//        }
//
//        return rowsNode;
//    }
    /**
     *
     * @param db
     * @param pServicesetIdList
     * @param pRegionCode
     * @return
     */
//    public List<Map<String, Object>> querySingleRegionService(DB db, List<Long> pServicesetIdList, String pRegionCode) {
//
//        List<Map<String, Object>> rowsNode = new LinkedList<>();
//
//        if (pServicesetIdList.isEmpty()) {
//            pServicesetIdList = new ArrayList<>();
//
//            DBObject query = new BasicDBObject();
//            query.put("region_code", pRegionCode);
//            query.put("enable", 1);
//
//            DBObject sort = new BasicDBObject();
//            sort.put("serviceset_id", 1);
//
//            List<DBObject> serviceList = db.getCollection(GABAggrConfiguration.table_gab_service_define).find(query).sort(sort).toArray();
//            for (DBObject serviceDoc : serviceList) {
//                long servicesetId = MongoUtil.getLongValue(serviceDoc, "serviceset_id");
//                if (pServicesetIdList.contains(servicesetId)) {
//                    continue;
//                }
//                pServicesetIdList.add(servicesetId);
//            }
//        }
//
//        for (Long servicesetId : pServicesetIdList) {
//            DBObject query = new BasicDBObject();
//            query.put("serviceset_id", servicesetId);
//
//            DBObject servicesetDoc = db.getCollection(GABAggrConfiguration.table_gab_serviceset_define).findOne(query);
//            if (servicesetDoc == null) {
//                continue;
//            }
//
//            query = new BasicDBObject();
//            query.put("region_code", pRegionCode);
//            query.put("serviceset_id", servicesetId);
//            query.put("enable", 1);
//
//            DBObject sort = new BasicDBObject();
//            sort.put("display_order", 1);
//
//            List<DBObject> serviceDocList = db.getCollection(GABAggrConfiguration.table_gab_service_define).find(query).sort(sort).toArray();
//            if (serviceDocList.isEmpty()) {
//                continue;
//            }
//
//            List<Map<String, Object>> servicesetItemsNode = new LinkedList<>();
//            for (DBObject serviceDoc : serviceDocList) {
//                Map<String, Object> serviceNode = new HashMap<>();
//                serviceNode.put("serviceId", MongoUtil.getLongValue(serviceDoc, "service_id"));
//                serviceNode.put("serviceName", MongoUtil.getStringValue(serviceDoc, "service_name"));
//                serviceNode.put("serviceDisplayName", MongoUtil.getStringValue(serviceDoc, "service_type_display_name"));
//
//                servicesetItemsNode.add(serviceNode);
//            }
//
//            String servicesetName = MongoUtil.getStringValue(servicesetDoc, "serviceset_name");
//            String servicesetDisplayName = MongoUtil.getStringValue(servicesetDoc, "serviceset_display_name");
//
//            Map<String, Object> servicesetNode = new HashMap<>();
//            servicesetNode.put("servicesetId", servicesetId);
//            servicesetNode.put("servicesetName", servicesetName);
//            servicesetNode.put("servicesetDisplayName", servicesetDisplayName);
//            servicesetNode.put("servicesetItems", servicesetItemsNode);
//
//            rowsNode.add(servicesetNode);
//        }
//
//        return rowsNode;
//    }
    /**
     *
     * @param dimension
     * @return
     */
    public List<Map<String, Object>> queryServiceOfDimensionGroupByRegion(DB db, String dimension) {

        List<Map<String, Object>> rowsNode = new LinkedList<>();

        //查询所有的区域信息
        DBObject query = new BasicDBObject();

        DBObject sort = new BasicDBObject();
        sort.put("PARENTID", 1);
        sort.put("CODEID", 1);

        List<DBObject> allRegionDocList = db.getCollection(GABAggrConfiguration.table_gab_region_define).find(query).sort(sort).toArray();

        Map<String, List<DBObject>> allRegionMap = new HashMap<>();//缓存所有的区域信息，并按上级区域分组
        for (DBObject regionDoc : allRegionDocList) {

            String PARENTID = MongoUtil.getStringValue(regionDoc, "PARENTID");

            List<DBObject> itemRegionList = allRegionMap.get(PARENTID);

            if (itemRegionList == null) {
                itemRegionList = new ArrayList<>();
            }
            itemRegionList.add(regionDoc);

            allRegionMap.put(PARENTID, itemRegionList);

        }

        //查询当前维度下所有的服务集信息
        query = new BasicDBObject();
        query.put("dimension_name", dimension);

        sort = new BasicDBObject();
        sort.put("display_order", 1);

        List<DBObject> servicesetDocList = db.getCollection(GABAggrConfiguration.table_gab_serviceset_define).find(query).sort(sort).toArray();

        List<Long> servicesetIdList = new ArrayList<>();

        Map<Long, DBObject> allServicesetMap = new HashMap<>();//缓存当前维度下所有的服务集信息   
        for (DBObject servicesetDoc : servicesetDocList) {
            long servicesetId = MongoUtil.getLongValue(servicesetDoc, "serviceset_id");
            allServicesetMap.put(servicesetId, servicesetDoc);

            if (!servicesetIdList.contains(servicesetId)) {
                servicesetIdList.add(servicesetId);
            }
        }

        //查询当前维度下所有的可用服务信息
        DBObject innerQuery = new BasicDBObject();
        innerQuery.put("$in", servicesetIdList.toArray());

        query = new BasicDBObject();
        query.put("serviceset_id", innerQuery);
        query.put("enable", 1);

        sort = new BasicDBObject();
        sort.put("region_code", 1);
        sort.put("display_order", 1);

        List<DBObject> allServiceList = db.getCollection(GABAggrConfiguration.table_gab_service_define).find(query).sort(sort).toArray();

        Map<String, List<DBObject>> allServiceMap = new HashMap<>();//缓存当前维度下所有的可用服务信息，并按区域分组
        for (DBObject serviceDoc : allServiceList) {

            String regionCode = MongoUtil.getStringValue(serviceDoc, "region_code");

            List<DBObject> itemServiceList = allServiceMap.get(regionCode);

            if (itemServiceList == null) {
                itemServiceList = new ArrayList<>();
            }
            itemServiceList.add(serviceDoc);

            allServiceMap.put(regionCode, itemServiceList);

        }

        //-----------------------------------
        String topParentId = "";
        List<DBObject> regionList = allRegionMap.get(topParentId);

        topParentId = "0";
        List<DBObject> itemRegionList = allRegionMap.get(topParentId);
        if (regionList == null && itemRegionList == null) {
            return rowsNode;
        }

        if (regionList == null && itemRegionList != null) {
            regionList = itemRegionList;
        } else if (regionList != null && itemRegionList != null) {
            regionList.addAll(itemRegionList);
        }

        for (DBObject regionDoc : regionList) {
            String regionCode = MongoUtil.getStringValue(regionDoc, "CODEID");
            List<DBObject> regionServiceDocList = allServiceMap.get(regionCode);// MongoOpt.find(db, QUERY_GAB_SERVICE_DEFINE_COLL_NAME, query, sort);

            List<Map<String, Object>> serviceItemsNode = new LinkedList<>();
            if (regionServiceDocList != null) {
                for (DBObject serviceDoc : regionServiceDocList) {
                    Map<String, Object> serviceNode = new HashMap<>();
                    serviceNode.put("serviceId", MongoUtil.getLongValue(serviceDoc, "service_id"));
                    serviceNode.put("serviceName", MongoUtil.getStringValue(serviceDoc, "service_name"));
                    serviceNode.put("serviceDisplayName", MongoUtil.getStringValue(serviceDoc, "service_region_display_name"));

                    serviceItemsNode.add(serviceNode);
                }
            }

            List<Map<String, Object>> itemsNode = new LinkedList<>();
            if (!("0".equals(regionCode))) {
                itemsNode.addAll(queryItemServiceOfRegion(db, regionCode, allRegionMap, allServicesetMap, allServiceMap));
            }
            if (serviceItemsNode.isEmpty() && itemsNode.isEmpty()) {
                continue;
            }

            Map<String, Object> regionNode = new HashMap<>();
            regionNode.put("regionCode", regionCode);
            regionNode.put("regionName", MongoUtil.getStringValue(regionDoc, "CITYNAME"));
            regionNode.put("count", serviceItemsNode.size());
            regionNode.put("serviceItems", serviceItemsNode);
            regionNode.put("items", itemsNode);

            rowsNode.add(regionNode);
        }

        return rowsNode;
    }

    /**
     *
     * @param db
     * @param pParentId
     * @param allRegionMap
     * @param allServicesetMap
     * @param allServiceMap
     * @return
     */
    private List<Map<String, Object>> queryItemServiceOfRegion(DB db, String pParentId, Map<String, List<DBObject>> allRegionMap, Map<Long, DBObject> allServicesetMap, Map<String, List<DBObject>> allServiceMap) {

        List<Map<String, Object>> rowsNode = new LinkedList<>();

        List<DBObject> itemRegionDocList = allRegionMap.get(pParentId);
        if (itemRegionDocList == null || itemRegionDocList.isEmpty()) {
            return rowsNode;
        }

        for (DBObject regionDoc : itemRegionDocList) {
            String regionCode = MongoUtil.getStringValue(regionDoc, "CODEID");

            List<DBObject> regionServiceDocList = allServiceMap.get(regionCode);

            List<Map<String, Object>> serviceItemsNode = new LinkedList<>();
            if (regionServiceDocList != null) {
                for (DBObject serviceDoc : regionServiceDocList) {
                    Map<String, Object> serviceNode = new HashMap<>();
                    serviceNode.put("serviceId", MongoUtil.getLongValue(serviceDoc, "service_id"));
                    serviceNode.put("serviceName", MongoUtil.getStringValue(serviceDoc, "service_name"));
                    serviceNode.put("serviceDisplayName", MongoUtil.getStringValue(serviceDoc, "service_region_display_name"));

                    serviceItemsNode.add(serviceNode);
                }
            }

            List<Map<String, Object>> itemsNode = queryItemServiceOfRegion(db, regionCode, allRegionMap, allServicesetMap, allServiceMap);
            if (serviceItemsNode.isEmpty() && itemsNode.isEmpty()) {
                continue;
            }

            Map<String, Object> regionNode = new HashMap<>();
            regionNode.put("regionCode", regionCode);
            regionNode.put("regionName", MongoUtil.getStringValue(regionDoc, "CITYNAME"));
            regionNode.put("count", serviceItemsNode.size());
            regionNode.put("serviceItems", serviceItemsNode);
            regionNode.put("items", itemsNode);

            rowsNode.add(regionNode);
        }

        return rowsNode;
    }

    /**
     *
     * @param db
     * @param pServicesetIdList
     * @param pRegionCode
     * @return
     */
//    public static long querySingleRegionServiceCount(DB db, List<Long> pServicesetIdList, String pRegionCode) {
//
//        DBObject query = new BasicDBObject();
//        query.put("region_code", pRegionCode);
//        query.put("enable", 1);
//
//        if (!pServicesetIdList.isEmpty()) {
//            DBObject innerQuery = new BasicDBObject();
//            innerQuery.put("$in", pServicesetIdList.toArray());
//
//            query.put("serviceset_id", innerQuery);
//        }
//
//        return db.getCollection(GABAggrConfiguration.table_gab_service_define).count(query);
//    }
}
