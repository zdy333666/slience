package com.hzcominfo.search.log;

import com.mongodb.BasicDBObject;
import com.mongodb.Bytes;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import java.util.ArrayList;
import java.util.List;

/*
 * @author xxx
 */
public class MongoOpt {

    public static DBObject findOne(DB db, String collName, DBObject query) {
        DBObject obj = null;

        if (db == null || collName == null || collName.trim().isEmpty()) {
            return obj;
        }

        DBCollection coll = db.getCollection(collName);

        DBCursor cursor = coll.find(query);
        cursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT);

        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            if (doc == null) {
                continue;
            }

            obj = doc;
            break;
        }
        cursor.close();

        return obj;
    }

    public static DBObject findOne(DB db, String collName, DBObject query, DBObject sort) {
        DBObject obj = null;

        if (db == null || collName == null || collName.trim().isEmpty()) {
            return obj;
        }

        DBCollection coll = db.getCollection(collName);

        DBCursor cursor = coll.find(query).sort(sort);
        cursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT);

        while (cursor.hasNext()) {
            DBObject doc = cursor.next();
            if (doc == null) {
                continue;
            }

            obj = doc;
            break;
        }
        cursor.close();

        return obj;
    }

    public static List<DBObject> find(DB db, String collName, DBObject query) {

        List<DBObject> docList = new ArrayList<DBObject>();
        if (db == null || collName == null || collName.trim().isEmpty()) {
            return docList;
        }

        DBCollection coll = db.getCollection(collName);

        DBCursor cursor = coll.find(query);
        cursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT);

        while (cursor.hasNext()) {
            DBObject doc = cursor.next();

            docList.add(doc);
        }
        cursor.close();

        return docList;
    }
    
    public static List<DBObject> find(DB db, String collName, DBObject query, DBObject sort) {

        List<DBObject> docList = new ArrayList<DBObject>();
        if (db == null || collName == null || collName.trim().isEmpty()) {
            return docList;
        }

        DBCollection coll = db.getCollection(collName);

        DBCursor cursor = coll.find(query).sort(sort);
        cursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT);

        while (cursor.hasNext()) {
            DBObject doc = cursor.next();

            docList.add(doc);
        }
        cursor.close();

        return docList;
    }

    public static List<DBObject> find(DB db, String collName, DBObject query, DBObject sort, int nLimit) {

        List<DBObject> docList = new ArrayList<DBObject>();
        if (db == null || collName == null || collName.trim().isEmpty()) {
            return docList;
        }

        DBCollection coll = db.getCollection(collName);

        DBCursor cursor = coll.find(query).sort(sort).limit(nLimit);
        cursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT);

        while (cursor.hasNext()) {
            DBObject doc = cursor.next();

            docList.add(doc);
        }
        cursor.close();

        return docList;
    }

    public static List<DBObject> find(DB db, String collName, DBObject query, DBObject sort, int nSkip, int nLimit) {

        List<DBObject> docList = new ArrayList<DBObject>();
        if (db == null || collName == null || collName.trim().isEmpty()) {
            return docList;
        }

        DBCollection coll = db.getCollection(collName);

        DBCursor cursor = coll.find(query).sort(sort).skip(nSkip).limit(nLimit);
        cursor.addOption(Bytes.QUERYOPTION_NOTIMEOUT);

        int n = 0;
        while (cursor.hasNext()) {
            if (!(n > nSkip)) {
                n++;
                continue;
            }

            DBObject doc = cursor.next();
            docList.add(doc);
        }
        cursor.close();

        return docList;
    }

    public static boolean insert(DB db, String collName, DBObject docParam) {
        boolean b = false;
        if (db == null || collName == null || collName.trim().isEmpty()) {
            return b;
        }

        DBCollection coll = db.getCollection(collName);

        WriteResult wr = coll.insert(docParam);

        if (wr.getLastError().getDouble("ok") > 0) {
            b = true;
        }

        return b;
    }

    public static boolean insertMulti(DB db, String collName, List<DBObject> docListParam) {
        boolean b = false;
        if (db == null || collName == null || collName.trim().isEmpty()) {
            return b;
        }

        DBCollection coll = db.getCollection(collName);

        WriteResult wr = coll.insert(docListParam);

        if (wr.getLastError().getDouble("ok") > 0) {
            b = true;
        }

        return b;
    }

    public static boolean update(DB db, String collName, DBObject query, DBObject docParam) {
        boolean b = false;
        if (db == null || collName == null || collName.trim().isEmpty()) {
            return b;
        }

        DBCollection coll = db.getCollection(collName);

        DBObject object = new BasicDBObject();
        object.put("$set", docParam);

        WriteResult wr = coll.update(query, object);

        if (wr.getLastError().getDouble("ok") > 0) {
            b = true;
        }

        return b;
    }

    public static boolean update(DB db, String collName, DBObject query, DBObject docParam, boolean upsert, boolean multi) {
        boolean b = false;
        if (db == null || collName == null || collName.trim().isEmpty()) {
            return b;
        }

        DBCollection coll = db.getCollection(collName);

        DBObject object = new BasicDBObject();
        object.put("$set", docParam);

        WriteResult wr = coll.update(query, object, upsert, multi);

        if (wr.getLastError().getDouble("ok") > 0) {
            b = true;
        }

        return b;
    }

    public static boolean updateMulti(DB db, String collName, DBObject query, DBObject docParam) {
        boolean b = false;
        if (db == null || collName == null || collName.trim().isEmpty()) {
            return b;
        }

        DBCollection coll = db.getCollection(collName);

        DBObject object = new BasicDBObject();
        object.put("$set", docParam);

        WriteResult wr = coll.updateMulti(query, object);

        if (wr.getLastError().getDouble("ok") > 0) {
            b = true;
        }

        return b;
    }

    public static boolean delete(DB db, String collName, DBObject query) {
        boolean b = false;
        if (db == null || collName == null || collName.trim().isEmpty()) {
            return b;
        }

        DBCollection coll = db.getCollection(collName);

        WriteResult wr = coll.remove(query);

        if (wr.getLastError().getDouble("ok") > 0) {
            b = true;
        }

        return b;
    }

    public static int count(DB db, String collName, DBObject query) {
        DBCollection coll = db.getCollection(collName);
        return coll.find(query).count();
    }
}
