/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.subject.scheduler;

import com.hzcominfo.subject.conf.MongoSource;
import com.hzcominfo.subject.conf.MongoSourceFactory;
import com.hzcominfo.subject.conf.SubjectConfiguration;
import com.hzcominfo.subject.pojo.SavePoint;
import com.hzcominfo.subject.util.TupleChecker;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.bson.types.ObjectId;
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

    private Logger logger = LoggerFactory.getLogger(FullSourceReader.class);
    private StringBuilder logBuilder = new StringBuilder();

    private boolean complete;
    private final AtomicLong counter = new AtomicLong(0L);

    private String table;
    private DBObject query = new BasicDBObject();
    private DBObject fields = new BasicDBObject();
    private DBObject sort = new BasicDBObject();
    private final String sortField = "_id";
    private int skip = 0;
    private int limit = 2_0000;

    private SavePoint savePoint = new SavePoint();
    private ObjectId savePointValue;

    private MongoSource source = null;
    private List<DBObject> docs = null;
    private Map<String, Object> tuple = null;
    private int size;

    private boolean ok;
    private ObjectId tempSavePoint;

    @Autowired
    private AsynBuilder asynBuilder;

    public FullSourceReader() {

        sort.put(sortField, 1);

        table = SubjectConfiguration.table_people_base;
        savePoint.setTable(table);
    }

    /**
     *
     */
    @Scheduled(fixedDelay = 1_000)
    public void read() {

        if (asynBuilder.isOk()) {

            if (complete) {
                logger.info(logBuilder.delete(0, logBuilder.length()).append(" ------------ full source process is completed").toString());
                System.exit(0);
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

            source = MongoSourceFactory.getSource(SubjectConfiguration.mongo_uri_people);

            try {
                docs = source.getClient().getDB(source.getClientURI().getDatabase()).getCollection(table).find(query, fields).sort(sort).skip(skip).limit(limit).toArray();//.addOption(Bytes.QUERYOPTION_NOTIMEOUT);
                size = docs.size();
                while (!docs.isEmpty()) {
                    tuple = docs.get(0).toMap();
                    tempSavePoint = (ObjectId) tuple.get(sortField);

                    ok = false;
                    tuple = TupleChecker.check(tuple);
                    if (tuple == null) {
                        docs.remove(0);
                        ok = true;
                    } else if (asynBuilder.offer(tuple)) {
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
                        Thread.sleep(100);
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
