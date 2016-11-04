/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.photo.dao;

import com.hzcominfo.photo.conf.PhotoConfiguration;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zdy
 */
@Repository
public class JWRYPhotoDao {

    /**
     *
     * @param db
     * @param sfzh
     * @return
     */
    public byte[] excute(DB db, String sfzh) {

        DBObject query = new BasicDBObject();
        query.put("ZJHM", sfzh);

        DBObject sort = new BasicDBObject();
        sort.put("XCZFRQ", -1);

        DBObject fields = new BasicDBObject();
        fields.put("ZP", 1);

        DBObject doc = null;

        DBCollection coll = db.getCollection(PhotoConfiguration.table_photo_jwry);
        DBCursor cursor = coll.find(query, fields).sort(sort).limit(1);
        if (cursor.hasNext()) {
            doc = cursor.next();
        }
        cursor.close();

        if (doc == null) {
            return null;
        }

        byte[] photo = (byte[]) doc.get("ZP");
        if (photo == null || photo.length == 0) {
            return null;
        }

        return photo;
    }

}
