/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.service;

import com.hzcominfo.relation.util.TupleChecker;
import com.hzcominfo.relation.conf.MongoSource;
import com.hzcominfo.relation.conf.MongoSourceFactory;
import com.hzcominfo.relation.conf.RelationConfiguration;
import com.hzcominfo.relation.dao.SavePointDao;
import com.hzcominfo.relation.pojo.SavePoint;
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
public class FullSourceReader {

    private final Logger logger = LoggerFactory.getLogger(FullSourceReader.class);
    private final StringBuilder logBuilder = new StringBuilder();

    private final AtomicLong counter = new AtomicLong(0L);
    private boolean complete = false;

    private final String table;
    private final DBObject query = new BasicDBObject();
    private final DBObject fields = new BasicDBObject();
    private final DBObject sort = new BasicDBObject();
    private final String sortField = "ETL_TIMESTAMP";
    private int skip = 0;
    private final int limit = 20_000;

    private SavePoint savePoint = new SavePoint();
    private Date savePointValue;

    private MongoSource source = null;
    private List<DBObject> docs = null;
    private Map<String, Object> tuple = null;
    private int size;

    private boolean ok;
    private Date tempSavePoint;

    private final SimpleDateFormat dateFormat_S = new SimpleDateFormat("yyyyMMddHHmmssS");

    @Autowired
    private BoltAsynRelationBuilder relationBuilder;

    @Autowired
    private SavePointDao savePointDao;

    public FullSourceReader() {
        fields.put("_id", 0);
        fields.put("ZJHM", 1);
        fields.put("XM", 1);
        fields.put("ZJLX", 1);
        fields.put("RZSJ", 1);
        fields.put("LDSJ", 1);
        fields.put("FH", 1);
        fields.put("LGDM", 1);
        fields.put("LGDM_LGMC_FORMAT", 1);
        fields.put(sortField, 1);

        sort.put(sortField, 1);

        table = RelationConfiguration.mongoSource;
        savePoint.setType(RelationConfiguration.relationType);
        savePoint.setTable(table);
    }

    /**
     *
     */
    //@Scheduled(fixedDelay = 2000)
    public void read() {

//        if (complete) {
//            logger.info(logBuilder.delete(0, logBuilder.length()).append(" ------------ full source process is completed").toString());
//            try {
//                Thread.sleep(10 * 60 * 1000);
//            } catch (InterruptedException ex) {
//                logger.error(null, ex);
//            }
//            //complete = false;
//            return;
//        }
        if (relationBuilder.isOk()) {

            relationBuilder.produceMessages();

            if (complete) {
                logger.info(logBuilder.delete(0, logBuilder.length()).append(" ------------ full source process is completed").toString());
                System.exit(0);
            }

            logger.info(counter.get() + " ------------------- queue is ready ................ ");

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
                    tuple = TupleChecker.check(tuple);;
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
                        Thread.sleep(1000);
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
