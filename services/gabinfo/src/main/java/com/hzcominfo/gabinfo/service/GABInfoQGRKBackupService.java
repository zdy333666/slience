/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabinfo.service;

import com.hzcominfo.gabinfo.conf.GABInfoConfiguration;
import com.hzcominfo.gabinfo.conf.MongoSource;
import com.hzcominfo.gabinfo.conf.MongoSourceFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 *
 * @author cominfo4
 */
@Service
public class GABInfoQGRKBackupService {

    private final Logger logger = LoggerFactory.getLogger(GABInfoQGRKBackupService.class);

    /**
     *
     * @param sfzh
     * @param fields
     * @return
     * @throws java.lang.Exception
     */
    public Map<String, String> find(String sfzh, String[] fields) throws Exception {

        DBObject query = new BasicDBObject();
        query.put("SFZH", sfzh);
        query.put("enable", -1);

        DBObject fieldsDoc = new BasicDBObject();
        fieldsDoc.put("_id", 0);
        if ((fields != null) && (fields.length > 0)) {
            for (String field : fields) {
                fieldsDoc.put(field, 1);
            }
        }

        try {
            MongoSource source = MongoSourceFactory.getSource(GABInfoConfiguration.MONGO_URI_HZGA);
            DB db = source.getClient().getDB(source.getClientURI().getDatabase());
            DBObject doc = db.getCollection(GABInfoConfiguration.TABLE_GAB_QGRK_BACKUP).findOne(query, fieldsDoc);
            if (doc != null) {
                return doc.toMap();
            }
        } catch (Exception e) {
            logger.error(null, e);
        }

        return null;
    }

    /**
     *
     * @param doc
     */
    @Async
    public void save(Map<String, String> doc) {

        String sfzh = doc.get("SFZH");
        if (sfzh == null || sfzh.trim().isEmpty()) {
            return;
        }

        DBObject query = new BasicDBObject();
        query.put("SFZH", sfzh);
        query.put("enable", -1);

        DBObject obj = new BasicDBObject(doc);
        obj.put("add_time", new Date());

        String XP = doc.get("XP");
        if (!(XP == null || XP.trim().isEmpty())) {
            obj.put("XP", Base64.getDecoder().decode(XP));
        }
        try {
            MongoSource source = MongoSourceFactory.getSource(GABInfoConfiguration.MONGO_URI_HZGA);
            DB db = source.getClient().getDB(source.getClientURI().getDatabase());
            db.getCollection(GABInfoConfiguration.TABLE_GAB_QGRK_BACKUP).update(query, new BasicDBObject().append("$set", obj), true, true);
        } catch (Exception e) {
            logger.error(null, e);
        }
    }

}
