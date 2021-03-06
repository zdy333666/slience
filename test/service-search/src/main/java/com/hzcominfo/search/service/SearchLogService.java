/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.service;

import com.hzcominfo.search.conf.SearchConfiguration;
import com.hzcominfo.search.conf.MongoSource;
import com.hzcominfo.search.conf.MongoSourceFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author zdy
 */
@Service
public class SearchLogService {

    private final Logger logger = LoggerFactory.getLogger(SearchLogService.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    @Async
    public void log(Map<String, Object> log) {

        try {
            DBObject doc = new BasicDBObject(log);
            //doc.put("NUM_ID", null);
            //doc.put("REG_ID", null);
            doc.put("OPERATE_TIME", sdf.format(new Date()));

            logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- Aggregate Log -- ").append(doc).toString());

//        MongoSource source = MongoSourceFactory.getSource(SearchConfiguration.MONGO_URI_RTDB);
//        DB db = source.getClient().getDB(source.getClientURI().getDatabase());
//        db.getCollection(SearchConfiguration.AGGREGATE_LOG).insert(doc);
        } catch (Exception e) {
            logger.error(null, e);
        }
    }

}
