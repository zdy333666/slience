/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.rchl.dao;

import com.hzcominfo.rchl.conf.RCHLConfiguration;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class DPTCodeDao {

    /**
     * 
     * @param db
     * @param dm
     * @return 
     */
    public Map<String, Map<String, String>> queryItems(DB db, String dm) {

        Map<String, Map<String, String>> result = new TreeMap<>();

        DBObject query = new BasicDBObject();
        query.put("parent", dm);

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);

        List<DBObject> docs = db.getCollection(RCHLConfiguration.table_code_dpt).find(query, fields).sort(new BasicDBObject("DM", 1)).toArray();

        if (docs.isEmpty()) {
            DBObject doc = db.getCollection(RCHLConfiguration.table_code_dpt).findOne(new BasicDBObject("DM", dm), fields);
            if (doc != null) {
                Map<String, String> item = new HashMap<>();
                String bmcode = (String) doc.get("DM");

                item.put("bmcode", bmcode);
                item.put("bm", (String) doc.get("MC"));
                result.put(bmcode, item);
            }
        } else {
            for (DBObject doc : docs) {
                Map<String, String> item = new HashMap<>();
                String bmcode = (String) doc.get("DM");
                if (bmcode == null || bmcode.trim().isEmpty()) {
                    continue;
                }

                item.put("bmcode", bmcode);
                item.put("bm", (String) doc.get("MC"));
                result.put(bmcode, item);
            }
        }

        return result;
    }

}
