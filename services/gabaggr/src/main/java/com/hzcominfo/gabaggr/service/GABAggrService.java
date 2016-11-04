/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabaggr.service;

import com.dragonsoft.pci.exception.InvokeServiceException;
import com.hzcominfo.gabaggr.conf.GABAggrConfiguration;
import com.hzcominfo.gabaggr.conf.MongoSource;
import com.hzcominfo.gabaggr.conf.MongoSourceFactory;
import com.hzcominfo.gabaggr.dao.GABAggrMetaDao;
import com.hzcominfo.gabaggr.dao.GABAggrResultDao;
import com.mongodb.DB;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zdy
 */
@Service
public class GABAggrService {

    @Autowired
    private GABAggrMetaDao metaDao;

    @Autowired
    private GABAggrResultDao _GABAggrResultDao;

    /**
     *
     * @param dimension
     * @return
     */
    public List<Map<String, Object>> queryServicesetOfDimension(String dimension) {

        MongoSource source = MongoSourceFactory.getSource(GABAggrConfiguration.mongo_uri_rtdb);
        DB db = source.getClient().getDB(source.getClientURI().getDatabase());

        return metaDao.queryServicesetOfDimension(db, dimension);
    }

    /**
     *
     * @param dimension
     * @return
     */
    public List<Map<String, Object>> queryServiceOfDimensionGroupByRegion(String dimension) {

        MongoSource source = MongoSourceFactory.getSource(GABAggrConfiguration.mongo_uri_rtdb);
        DB db = source.getClient().getDB(source.getClientURI().getDatabase());

        return metaDao.queryServiceOfDimensionGroupByRegion(db, dimension);
    }

    /**
     *
     * @param input
     * @return @throws InvokeServiceException
     * @throws DocumentException
     */
    public Map<String, Object> fetch(String sessionId) throws Exception {

        MongoSource source = MongoSourceFactory.getSource(GABAggrConfiguration.mongo_uri_rtdb);
        DB db = source.getClient().getDB(source.getClientURI().getDatabase());

        Map<String, Object> fetchData = _GABAggrResultDao.query(db, sessionId);
        if (fetchData == null) {
            return fetchData;
        }

        List<Map<String, Object>> items = (List<Map<String, Object>>) fetchData.get("items");

        Map<Long, Map<String, Object>> servicesetDataGroup = new HashMap<>();
        for (Map<String, Object> serviceData : items) {

            if (serviceData == null) {
                continue;
            }
            long localServiceSetId = (long) serviceData.get("servicesetId");
            String localServiceSetName = (String) serviceData.get("servicesetName");
            String localServiceSetDisplayName = (String) serviceData.get("servicesetDisplayName");

            String localRegionCode = (String) serviceData.get("regionCode");
            String localRegionName = (String) serviceData.get("regionName");
            String localServiceName = (String) serviceData.get("serviceName");
            String localServiceDisplayName = (String) serviceData.get("serviceDisplayName");

            List<Map<String, Object>> localServiceItems = (List<Map<String, Object>>) serviceData.get("serviceItems");

            Map<String, Object> resOneServiceNode = new HashMap<>();
            resOneServiceNode.put("regionCode", localRegionCode);
            resOneServiceNode.put("regionName", localRegionName);
            resOneServiceNode.put("serviceName", localServiceName);
            resOneServiceNode.put("serviceDisplayName", localServiceDisplayName);
            resOneServiceNode.put("serviceItems", localServiceItems);

            if (!servicesetDataGroup.containsKey(localServiceSetId)) {

                Map<String, Object> localServiceSetNode = new HashMap<>();
                localServiceSetNode.put("servicesetId", localServiceSetId);
                localServiceSetNode.put("servicesetName", localServiceSetName);
                localServiceSetNode.put("servicesetDisplayName", localServiceSetDisplayName);

                List<Map<String, Object>> localServiceSetItemsNode = new ArrayList<>();
                localServiceSetItemsNode.add(resOneServiceNode);
                localServiceSetNode.put("servicesetItems", localServiceSetItemsNode);
                //localInfoSessionParamNew.setQueryTimes(localInfoSessionParamNew.getQueryTimes()+1);
                //localServiceSetNode.put("serviceSetOrder",localInfoSessionParamNew.getQueryTimes());

                servicesetDataGroup.put(localServiceSetId, localServiceSetNode);
            } else {
                Map<String, Object> localServiceSetNode = servicesetDataGroup.get(localServiceSetId);
                List<Map<String, Object>> localServiceSetItemsNode = (List<Map<String, Object>>) localServiceSetNode.get("servicesetItems");
                localServiceSetItemsNode.add(resOneServiceNode);
            }
        }

        fetchData.put("items", servicesetDataGroup.values());
        fetchData.put("dealed", items.size());

        return fetchData;
    }

}
