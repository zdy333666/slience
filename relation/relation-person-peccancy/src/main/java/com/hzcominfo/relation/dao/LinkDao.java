/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import com.hzcominfo.relation.util.TupleChecker;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.Map;

/**
 *
 * @author cominfo4
 */
public class LinkDao {

    public static Map<String, Object> getCarOwner(DBCollection coll, String cphm, String hpzl) {
        DBObject query = new BasicDBObject();
        query.put("CPHM", cphm);
        query.put("HPZL", hpzl);
        query.put("SFZMMC", "A");

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("SFZMHM", 1);
        fields.put("SYR", 1);

        DBObject sort = new BasicDBObject();
        sort.put("FDJRQ", -1);

        DBObject doc = coll.findOne(query, fields, sort);

        return doc == null ? null : doc.toMap();
    }

}
