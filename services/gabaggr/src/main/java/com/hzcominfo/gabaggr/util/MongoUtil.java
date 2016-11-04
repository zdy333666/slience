/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabaggr.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xzh
 */
public class MongoUtil {

    public static int getIntValue(DBObject paramObject, String paramFieldName) {
        int localIntValue;
        Object localObject = paramObject.get(paramFieldName);
        if (localObject == null) {
            return 0;
        }
        if (localObject instanceof Long) {
            localIntValue = ((Long) localObject).intValue();
        } else if (localObject instanceof Double) {
            localIntValue = ((Double) localObject).intValue();
        } else if (localObject instanceof Float) {
            localIntValue = ((Float) localObject).intValue();
        } else if (localObject instanceof Integer) {
            localIntValue = (Integer) localObject;
        } else {
            localIntValue = Integer.parseInt(localObject.toString());
        }
        return localIntValue;
    }

    public static long getLongValue(DBObject paramObject, String paramFieldName) {
        long localLongValue;
        Object localObject = paramObject.get(paramFieldName);
        if (localObject == null) {
            return 0;
        }
        if (localObject instanceof Long) {
            localLongValue = (Long) localObject;
        } else if (localObject instanceof Double) {
            localLongValue = ((Double) localObject).longValue();
        } else if (localObject instanceof Float) {
            localLongValue = ((Float) localObject).longValue();
        } else if (localObject instanceof Integer) {
            localLongValue = ((Integer) localObject).longValue();
        } else {
            localLongValue = Long.parseLong(localObject.toString());
        }
        return localLongValue;
    }

    public static String getStringValue(DBObject paramObject, String paramFieldName) {
        String localStringValue;
        Object localObject = paramObject.get(paramFieldName);
        if (localObject == null) {
            return "";
        }
        if (localObject instanceof Date) {
            localStringValue = new SimpleDateFormat("yyyyMMddHHmmss").format(
                    (java.util.Date) localObject);
        } else {
            localStringValue = localObject.toString();
        }
        return localStringValue;
    }

    public static String getGridFSDBFileString(DB db, String fileName) {
        GridFS gridFS = new GridFS(db);
        GridFSDBFile gridFSDBFile = gridFS.findOne(fileName);
        InputStream inputStream = gridFSDBFile.getInputStream();
        if (inputStream != null) {
            try {
                int count;
                count = inputStream.available();
                byte[] b = new byte[count];
                count = inputStream.read(b);
                if (count != 0) {
                    return new String(b);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static GridFSDBFile getGridFSDBFile(DB db, String fileName) {
        if (db == null || fileName == null || fileName.isEmpty()) {
            return null;
        }
        try {
            GridFS gridFS = new GridFS(db);
            return gridFS.findOne(fileName);
        } catch (Exception e) {
            return null;
        }
    }

    public String getGridFSDBFileName(String fileName) {
        if (fileName != null && fileName.startsWith("gridfs:")) {
            try {
                String name = fileName.split(":")[1];
                if (name != null && !name.trim().isEmpty()) {
                    return name;
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Date getDateValue(DBObject paramObject, String paramFieldName) {
        Date localDateValue;
        Object localObject = paramObject.get(paramFieldName);
        if (localObject == null) {
            return null;
        }
        if (localObject instanceof Date) {
            localDateValue = (java.util.Date) localObject;
        } else {
            localDateValue = null;
        }
        return localDateValue;
    }

    public static byte[] getByteArrayValue(DBObject paramObject, String paramFieldName) {
        byte[] localByteArrayValue;
        Object localObject = paramObject.get(paramFieldName);
        if (localObject == null) {
            return null;
        }
        if (localObject instanceof byte[]) {
            localByteArrayValue = (byte[]) localObject;
        } else {
            localByteArrayValue = null;
        }
        return localByteArrayValue;
    }

    public static long getSequence(DB paramDb, String paramSeqName) {
        long localLongValue = 0;
        DBCollection localCol = paramDb.getCollection("common_seq");
        if (localCol == null) {
            return -1;
        }
        DBObject localCond = new BasicDBObject();
        localCond.put("seq_name", paramSeqName);
        DBObject localFields = new BasicDBObject();
        DBObject localValueObject = new BasicDBObject();
        DBObject localAlterValueObject = new BasicDBObject();
        localAlterValueObject.put("seq_value", (long) 1);
        localValueObject.put("$inc", localAlterValueObject);
        DBObject localValue = localCol.findAndModify(localCond,
                localFields, null, false, localValueObject, true, true);
        if (localValue == null) {
            return -1;
        }
        localLongValue = getLongValue(localValue, "seq_value");
        return localLongValue;
    }

}
