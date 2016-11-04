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
public class JCZCodeDao {

    public Map<String, Map<String, String>> queryAll(DB db) {

        DBObject query = new BasicDBObject();

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("JCZDM", 1);
        fields.put("JCZMC", 1);

        List<DBObject> docs = db.getCollection(RCHLConfiguration.table_code_jcz).find(query, fields).sort(new BasicDBObject("JCZDM", 1)).toArray();

        Map<String, Map<String, String>> result = new TreeMap<>();
        for (DBObject doc : docs) {

            String bmcode = (String) doc.get("JCZDM");
            if (bmcode == null || bmcode.trim().isEmpty()) {
                continue;
            }

            Map<String, String> item = new HashMap<>();
            item.put("bmcode", bmcode);
            item.put("bm", (String) doc.get("JCZMC"));
            result.put(bmcode, item);
        }

        return result;
    }

}
