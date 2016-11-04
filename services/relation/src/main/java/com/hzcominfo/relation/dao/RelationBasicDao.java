/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import com.hzcominfo.relation.pojo.PeopleCriminalStatus;
import com.hzcominfo.relation.pojo.RelationDefine;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class RelationBasicDao {

    /**
     *
     * @param coll
     * @return
     */
    public List<RelationDefine> getRelationDefines(DBCollection coll) {

        DBObject query = new BasicDBObject();
        query.put("enable", 1);

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("name", 1);
        fields.put("display_name", 1);
        fields.put("weight", 1);

        DBObject sort = new BasicDBObject();
        sort.put("display_order", 1);

        List<DBObject> docs = coll.find(query, fields).sort(sort).toArray();

        List<RelationDefine> result = new LinkedList<>();
        for (DBObject doc : docs) {

            String name = (String) doc.get("name");
            String displayName = (String) doc.get("display_name");
            int weight = (int) doc.get("weight");

            RelationDefine relationDefine = new RelationDefine(name, displayName, weight);
            result.add(relationDefine);
        }

        return result;
    }

    /**
     *
     * @param coll
     * @param sfzh
     * @return
     */
    public Map<String, Object> getPeopleBaseInfo(DBCollection coll, String sfzh, String[] fieldArr) {

        DBObject query = new BasicDBObject();
        query.put("SFZH", sfzh);

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        for (String field : fieldArr) {
            fields.put(field, 1);
        }

        DBObject doc = coll.findOne(query, fields);
        if (doc != null) {
            return doc.toMap();
        }

        return null;
    }

    /**
     *
     * @param coll
     * @param sfzh
     * @return
     */
    public PeopleCriminalStatus checkPeopleCriminal(DBCollection coll, String sfzh) {

        DBObject query = new BasicDBObject();
        query.put("SFZH", sfzh);

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("SFZH", 1);
        fields.put("MODEL_ID", 1);

        DBObject doc = coll.findOne(query, fields);

        PeopleCriminalStatus status = new PeopleCriminalStatus();
        if (doc == null) {
            return status;
        }

        status.setIsCriminal(true);

        List<String> modelIds = (List<String>) doc.get("MODEL_ID");
        if (modelIds == null) {
            return status;
        }

        if (modelIds.contains("16")) {
            status.setIsCriminalDrug(true);
        }

        return status;
    }

}
