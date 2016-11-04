/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.log;

import com.hzcominfo.auth.dao.Impl.UserDao;
import com.hzcominfo.auth.model.A_User;
import com.hzcominfo.auth.model.AuthUserContext;
import com.hzcominfo.mongoutil.MongoUtil;
import com.hzcominfo.search.result.ModelFieldInfo;
import com.hzcominfo.search.service.SearchDao;
import com.hzcominfo.search.service.SearchModelFieldDefine;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

/**
 *
 * @author breeze
 */
public class SearchLogService {

    //private static String authUserCollName = "auth_user";
    private static final String SEARCH_LOG_COLL_NAME = "search_log";
    private static final String SEARCH_COUNT_LOG_COLL_NAME = "access_search_log";
    private static final String SEARCH_MODEL_SELECT_LOG_COLL_NAME = "search_model_select_log";
    private static final SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    //搜索日志查询接口
    public static JsonNode service(HttpServletRequest paramRequest, DB db, String strBody) {
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
        rootNode.put("resultCode", 0);
        rootNode.put("resultMsg", "操作失败");

        if (strBody == null) {
            rootNode.put("resultCode", -1);
            rootNode.put("resultMsg", "出错了，未能获取请求参数");

            System.out.println(rootNode);
            return rootNode;
        }

        JsonNode requestBodyNode = null;
        JsonNode localValueNode = null;

        ObjectMapper localMapper = new ObjectMapper();
        try {
            requestBodyNode = localMapper.readTree(strBody);
            if (requestBodyNode == null) {
                rootNode.put("resultCode", -1);
                rootNode.put("resultMsg", "出错了，未能获取请求参数");

                System.out.println(rootNode);
                return rootNode;
            }

            localValueNode = requestBodyNode.get("func"); //获取操作类型参数
            if (localValueNode == null) {
                rootNode.put("resultCode", 0);
                rootNode.put("resultMsg", "操作失败，功能名称未定义：");

                System.out.println(rootNode);
                return rootNode;
            }
            String funcParam = localValueNode.getValueAsText();

            if ("insertLog".equals(funcParam)) {

                JsonNode paramNode = requestBodyNode.get("param");//获取查询参数
                if (paramNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "添加失败，参数param未定义");

                    System.out.println(rootNode);
                    return rootNode;
                }

                AuthUserContext localAuthInfo = (AuthUserContext) paramRequest.getSession().getAttribute("AuthInfo");
                if (localAuthInfo == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "添加失败，用户未登陆");

                    System.out.println(rootNode);
                    return rootNode;
                }
                String userId = localAuthInfo.getUsers().getUserID();
                String user = localAuthInfo.getUsers().getUsername();
                String pkiName = localAuthInfo.getUsers().getPkiName();

                localValueNode = paramNode.get("model_group_display_name");
                if (localValueNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "添加失败，输入参数 model_group_display_name 缺失");
                    return rootNode;
                }
                String model_group_display_name = localValueNode.getValueAsText();

                localValueNode = paramNode.get("model_name");
                if (localValueNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "添加失败，输入参数 model_name 缺失");
                    return rootNode;
                }
                String model_name = localValueNode.getValueAsText();

                localValueNode = paramNode.get("model_display_name");
                if (localValueNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "添加失败，输入参数 model_display_name 缺失");
                    return rootNode;
                }
                String model_display_name = localValueNode.getValueAsText();

                localValueNode = paramNode.get("search_key");
                if (localValueNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "添加失败，输入参数 search_key 缺失");
                    return rootNode;
                }
                String search_key = localValueNode.getValueAsText();

                localValueNode = paramNode.get("search_key_display");
                if (localValueNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "添加失败，输入参数 search_key_display 缺失");
                    return rootNode;
                }
                String search_key_display = localValueNode.getValueAsText();

                localValueNode = paramNode.get("stats_key");
                if (localValueNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "添加失败，输入参数 stats_key 缺失");
                    return rootNode;
                }
                String stats_key = localValueNode.getValueAsText();

                localValueNode = paramNode.get("stats_key_display_name");
                if (localValueNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "添加失败，输入参数 stats_key_display_name 缺失");
                    return rootNode;
                }
                String stats_key_display_name = localValueNode.getValueAsText();

                localValueNode = paramNode.get("search_return_num");
                if (localValueNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "添加失败，输入参数 search_return_num 缺失");
                    return rootNode;
                }
                int search_return_num = localValueNode.getValueAsInt();

                localValueNode = paramNode.get("search_stats_return_num");
                if (localValueNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "添加失败，输入参数 search_stats_return_num 缺失");
                    return rootNode;
                }
                int search_stats_return_num = localValueNode.getValueAsInt();

                localValueNode = paramNode.get("search_url");
                if (localValueNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "添加失败，输入参数 search_url 缺失");
                    return rootNode;
                }
                String search_url = localValueNode.getValueAsText();

                Date addTime = new Date();

                Map<String, Object> logMap = new HashMap<String, Object>();
                logMap.put("search_log_id", MongoUtil.getSequence(db, "search_log_id")); //long  日志id
                logMap.put("search_time", sdFormat.format(addTime));    //String  搜索时间（系统时间）
                logMap.put("model_group_display_name", model_group_display_name);   //String 数据源类别
                logMap.put("model_name", model_name);  //String 数据模型
                logMap.put("model_display_name", model_display_name);  //String 数据模型显示名称
                logMap.put("search_key", search_key);  //String 搜索关键字
                logMap.put("search_key_display", search_key_display);  //String 搜索关键字中文名称               
                logMap.put("stats_key", stats_key);   //String 数据筛选key
                logMap.put("stats_key_display_name", stats_key_display_name);  //String 数据筛选show
                logMap.put("search_return_num", search_return_num);  //int   初始搜索返回的记录数
                logMap.put("search_stats_return_num", search_stats_return_num);  //int   筛选搜索后返回的记录数
                logMap.put("search_user_id", userId);
                logMap.put("search_user", user);   //String 用户名
                logMap.put("search_pki_user", pkiName);   //String 用户显示名
                logMap.put("search_url", search_url);    //String 搜索url

                boolean b = SearchLogDao.insertLog(db, logMap);

                if (b != false) {
                    rootNode.put("resultCode", 1);
                    rootNode.put("resultMsg", "添加成功");
                } else {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "添加失败");
                }

                //添加访问日志-------------------------------------------------------
                Map<String, Object> standardLogMap = new HashMap<String, Object>();
                standardLogMap.put("USER_ID", localAuthInfo.getUsers().getPkiSfzh());
                standardLogMap.put("USER_NAME", localAuthInfo.getUsers().getPkiName());
                standardLogMap.put("ORGANIZATION", localAuthInfo.getDeptName());
                standardLogMap.put("ORGANIZATION_ID", localAuthInfo.getUsers().getUserDeptCode());
                standardLogMap.put("TERMINAL_ID", paramRequest.getRemoteAddr());
                standardLogMap.put("OPERATE_TIME", sdf.format(addTime));
                standardLogMap.put("OPERATE_NAME", "SearchService");
                standardLogMap.put("OPERATE_CONDITION", search_key);
                standardLogMap.put("OPERATE_TYPE", 1);
                standardLogMap.put("OPERATE_RESULT", 1);
                standardLogMap.put("ERROR_CODE", null);
                standardLogMap.put("BZ1", localAuthInfo.getUsers().getUserID());
                standardLogMap.put("BZ2", localAuthInfo.getRoles().getRoleNameFormat());
                standardLogMap.put("BZ3", null);
                standardLogMap.put("BZ4", null);
                standardLogMap.put("BZ5", null);

                SearchLogDao.insertStandardLog(db, standardLogMap);

            } else if ("querySelfLog".equals(funcParam)) {

                AuthUserContext localAuthInfo = (AuthUserContext) paramRequest.getSession().getAttribute("AuthInfo");
                if (localAuthInfo == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "查询失败，用户未登陆");

                    System.out.println(rootNode);
                    return rootNode;
                }

                int nSkip = 0;
                int nLimit = 50;   //默认返回记录数   

                JsonNode paramNode = requestBodyNode.get("param");//获取查询参数
                if (paramNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "查询失败，参数param未定义");

                    System.out.println(rootNode);
                    return rootNode;
                }

                localValueNode = paramNode.get("nSkip"); //获取操作类型参数
                if (localValueNode != null) {
                    nSkip = localValueNode.getValueAsInt();
                }

                localValueNode = paramNode.get("maxRows"); //获取操作类型参数
                if (localValueNode != null) {
                    nLimit = localValueNode.getValueAsInt() > nLimit ? nLimit : localValueNode.getValueAsInt();
                }

                String searchKey = null;
                localValueNode = paramNode.get("search_key");
                if (localValueNode != null) {
                    searchKey = (localValueNode.getValueAsText() != null && !localValueNode.getValueAsText().trim().isEmpty()) ? localValueNode.getValueAsText() : null;
                }

                String startTime = null;
                localValueNode = paramNode.get("startTime"); //开始时间
                if (localValueNode != null) {
                    startTime = localValueNode.getValueAsText();
                }

                String endTime = null;
                localValueNode = paramNode.get("endTime"); //开始时间
                if (localValueNode != null) {
                    endTime = localValueNode.getValueAsText();
                }

                String userId = localAuthInfo.getUsers().getUserID();
                String user = localAuthInfo.getUsers().getUsername();

                DBObject query = new BasicDBObject();
                //query.put("search_user", user);
                query.put("search_user_id", userId);

                DBObject searchTimeInnerQuery = new BasicDBObject();
                if (startTime != null && !startTime.isEmpty()) {
                    searchTimeInnerQuery.put("$gte", startTime);
                }
                if (endTime != null && !endTime.isEmpty()) {
                    searchTimeInnerQuery.put("$lte", endTime);
                }
                if (!searchTimeInnerQuery.keySet().isEmpty()) {
                    query.put("search_time", searchTimeInnerQuery);
                }

                DBObject searchKeyQuery = new BasicDBObject();
                if (searchKey != null) {
                    searchKeyQuery.put("$regex", searchKey);
                }
                if (!searchKeyQuery.keySet().isEmpty()) {
                    query.put("search_key", searchKeyQuery);
                }

                DBObject sort = new BasicDBObject();
                sort.put("search_time", -1);

                List<DBObject> logDocList = MongoOpt.find(db, SEARCH_LOG_COLL_NAME, query, sort, nSkip, nLimit);

                ArrayNode rowsNode = JsonNodeFactory.instance.arrayNode();
                for (DBObject logDoc : logDocList) {

                    ObjectNode rowNode = JsonNodeFactory.instance.objectNode();
                    rowNode.put("search_log_id", MongoUtil.getLongValue(logDoc, "search_log_id")); //long  日志id
                    rowNode.put("search_time", MongoUtil.getStringValue(logDoc, "search_time"));    //String  搜索时间（系统时间）
                    rowNode.put("model_group_display_name", MongoUtil.getStringValue(logDoc, "model_group_display_name"));   //String 数据源类别
                    rowNode.put("model_name", MongoUtil.getStringValue(logDoc, "model_name"));  //String 数据模型
                    rowNode.put("model_display_name", MongoUtil.getStringValue(logDoc, "model_display_name"));  //String 数据模型显示名称
                    rowNode.put("search_key", MongoUtil.getStringValue(logDoc, "search_key"));  //String 搜索关键字
                    rowNode.put("search_key_display", MongoUtil.getStringValue(logDoc, "search_key_display"));  //String 搜索关键字中文名称
                    rowNode.put("stats_key", MongoUtil.getStringValue(logDoc, "stats_key"));   //String 数据筛选key
                    rowNode.put("stats_key_display_name", MongoUtil.getStringValue(logDoc, "stats_key_display_name"));  //String 数据筛选show
                    rowNode.put("search_return_num", MongoUtil.getIntValue(logDoc, "search_return_num"));  //int   初始搜索返回的记录数
                    rowNode.put("search_stats_return_num", MongoUtil.getIntValue(logDoc, "search_stats_return_num"));  //int   筛选搜索后返回的记录数
                    rowNode.put("search_user_id", userId);
                    rowNode.put("search_user", user);   //String 用户名
                    rowNode.put("search_pki_user", MongoUtil.getStringValue(logDoc, "search_pki_user"));   //String 用户显示名
                    rowNode.put("search_url", MongoUtil.getStringValue(logDoc, "search_url"));    //String 搜索url

                    rowsNode.add(rowNode);
                }

                rootNode.put("resultCode", 1);
                rootNode.put("resultMsg", "查询成功");
                rootNode.put("user_id", userId);
                rootNode.put("count", MongoOpt.count(db, SEARCH_LOG_COLL_NAME, query));
                rootNode.put("return_num", rowsNode.size());
                rootNode.put("rows", rowsNode);
            } //                else if ("queryUserLog".equals(funcParam)) {
            //
            //                int nSkip = 0;
            //                int nLimit = 50;   //默认返回记录数   
            //
            //                JsonNode paramNode = requestBodyNode.get("param");//获取查询参数
            //                if (paramNode == null) {
            //                    rootNode.put("resultCode", 0);
            //                    rootNode.put("resultMsg", "查询失败，参数param未定义");
            //
            //                    System.out.println(rootNode);
            //                    return rootNode;
            //                }
            //
            //                localValueNode = paramNode.get("search_user");
            //                if (localValueNode == null) {
            //                    rootNode.put("resultCode", 0);
            //                    rootNode.put("resultMsg", "查询失败，输入参数 search_user 缺失");
            //                    return rootNode;
            //                }
            //                String search_user = localValueNode.getValueAsText();
            //
            //                localValueNode = paramNode.get("nSkip"); //获取操作类型参数
            //                if (localValueNode != null) {
            //                    nSkip = localValueNode.getValueAsInt();
            //                }
            //
            //                localValueNode = paramNode.get("maxRows"); //获取操作类型参数
            //                if (localValueNode != null) {
            //                    nLimit = localValueNode.getValueAsInt() > nLimit ? nLimit : localValueNode.getValueAsInt();
            //                }
            //
            //                String searchKey = null;
            //                localValueNode = paramNode.get("search_key");
            //                if (localValueNode != null) {
            //                    searchKey = (localValueNode.getValueAsText() != null && !localValueNode.getValueAsText().trim().isEmpty()) ? localValueNode.getValueAsText() : null;
            //                }
            //
            //                String startTime = null;
            //                localValueNode = paramNode.get("startTime"); //开始时间
            //                if (localValueNode != null) {
            //                    startTime = localValueNode.getValueAsText();
            //                }
            //
            //                String endTime = null;
            //                localValueNode = paramNode.get("endTime"); //开始时间
            //                if (localValueNode != null) {
            //                    endTime = localValueNode.getValueAsText();
            //                }
            //
            //                DBObject query = new BasicDBObject();
            //                query.put("search_pki_user", search_user);
            //
            //                DBObject searchTimeInnerQuery = new BasicDBObject();
            //                if (startTime != null && !startTime.isEmpty()) {
            //                    searchTimeInnerQuery.put("$gte", startTime);
            //                }
            //                if (endTime != null && !endTime.isEmpty()) {
            //                    searchTimeInnerQuery.put("$lte", endTime);
            //                }
            //                if (!searchTimeInnerQuery.keySet().isEmpty()) {
            //                    query.put("search_time", searchTimeInnerQuery);
            //                }
            //
            //                DBObject searchKeyQuery = new BasicDBObject();
            //                if (searchKey != null) {
            //                    searchKeyQuery.put("$regex", searchKey);
            //                }
            //                if (!searchKeyQuery.keySet().isEmpty()) {
            //                    query.put("search_key", searchKeyQuery);
            //                }
            //
            //                DBObject sort = new BasicDBObject();
            //                sort.put("search_time", -1);
            //
            //                List<DBObject> logDocList = MongoOpt.find(db, SEARCH_LOG_COLL_NAME, query, sort, nSkip, nLimit);
            //
            //                ArrayNode rowsNode = JsonNodeFactory.instance.arrayNode();
            //                for (DBObject logDoc : logDocList) {
            //
            //                    ObjectNode rowNode = JsonNodeFactory.instance.objectNode();
            //                    rowNode.put("search_log_id", MongoUtil.getLongValue(logDoc, "search_log_id")); //long  日志id
            //                    rowNode.put("search_time", MongoUtil.getStringValue(logDoc, "search_time"));    //String  搜索时间（系统时间）
            //                    rowNode.put("model_group_display_name", MongoUtil.getStringValue(logDoc, "model_group_display_name"));   //String 数据源类别
            //                    rowNode.put("model_name", MongoUtil.getStringValue(logDoc, "model_name"));  //String 数据模型
            //                    rowNode.put("model_display_name", MongoUtil.getStringValue(logDoc, "model_display_name"));  //String 数据模型显示名称
            //                    rowNode.put("search_key", MongoUtil.getStringValue(logDoc, "search_key"));  //String 搜索关键字
            //                    rowNode.put("search_key_display", MongoUtil.getStringValue(logDoc, "search_key_display"));  //String 搜索关键字中文名称
            //                    rowNode.put("stats_key", MongoUtil.getStringValue(logDoc, "stats_key"));   //String 数据筛选key
            //                    rowNode.put("stats_key_display_name", MongoUtil.getStringValue(logDoc, "stats_key_display_name"));  //String 数据筛选show
            //                    rowNode.put("search_return_num", MongoUtil.getIntValue(logDoc, "search_return_num"));  //int   初始搜索返回的记录数
            //                    rowNode.put("search_stats_return_num", MongoUtil.getIntValue(logDoc, "search_stats_return_num"));  //int   筛选搜索后返回的记录数
            //                    rowNode.put("search_user_id", MongoUtil.getStringValue(logDoc, "search_user_id"));
            //                    rowNode.put("search_user", MongoUtil.getStringValue(logDoc, "search_user"));   //String 用户名
            //                    rowNode.put("search_pki_user", MongoUtil.getStringValue(logDoc, "search_pki_user"));   //String 用户显示名
            //                    rowNode.put("search_url", MongoUtil.getStringValue(logDoc, "search_url"));    //String 搜索url
            //
            //                    rowsNode.add(rowNode);
            //                }
            //
            //                rootNode.put("resultCode", 1);
            //                rootNode.put("resultMsg", "查询成功");
            //                rootNode.put("count", MongoOpt.count(db, SEARCH_LOG_COLL_NAME, query));
            //                rootNode.put("return_num", rowsNode.size());
            //                rootNode.put("rows", rowsNode);
            //
            //            } 
            else if ("queryAllLog".equals(funcParam)) {

                AuthUserContext modelContext = (AuthUserContext) paramRequest.getSession().getAttribute("AuthInfo");
                if (modelContext == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "查询失败，用户未登陆");
                    return rootNode;
                }
                String role = modelContext.getRoles().getRoleName();
                String userId = modelContext.getUsers().getUserID();
                String pkiName = modelContext.getUsers().getPkiName();

                int nSkip = 0;
                int nLimit = 50;   //默认返回记录数   

                JsonNode paramNode = requestBodyNode.get("param");//获取查询参数
                if (paramNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "查询失败，参数param未定义");

                    System.out.println(rootNode);
                    return rootNode;
                }

                localValueNode = paramNode.get("nSkip"); //获取操作类型参数
                if (localValueNode != null) {
                    nSkip = localValueNode.getValueAsInt();
                }

                localValueNode = paramNode.get("maxRows"); //获取操作类型参数
                if (localValueNode != null) {
                    nLimit = localValueNode.getValueAsInt() > nLimit ? nLimit : localValueNode.getValueAsInt();
                }

                String searchUser = null;
                localValueNode = paramNode.get("search_user");
                if (localValueNode != null) {
                    searchUser = (localValueNode.getValueAsText() != null && !localValueNode.getValueAsText().trim().isEmpty()) ? localValueNode.getValueAsText() : null;
                }

                String searchKey = null;
                localValueNode = paramNode.get("search_key");
                if (localValueNode != null) {
                    searchKey = (localValueNode.getValueAsText() != null && !localValueNode.getValueAsText().trim().isEmpty()) ? localValueNode.getValueAsText() : null;
                }

                String startTime = null;
                localValueNode = paramNode.get("startTime"); //开始时间
                if (localValueNode != null) {
                    startTime = localValueNode.getValueAsText();
                }

                String endTime = null;
                localValueNode = paramNode.get("endTime"); //开始时间
                if (localValueNode != null) {
                    endTime = localValueNode.getValueAsText();
                }

                //设定查询条件
                DBObject query = new BasicDBObject();

                if (!"admin".equals(role)) {
                    query.put("search_user_id", userId);

                    if (searchUser != null) {
                        if (!(pkiName != null) && !pkiName.equals(searchUser)) {
                            rootNode.put("resultCode", 0);
                            rootNode.put("resultMsg", "查询失败，您不是管理员身份");
                            return rootNode;
                        }
                    }
                } else {
                    if (searchUser != null) {
                        query.put("search_pki_user", searchUser);
                    }
                }

                DBObject searchTimeInnerQuery = new BasicDBObject();
                if (startTime != null && !startTime.isEmpty()) {
                    searchTimeInnerQuery.put("$gte", startTime);
                }
                if (endTime != null && !endTime.isEmpty()) {
                    searchTimeInnerQuery.put("$lte", endTime);
                }
                if (!searchTimeInnerQuery.keySet().isEmpty()) {
                    query.put("search_time", searchTimeInnerQuery);
                }

                DBObject searchKeyQuery = new BasicDBObject();
                if (searchKey != null) {
                    searchKeyQuery.put("$regex", searchKey);
                }
                if (!searchKeyQuery.keySet().isEmpty()) {
                    query.put("search_key", searchKeyQuery);
                }

                DBObject sort = new BasicDBObject();
                sort.put("search_time", -1);

                List<DBObject> logDocList = MongoOpt.find(db, SEARCH_LOG_COLL_NAME, query, sort, nSkip, nLimit);

                ArrayNode rowsNode = JsonNodeFactory.instance.arrayNode();
                for (DBObject logDoc : logDocList) {

                    ObjectNode rowNode = JsonNodeFactory.instance.objectNode();
                    rowNode.put("search_log_id", MongoUtil.getLongValue(logDoc, "search_log_id")); //long  日志id
                    rowNode.put("search_time", MongoUtil.getStringValue(logDoc, "search_time"));    //String  搜索时间（系统时间）
                    rowNode.put("model_group_display_name", MongoUtil.getStringValue(logDoc, "model_group_display_name"));   //String 数据源类别
                    rowNode.put("model_name", MongoUtil.getStringValue(logDoc, "model_name"));  //String 数据模型
                    rowNode.put("model_display_name", MongoUtil.getStringValue(logDoc, "model_display_name"));  //String 数据模型显示名称
                    rowNode.put("search_key", MongoUtil.getStringValue(logDoc, "search_key"));  //String 搜索关键字
                    rowNode.put("search_key_display", MongoUtil.getStringValue(logDoc, "search_key_display"));  //String 搜索关键字中文名称
                    rowNode.put("stats_key", MongoUtil.getStringValue(logDoc, "stats_key"));   //String 数据筛选key
                    rowNode.put("stats_key_display_name", MongoUtil.getStringValue(logDoc, "stats_key_display_name"));  //String 数据筛选show
                    rowNode.put("search_return_num", MongoUtil.getIntValue(logDoc, "search_return_num"));  //int   初始搜索返回的记录数
                    rowNode.put("search_stats_return_num", MongoUtil.getIntValue(logDoc, "search_stats_return_num"));  //int   筛选搜索后返回的记录数
                    rowNode.put("search_user_id", MongoUtil.getStringValue(logDoc, "search_user_id"));
                    rowNode.put("search_user", MongoUtil.getStringValue(logDoc, "search_user"));   //String 用户名
                    rowNode.put("search_pki_user", MongoUtil.getStringValue(logDoc, "search_pki_user"));   //String 用户显示名
                    rowNode.put("search_url", MongoUtil.getStringValue(logDoc, "search_url"));    //String 搜索url                                    //返回的统计信息    string	Json格式

                    rowsNode.add(rowNode);
                }

                rootNode.put("resultCode", 1);
                rootNode.put("resultMsg", "查询成功");
                rootNode.put("count", MongoOpt.count(db, SEARCH_LOG_COLL_NAME, query));
                rootNode.put("return_num", rowsNode.size());
                rootNode.put("rows", rowsNode);

            } else if ("querySameSearch".equals(funcParam)) {

                int nSkip = 0;
                int nLimit = 50;   //默认返回记录数   

                JsonNode paramNode = requestBodyNode.get("param");//获取查询参数
                if (paramNode == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "查询失败，参数param未定义");

                    System.out.println(rootNode);
                    return rootNode;
                }

                localValueNode = paramNode.get("nSkip"); //获取操作类型参数
                if (localValueNode != null) {
                    nSkip = localValueNode.getValueAsInt();
                }

                localValueNode = paramNode.get("maxRows"); //获取操作类型参数
                if (localValueNode != null) {
                    nLimit = localValueNode.getValueAsInt() > nLimit ? nLimit : localValueNode.getValueAsInt();
                }

                String searchKey = null;
                localValueNode = paramNode.get("search_key");
                if (localValueNode != null) {
                    searchKey = (localValueNode.getValueAsText() != null && !localValueNode.getValueAsText().trim().isEmpty()) ? localValueNode.getValueAsText() : null;
                } else {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "查询失败，参数search_key无效");

                    System.out.println(rootNode);
                    return rootNode;
                }

                String searchModelName = null;
                localValueNode = paramNode.get("model_name");
                if (localValueNode != null) {
                    searchModelName = (localValueNode.getValueAsText() != null && !localValueNode.getValueAsText().trim().isEmpty()) ? localValueNode.getValueAsText() : null;
                } else {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "查询失败，参数model_name无效");
                    System.out.println(rootNode);
                    return rootNode;
                }

                //设定查询条件
                DBObject query = new BasicDBObject();
                query.put("search_key", searchKey);
                query.put("model_name", searchModelName);

                DBObject sort = new BasicDBObject();
                sort.put("search_time", -1);

                List<DBObject> logDocList = MongoOpt.find(db, SEARCH_LOG_COLL_NAME, query, sort, nSkip, nLimit);

                Map<String, AuthUserContext> userId_UserContextMapper = new HashMap<String, AuthUserContext>();
                ArrayNode rowsNode = JsonNodeFactory.instance.arrayNode();
                for (DBObject logDoc : logDocList) {
                    String userDept = "";

                    String userId = MongoUtil.getStringValue(logDoc, "search_user_id");
                    if (userId != null && !userId.trim().isEmpty()) {
                        AuthUserContext modelContext = null;

                        if (userId_UserContextMapper.containsKey(userId)) {
                            modelContext = userId_UserContextMapper.get(userId);
                        } else {
                            A_User aUser = new A_User();
                            aUser.setUserID(userId);
                            modelContext = UserDao.GetInstance().checkUser(db, aUser);
                            userId_UserContextMapper.put(userId, modelContext);
                        }

                        if (modelContext != null) {
                            userDept = modelContext.getDeptName();
                        }
                    }

                    ObjectNode rowNode = JsonNodeFactory.instance.objectNode();
                    rowNode.put("search_log_id", MongoUtil.getLongValue(logDoc, "search_log_id")); //long  日志id
                    rowNode.put("search_time", MongoUtil.getStringValue(logDoc, "search_time"));    //String  搜索时间（系统时间）
                    rowNode.put("model_group_display_name", MongoUtil.getStringValue(logDoc, "model_group_display_name"));   //String 数据源类别
                    rowNode.put("model_name", MongoUtil.getStringValue(logDoc, "model_name"));  //String 数据模型
                    rowNode.put("model_display_name", MongoUtil.getStringValue(logDoc, "model_display_name"));  //String 数据模型显示名称
                    rowNode.put("search_key", MongoUtil.getStringValue(logDoc, "search_key"));  //String 搜索关键字
                    rowNode.put("search_key_display", MongoUtil.getStringValue(logDoc, "search_key_display"));  //String 搜索关键字中文名称
                    rowNode.put("stats_key", MongoUtil.getStringValue(logDoc, "stats_key"));   //String 数据筛选key
                    rowNode.put("stats_key_display_name", MongoUtil.getStringValue(logDoc, "stats_key_display_name"));  //String 数据筛选show
                    rowNode.put("search_return_num", MongoUtil.getIntValue(logDoc, "search_return_num"));  //int   初始搜索返回的记录数
                    rowNode.put("search_stats_return_num", MongoUtil.getIntValue(logDoc, "search_stats_return_num"));  //int   筛选搜索后返回的记录数
                    rowNode.put("search_user_id", userId);
                    rowNode.put("search_user", MongoUtil.getStringValue(logDoc, "search_user"));   //String 用户名
                    rowNode.put("search_pki_user", MongoUtil.getStringValue(logDoc, "search_pki_user"));   //String 用户显示名
                    rowNode.put("search_user_dept", userDept);
                    rowNode.put("search_url", MongoUtil.getStringValue(logDoc, "search_url"));    //String 搜索url                                    //返回的统计信息    string	Json格式

                    rowsNode.add(rowNode);
                }

                rootNode.put("resultCode", 1);
                rootNode.put("resultMsg", "查询成功");
                rootNode.put("count", MongoOpt.count(db, SEARCH_LOG_COLL_NAME, query));
                rootNode.put("return_num", rowsNode.size());
                rootNode.put("rows", rowsNode);

            } else if ("querySearchTotal".equals(funcParam)) {

                DBObject query = new BasicDBObject();

                DBObject sort = new BasicDBObject();
                sort.put("update_time", -1);

                DBObject doc = MongoOpt.findOne(db, SEARCH_COUNT_LOG_COLL_NAME, query, sort);
                if (doc != null) {
                    rootNode.put("resultCode", 1);
                    rootNode.put("resultMsg", "查询成功");
                    rootNode.put("searchTotal", MongoUtil.getLongValue(doc, "search_total"));
                }

            } else if ("saveSearchModelSelected".equals(funcParam)) {

                AuthUserContext localAuthInfo = (AuthUserContext) paramRequest.getSession().getAttribute("AuthInfo");
                if (localAuthInfo == null) {
                    rootNode.put("resultMsg", "保存失败，用户未登陆");
                    System.out.println(rootNode);
                    return rootNode;
                }
                String user = localAuthInfo.getUsers().getUsername();

                JsonNode paramNode = requestBodyNode.get("param");//获取查询参数
                if (paramNode == null) {
                    rootNode.put("resultMsg", "保存失败，参数 param 未定义");
                    System.out.println(rootNode);
                    return rootNode;
                }

                localValueNode = paramNode.get("model_group_name");
                if (localValueNode == null) {
                    rootNode.put("resultMsg", "查询失败，参数 model_group_name 未定义");
                    System.out.println(rootNode);
                    return rootNode;
                }
                String modelGroupName = localValueNode.getValueAsText();
                if (modelGroupName == null || modelGroupName.trim().isEmpty()) {
                    rootNode.put("resultMsg", "保存失败，参数 modelGroupName 的值为空!");
                    System.out.println(rootNode);
                    return rootNode;
                }

                String selectedModelStr = "";
                localValueNode = paramNode.get("selected_model");
                if (localValueNode != null) {
                    selectedModelStr = localValueNode.getValueAsText();
                }

                DBObject query = new BasicDBObject();
                query.put("user", user);
                query.put("model_group_name", modelGroupName);

                DBObject doc = new BasicDBObject();
                doc.put("user", user);
                doc.put("model_group_name", modelGroupName);
                doc.put("selected_model", selectedModelStr);
                doc.put("update_time", new Date());

                boolean b = MongoOpt.update(db, SEARCH_MODEL_SELECT_LOG_COLL_NAME, query, doc, true, false);
                if (b == true) {
                    rootNode.put("resultCode", 1);
                    rootNode.put("resultMsg", "保存成功");
                } else {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "保存失败");
                }

            } else if ("querySearchModelSelected".equals(funcParam)) {

                AuthUserContext localAuthInfo = (AuthUserContext) paramRequest.getSession().getAttribute("AuthInfo");
                if (localAuthInfo == null) {
                    rootNode.put("resultCode", 0);
                    rootNode.put("resultMsg", "查询失败，用户未登陆");

                    System.out.println(rootNode);
                    return rootNode;
                }
                String user = localAuthInfo.getUsers().getUsername();

                DBObject query = new BasicDBObject();
                query.put("user", user);

                List<DBObject> docList = MongoOpt.find(db, SEARCH_MODEL_SELECT_LOG_COLL_NAME, query);
                ArrayNode rowsNode = JsonNodeFactory.instance.arrayNode();

                for (DBObject doc : docList) {
                    String modelGroupName = MongoUtil.getStringValue(doc, "model_group_name");
                    if (modelGroupName == null || modelGroupName.trim().isEmpty()) {
                        continue;
                    }

                    ArrayNode itemsNode = JsonNodeFactory.instance.arrayNode();
                    String selectedModelStr = MongoUtil.getStringValue(doc, "selected_model");

                    if (selectedModelStr != null && !selectedModelStr.trim().isEmpty()) {
                        String[] selectedModelArray = selectedModelStr.split(",");
                        for (String selectedModelName : selectedModelArray) {
                            itemsNode.add(selectedModelName);
                        }
                    }

                    ObjectNode rowNode = JsonNodeFactory.instance.objectNode();
                    rowNode.put("model_group_name", modelGroupName);
                    rowNode.put("selected_model_array", itemsNode);

                    Date updateTime = MongoUtil.getDateValue(doc, "update_time");
                    rowNode.put("update_time", sdFormat.format(updateTime));

                    rowsNode.add(rowNode);
                }

                rootNode.put("resultCode", 1);
                rootNode.put("resultMsg", "查询成功");
                rootNode.put("rows", rowsNode);
            }

        } catch (Exception ex) {
            rootNode.put("resultCode", -1);
            rootNode.put("resultMsg", "出错了");
            Logger.getLogger(SearchLogService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return rootNode;
        }

    }

    public static void buildSearchModelFieldInfo(HttpServletRequest paramRequest, DB db) {

        Map<String, JsonNode> searchModelFieldMap = new HashMap<String, JsonNode>();

        String searchModelDefineCollName = "search_model_define";
        List<DBObject> searchModelDocList = MongoOpt.find(db, searchModelDefineCollName, new BasicDBObject(), new BasicDBObject());

        for (DBObject searchModelDoc : searchModelDocList) {

            int searchModelDefId = MongoUtil.getIntValue(searchModelDoc, "model_def_id");
            String searchModelName = MongoUtil.getStringValue(searchModelDoc, "model_name");

            ArrayList< SearchModelFieldDefine> localFieldDefineList = SearchDao.GetModelFieldDefine(db, searchModelDefId);
            if (localFieldDefineList == null) {
                return;
            }

            List< ModelFieldInfo> searchModelFieldInfoList = new ArrayList< ModelFieldInfo>();
            for (SearchModelFieldDefine localFieldDefine : localFieldDefineList) {
//              过滤grid模式下的一些字段            
                if (localFieldDefine.getDisplayInGrid() == 0) {
                    continue;
                }
                ModelFieldInfo localFieldInfo = new ModelFieldInfo();
                localFieldInfo.fieldName = localFieldDefine.getFieldName();
                localFieldInfo.fieldDisplayName = localFieldDefine.getFieldDisplayName();//集合字段显示名称
                localFieldInfo.displayInGrid = localFieldDefine.getDisplayInGrid();
                localFieldInfo.displayWidth = localFieldDefine.getDisplayWidth();
                localFieldInfo.fieldSpec = localFieldDefine.getFieldSpec();
                searchModelFieldInfoList.add(localFieldInfo);
            }

            ArrayNode localFieldsNode = JsonNodeFactory.instance.arrayNode();

            for (ModelFieldInfo localFieldInfo : searchModelFieldInfoList) {
                ObjectNode localNode = JsonNodeFactory.instance.objectNode();
                localNode.put("fieldName", localFieldInfo.fieldName);
                if ((localFieldInfo.fieldDisplayName == null) || (localFieldInfo.fieldDisplayName.isEmpty())) {
                    localNode.put("fieldDisplayName", localFieldInfo.fieldName);
                } else {
                    localNode.put("fieldDisplayName", localFieldInfo.fieldDisplayName);
                }
                localNode.put("displayInGrid", localFieldInfo.displayInGrid);
                if (localFieldInfo.displayWidth != 0) {
                    localNode.put("displayWidth", localFieldInfo.displayWidth);
                }
                if (localFieldInfo.fieldSpec != 0) {
                    localNode.put("fieldSpec", localFieldInfo.fieldSpec);
                }
                localFieldsNode.add(localNode);
            }

            searchModelFieldMap.put(searchModelName, localFieldsNode);
        }

        paramRequest.getSession().setAttribute("searchModelFieldMap", searchModelFieldMap);
    }
}
