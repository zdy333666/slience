/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.service;

import com.hzcominfo.aggregate.dao.FetchFieldsDao;
import com.hzcominfo.aggregate.dao.FetchRowsDao;
import com.hzcominfo.aggregate.cache.AggregateCacheBuilder;
import com.hzcominfo.aggregate.cache.AttrSetCache;
import com.hzcominfo.aggregate.cache.DimensionCache;
import com.hzcominfo.aggregate.pojo.AggregateInput;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zdy
 */
@Service
public class AggregateService {

    private final Logger logger = LoggerFactory.getLogger(AggregateService.class);

    @Autowired
    private AggregateCacheBuilder _cacheBuilder;

    @Autowired
    private FetchFieldsDao _fieldsDao;

    @Autowired
    private FetchRowsDao _rowsDao;

    /**
     *
     * @param input
     * @return
     */
    public Map<String, Object> service(AggregateInput input) {

        Map<String, Object> result = null;

        try {
            String dimension = input.getDimension();
            DimensionCache dimensionCache = _cacheBuilder.buildCache(dimension, input.getAttrSetId());
            Map<Long, AttrSetCache> attrSetCaches = dimensionCache.getAttrSetCaches();
            if (attrSetCaches == null) {
                return null;
            }

            long attrSetId = input.getAttrSetId();
            AttrSetCache attrSetCache = attrSetCaches.get(attrSetId);
            if (attrSetCache == null) {
                return null;
            }

            //获取fields节点
            long startTime = System.currentTimeMillis();
            Map<String, Object> fields = _fieldsDao.getFields(attrSetCache);
            logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- Aggregate Query fields -- ").append(dimension).append(" -- ").append(attrSetId).append(" spend time ---> ").append(System.currentTimeMillis() - startTime).append(" ms").toString());

            //获取rows节点
            startTime = System.currentTimeMillis();

            Map<String, Object> rows = _rowsDao.getRows(input, attrSetCache);

            logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- Aggregate Query rows -- ").append(dimension).append(" -- ").append(attrSetId).append(" spend time ---> ").append(System.currentTimeMillis() - startTime).append(" ms").toString());

            result = new HashMap<>();
            result.put("fields", fields);
            result.put("rows", rows);
        } catch (Exception e) {
            logger.error(null, e);
        }

        return result;
    }

}
