/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.service;

import com.hzcominfo.aggregate.base.dao.AggregateFieldsDao;
import com.hzcominfo.aggregate.base.dao.AggregateRowsDao;
import com.hzcominfo.aggregate.base.cache.AggregateCacheBuilder;
import com.hzcominfo.aggregate.base.cache.AggrModelCache;
import com.hzcominfo.aggregate.base.cache.AggrSetCache;
import com.hzcominfo.aggregate.base.pojo.AggregateInput;
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
    private AggregateFieldsDao _fieldsDao;

    @Autowired
    private AggregateRowsDao _rowsDao;

    /**
     *
     * @param input
     * @return
     */
    public Map<String, Object> service(AggregateInput input) {

        Map<String, Object> result = null;

        try {
            long dimensionId = input.getDimensionId();
            AggrSetCache cache = _cacheBuilder.buildCache(input);
            Map<Long, AggrModelCache> aggrModelCaches = cache.getAggrModelCaches();
            if (aggrModelCaches == null) {
                return null;
            }

            long aggrSetId = input.getSetId();
            AggrModelCache aggrSetCache = aggrModelCaches.get(aggrSetId);
            if (aggrSetCache == null) {
                return null;
            }

            //获取fields节点
            long startTime = System.currentTimeMillis();
            Map<String, Object> fields = _fieldsDao.getFields(cache);
            logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- Aggregate Query fields -- ").append(dimensionId).append(" -- ").append(aggrSetId).append(" spend time ---> ").append(System.currentTimeMillis() - startTime).append(" ms").toString());

            //获取rows节点
            startTime = System.currentTimeMillis();

            Map<String, Object> rows = _rowsDao.getRows(input, cache);

            logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- Aggregate Query rows -- ").append(dimensionId).append(" -- ").append(aggrSetId).append(" spend time ---> ").append(System.currentTimeMillis() - startTime).append(" ms").toString());

            result = new HashMap<>();
            result.put("fields", fields);
            result.put("rows", rows);
        } catch (Exception e) {
            logger.error(null, e);
        }

        return result;
    }

}
