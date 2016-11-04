/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabaggr.dao;

import com.hzcominfo.gabaggr.conf.GABAggrConfiguration;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class GABAggrResultDao {

    /**
     *
     * @param db
     * @param sessionId
     * @param size
     */
    public void setBegin(DB db, String sessionId, int size) {

        DBObject query = new BasicDBObject("_id", sessionId);

        DBObject obj = new BasicDBObject();
        obj.put("$set", new BasicDBObject("ok", false).append("size", size).append("items", new BasicDBList()));

        db.getCollection(GABAggrConfiguration.table_gab_result).update(query, obj, true, false);

    }

    /**
     *
     * @param db
     * @param sessionId
     * @param rs
     */
    public void save(DB db, String sessionId, Map<String, Object> rs) {

        DBObject query = new BasicDBObject("_id", sessionId);

        DBObject obj = new BasicDBObject();
        obj.put("$push", new BasicDBObject("items", rs));

        db.getCollection(GABAggrConfiguration.table_gab_result).update(query, obj, false, false);

    }

    /**
     *
     * @param db
     * @param sessionId
     */
    public void setOk(DB db, String sessionId) {

        DBObject query = new BasicDBObject("_id", sessionId);

        DBObject obj = new BasicDBObject();
        obj.put("$set", new BasicDBObject("ok", true).append("createdAt", new Date(System.currentTimeMillis() + 1000 * GABAggrConfiguration.gab_result_expired_seconds)));

        db.getCollection(GABAggrConfiguration.table_gab_result).update(query, obj, false, false);
    }

    /**
     *
     * @param db
     * @param sessionId
     * @return
     */
    public Map<String, Object> query(DB db, String sessionId) {

        DBObject query = new BasicDBObject("_id", sessionId);

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("items", 1);
        fields.put("size", 1);
        fields.put("ok", 1);

        DBObject doc = db.getCollection(GABAggrConfiguration.table_gab_result).findOne(query, fields);

        return doc == null ? null : doc.toMap();
    }

}
