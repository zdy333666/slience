/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabaggr.dao;

import com.hzcominfo.gabaggr.conf.GABAggrConfiguration;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class AuthQuery {

    /**
     *
     * @param db
     * @param userId
     * @return
     */
    public Map<String, Object> getUserInfo(DB db, String userId) {

        DBObject query = new BasicDBObject("userID", userId);

        DBObject fields = new BasicDBObject("_id", 0);
        fields.put("pkiSfzh", 1);
        fields.put("pkiName", 1);
        fields.put("userDeptCode", 1);

        DBObject doc = db.getCollection(GABAggrConfiguration.table_auth_user).findOne(query, fields);

        return doc == null ? null : doc.toMap();
    }

}
