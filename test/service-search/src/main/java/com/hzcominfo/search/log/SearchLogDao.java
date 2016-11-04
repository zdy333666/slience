/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.log;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.Map;

/**
 *
 * @author zdy
 */
public class SearchLogDao {

    private static final String SEARCH_LOG_COLL_NAME = "search_log";
    private static final String LOG_COLL_NAME = "log_search_service";

    public static boolean insertLog(DB db, Map<String, Object> logMap) {

        DBObject logDoc = new BasicDBObject(logMap);
        return MongoOpt.insert(db, SEARCH_LOG_COLL_NAME, logDoc);
    }

    public static boolean insertStandardLog(DB db, Map<String, Object> logMap) {

        DBObject logDoc = new BasicDBObject();
        logDoc.put("USER_ID", logMap.get("USER_ID"));
        logDoc.put("USER_NAME", logMap.get("USER_NAME"));
        logDoc.put("ORGANIZATION", logMap.get("ORGANIZATION"));
        logDoc.put("ORGANIZATION_ID", logMap.get("ORGANIZATION_ID"));
        logDoc.put("TERMINAL_ID", logMap.get("TERMINAL_ID"));
        logDoc.put("OPERATE_TIME", logMap.get("OPERATE_TIME"));
        logDoc.put("OPERATE_NAME", logMap.get("OPERATE_NAME"));
        logDoc.put("OPERATE_CONDITION", logMap.get("OPERATE_CONDITION"));
        logDoc.put("OPERATE_TYPE", logMap.get("OPERATE_TYPE"));
        logDoc.put("OPERATE_RESULT", logMap.get("OPERATE_RESULT"));
        logDoc.put("ERROR_CODE", logMap.get("ERROR_CODE"));
        logDoc.put("BZ1", logMap.get("BZ1"));
        logDoc.put("BZ2", logMap.get("BZ2"));
        logDoc.put("BZ3", logMap.get("BZ3"));
        logDoc.put("BZ4", logMap.get("BZ4"));
        logDoc.put("BZ5", logMap.get("BZ5"));

        return MongoOpt.insert(db, LOG_COLL_NAME, logDoc);
    }

    public static void seekLog(DB db, String user, String searchKey, String startTime, String endTime) {

    }

}
