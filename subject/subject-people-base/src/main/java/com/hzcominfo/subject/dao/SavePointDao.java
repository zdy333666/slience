/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.subject.dao;

import com.hzcominfo.subject.conf.MongoSource;
import com.hzcominfo.subject.conf.MongoSourceFactory;
import com.hzcominfo.subject.pojo.SavePoint;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
//@Component
public class SavePointDao {

    private final Logger logger = LoggerFactory.getLogger(SavePointDao.class);

    /**
     *
     * @param query
     * @param doc
     */
    public void set(SavePoint savePoint) {

        DBObject query = new BasicDBObject();
        query.put("type", savePoint.getType());
        query.put("table", savePoint.getTable());

        DBObject doc = new BasicDBObject();
        doc.put("value", savePoint.getValue());
        doc.put("skip", savePoint.getSkip());

        try {
            //MongoSource source = MongoSourceFactory.getSource(RelationConfiguration.mongo_uri_rtdb);
            //source.getClient().getDB(source.getClientURI().getDatabase()).getCollection(RelationConfiguration.relation_savepoint_table).update(query, new BasicDBObject().append("$set", doc), true, false);

        } catch (Exception ex) {
            logger.error(null, ex);
        }
    }

    /**
     *
     * @param query
     */
    public SavePoint get(SavePoint savePoint) {

        DBObject query = new BasicDBObject();
        query.put("type", savePoint.getType());
        query.put("table", savePoint.getTable());

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("value", 1);
        fields.put("skip", 1);

        try {
            //MongoSource source = MongoSourceFactory.getSource(RelationConfiguration.mongo_uri_rtdb);
            //DBObject doc = source.getClient().getDB(source.getClientURI().getDatabase()).getCollection(RelationConfiguration.relation_savepoint_table).findOne(query, fields);
//            if (doc != null) {
//                savePoint.setValue(doc.get("value"));
//                savePoint.setSkip((int) doc.get("skip"));
//            }
        } catch (Exception ex) {
            logger.error(null, ex);
        }

        return savePoint;
    }

}
