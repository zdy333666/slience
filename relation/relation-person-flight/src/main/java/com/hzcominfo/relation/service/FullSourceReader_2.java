/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.service;

import com.hzcominfo.relation.conf.MongoSource;
import com.hzcominfo.relation.conf.MongoSourceFactory;
import com.hzcominfo.relation.conf.RelationConfiguration;
import com.hzcominfo.relation.dao.SavePointDao;
import com.hzcominfo.relation.pojo.SavePoint;
import com.hzcominfo.relation.util.TupleChecker_2;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class FullSourceReader_2 {

    private Logger logger = LoggerFactory.getLogger(FullSourceReader_2.class);
    private StringBuilder logBuilder = new StringBuilder();

    private boolean complete;
    private final AtomicLong counter = new AtomicLong(0L);

    private String table;
    private DBObject query = new BasicDBObject();
    private DBObject fields = new BasicDBObject();
    private DBObject sort = new BasicDBObject();
    private final String sortField = "ETL_TIMESTAMP";
    private int skip = 0;
    private int limit = 2_0000;

    private SavePoint savePoint = new SavePoint();
    private Date savePointValue;

    private MongoSource source = null;
    private List<DBObject> docs = null;
    private Map<String, Object> tuple = null;
    private int size;

    private boolean ok;
    private Date tempSavePoint;

    private SimpleDateFormat dateFormat_S = new SimpleDateFormat("yyyyMMddHHmmssS");

    @Autowired
    private BoltAsynRelationBuilder relationBuilder;

    @Autowired
    private SavePointDao savePointDao;

    public FullSourceReader_2() {
        fields.put("_id", 0);
        fields.put("FOID", 1);
        fields.put("CHINESE_NAME", 1);
        fields.put("ENGLISH_NAME", 1);
        fields.put("FLIGHT", 1);
        fields.put("SEAT", 1);
        fields.put("STRT_NAME", 1);
        fields.put("DEST_NAME", 1);
        fields.put("OFFDAY", 1);
        fields.put(sortField, 1);

        sort.put(sortField, 1);

        table = RelationConfiguration.mongo_source;
        savePoint.setType(RelationConfiguration.relation_type);
        savePoint.setTable(table);
    }

    /**
     *
     */
    //@Scheduled(fixedDelay = 1_000)
    public void read() {

        if (relationBuilder.isOk()) {

            //relationBuilder.produceMessages();

            if (complete) {
                logger.info(logBuilder.delete(0, logBuilder.length()).append(" ------------ full source process is completed").toString());
                System.exit(0);
            }

            //logger.info(counter.get() + " ------------------- queue is ready ................ ");
            if (savePointValue != null) {
                savePoint.setValue(dateFormat_S.format(savePointValue));
                savePoint.setSkip(skip);
                savePointDao.set(savePoint);
            } else {
                savePoint = savePointDao.get(savePoint);

                String value = savePoint.getValue();
                if (!(value == null || value.trim().isEmpty())) {
                    try {
                        savePointValue = dateFormat_S.parse(value);
                        skip = savePoint.getSkip();
                    } catch (ParseException ex) {
                        logger.error(null, ex);
                    }
                }
            }

            if (savePointValue != null) {
                query.put(sortField, new BasicDBObject().append("$gte", savePointValue));
            } else {
                query.put(sortField, new BasicDBObject().append("$ne", savePointValue));
            }

            logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- table -->").append(table).toString());
            logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- query -->").append(query).toString());
            logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- fields -->").append(fields).toString());
            logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- sort -->").append(sort).toString());
            logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- skip -->").append(skip).toString());
            logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- limit -->").append(limit).toString());

            source = MongoSourceFactory.getSource(RelationConfiguration.mongo_uri_hzga);

            try {
                docs = source.getClient().getDB(source.getClientURI().getDatabase()).getCollection(table).find(query, fields).sort(sort).skip(skip).limit(limit).toArray();//.addOption(Bytes.QUERYOPTION_NOTIMEOUT);
                size = docs.size();
                while (!docs.isEmpty()) {
                    tuple = docs.get(0).toMap();
                    tempSavePoint = (Date) tuple.get(sortField);

                    ok = false;
                    tuple = TupleChecker_2.check(tuple);
                    if (tuple == null) {
                        docs.remove(0);
                        ok = true;
                    } else if (relationBuilder.offer(tuple)) {
                        docs.remove(0);
                        ok = true;
                    }
                    tuple = null;

                    if (ok) {
                        counter.incrementAndGet();
                        if (tempSavePoint == null || tempSavePoint.equals(savePointValue)) {
                            skip++;
                        } else {
                            skip = 0;
                            savePointValue = tempSavePoint;
                        }
                    } else {
                        Thread.sleep(10);
                    }
                }

                docs = null;
                logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- full source spout to --> ").append(counter.get()).toString());

                if (size < limit) {
                    complete = true;
                }

            } catch (InterruptedException ex) {
                logger.error(null, ex);
            }
        }

    }

}
