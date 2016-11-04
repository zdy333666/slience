/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.archive.service;

import com.hzcominfo.archive.conf.ArchiveConfiguration;
import com.hzcominfo.archive.conf.MongoSource;
import com.hzcominfo.archive.conf.MongoSourceFactory;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author cominfo4
 */
@Service
public class ArchiveService {

    public List<Long> fetchAttrSetIds(String dimension) throws Exception {

        DBObject query = new BasicDBObject();
        query.put("query_dimension_name", dimension);
        query.put("enable_flag", 1);

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("query_attrset_id", 1);

        DBObject sort = new BasicDBObject();
        sort.put("display_order", 1);

        MongoSource source = MongoSourceFactory.getSource(ArchiveConfiguration.MONGO_URI_RTDB);
        DB db = source.getClient().getDB(source.getClientURI().getDatabase());
        List<DBObject> docs = db.getCollection(ArchiveConfiguration.AGGREGATE_DIMENSION_ATTRSET).find(query, fields).sort(sort).toArray();

        NumberFormat numberFormat = NumberFormat.getInstance();
        List<Long> result = new ArrayList<>();
        for (DBObject doc : docs) {
            result.add(numberFormat.parse(String.valueOf(doc.get("query_attrset_id"))).longValue());
        }

        return result;
    }

}
