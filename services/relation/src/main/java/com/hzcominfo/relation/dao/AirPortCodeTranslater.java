/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

/**
 *
 * @author cominfo4
 */
public class AirPortCodeTranslater {

    public String translate(DBCollection coll, String code) {

        DBObject query = new BasicDBObject();
        query.put("AIRPORT_IATA_CD", code);

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("AIRPORT_CHN_NM", 1);

        DBObject doc = coll.findOne(query, fields);

        return doc == null ? "" : (String) doc.get("AIRPORT_CHN_NM");
    }

}
