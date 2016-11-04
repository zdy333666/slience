/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.service;

import com.hzcominfo.auth.dao.Impl.RoleModelFieldMapDao;
import com.hzcominfo.auth.model.A_Role;
import com.hzcominfo.auth.model.A_RoleModelFieldMap;
import com.hzcominfo.auth.model.AuthUserContext;
import com.hzcominfo.authparam.AuthRoleInfo;
import com.hzcominfo.mongoutil.MongoUtil;
import com.hzcominfo.search.conf.MongoSource;
import com.hzcominfo.search.conf.MongoSourceFactory;
import com.hzcominfo.search.conf.SearchConfiguration;
import com.hzcominfo.search.pojo.SearchModel;
import com.hzcominfo.search.pojo.SearchModelGroup;
import com.hzcominfo.search.result.KeyModelMap;
import com.hzcominfo.search.result.ModelGroupKeyMap;
import com.hzcominfo.search.util.MongoValuePaser;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.JsonNodeFactory;
import org.codehaus.jackson.node.ObjectNode;

/**
 *
 * @author xzh
 */
public class SearchDao {

    public static ArrayList<SearchModelGroup> getSearchModelGroups() {

        DBObject query = new BasicDBObject();
        query.put("enable_flag", 1);

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("enable_flag", 0);

        DBObject sort = new BasicDBObject();
        sort.put("display_order", 1);

        MongoSource source = MongoSourceFactory.getSource(SearchConfiguration.MONGO_URI_RTDB);
        DB db = source.getClient().getDB(source.getClientURI().getDatabase());
        List<DBObject> docs = db.getCollection(SearchConfiguration.SEARCH_MODEL_GROUP).find(query, fields).sort(sort).toArray();

        ArrayList< SearchModelGroup> result = new ArrayList<>();
        for (DBObject doc : docs) {
            SearchModelGroup modelGroup = new SearchModelGroup();
            modelGroup.setId(MongoValuePaser.parseLong(doc.get("model_group_id")));
            modelGroup.setName(MongoValuePaser.parseString(doc.get("model_group_name")));
            modelGroup.setDisplayName(MongoValuePaser.parseString(doc.get("model_group_display_name")));
            modelGroup.setAddComment(MongoValuePaser.parseString(doc.get("add_comment")));
            modelGroup.setSelectedDefault(MongoValuePaser.parseInt(doc.get("selected_default")));
            modelGroup.setUseSpec(MongoValuePaser.parseInt(doc.get("use_spec")));
            modelGroup.setDisplayOrder(MongoValuePaser.parseInt(doc.get("display_order")));
            modelGroup.setEnable(1);

            result.add(modelGroup);
        }

        return result;
    }
    //取模型组和关键字key的映射信息

    public static ArrayList<ModelGroupKeyMap> GetModelGroupKeyList(com.mongodb.DB db, String modelGroupId, HttpServletRequest request) throws Exception {
        if (db == null || modelGroupId.compareToIgnoreCase("") == 0) {
            return null;
        }
        DBCollection localCol = db.getCollection("search_model_group_key_map");
        Object localValue;//存放临时值的地方
//  没有找到collection         
        if (localCol == null) {
            return null;
        }
//  根据model_name查找search_model_def
        DBObject doc = new BasicDBObject();
        doc.put("key_group_id", modelGroupId);
        DBCursor localCursor = localCol.find(doc);
        if ((localCursor == null) || (!localCursor.hasNext())) {
            return null;
        }
        ArrayList<ModelGroupKeyMap> modelGroupKeyList = new ArrayList<ModelGroupKeyMap>();
        while (localCursor.hasNext()) {
            ModelGroupKeyMap key = new ModelGroupKeyMap();
            DBObject localObject = localCursor.next();
            if (localObject == null) {
                continue;
            }
            localValue = localObject.get("key_name");
            if (localValue == null) {
                continue;
            }
            key.keyName = localValue.toString();
            localValue = localObject.get("key_display_name");
            if (localValue == null) {
                continue;
            }
            key.keyDisplayName = localValue.toString();
            localValue = localObject.get("key_id");
            if (localValue == null) {
                continue;
            }
            key.keyId = MongoUtil.getIntValue(localObject, "key_id");
            key.keyModelList = GetKeyModelList(db, key.keyId, request);//取关键字key和模型的映射信息
            modelGroupKeyList.add(key);
        }
        return modelGroupKeyList;
    }
    //取关键字key和模型的映射信息

    public static ArrayList<KeyModelMap> GetKeyModelList(com.mongodb.DB db, int keyId, HttpServletRequest request) throws Exception {
        if (db == null || keyId <= 0) {
            return null;
        }
        DBCollection localCol = db.getCollection("search_key_model_map");
        Object localValue;//存放临时值的地方
//  没有找到collection         
        if (localCol == null) {
            return null;
        }
//  根据model_name查找search_model_def
        DBObject doc = new BasicDBObject();
        doc.put("key_id", keyId);
        DBCursor localCursor = localCol.find(doc);
        if ((localCursor == null) || (!localCursor.hasNext())) {
            return null;
        }
        ArrayList<KeyModelMap> keyModelList = new ArrayList<KeyModelMap>();
        while (localCursor.hasNext()) {
            KeyModelMap keyModel = new KeyModelMap();
            DBObject localObject = localCursor.next();
            if (localObject == null) {
                continue;
            }
            localValue = localObject.get("model_name");
            if (localValue == null) {
                continue;
            }
            keyModel.modelName = localValue.toString();
            localValue = localObject.get("model_display_name");
            if (localValue == null) {
                continue;
            }
            keyModel.modelDisplayName = localValue.toString();
            localValue = localObject.get("field_name");
            if (localValue == null) {
                continue;
            }
            keyModel.fieldName = localValue.toString();
            if (checkRoleModelAuth(db, keyModel.modelName, request) > 0) {
                keyModelList.add(keyModel);
            }
        }
        return keyModelList;
    }

//    public static ArrayList< SearchModelGroupDefine> GetModelGroupDefineList(com.mongodb.DB db) {
//        DBCollection localDefCol = db.getCollection("search_model_group_define");
//        Object localValue;//存放临时值的地方
////  没有找到collection         
//        if (localDefCol == null) {
//            return null;
//        }
////  根据model_name查找search_model_def
//        DBCursor localCursor = localDefCol.find();
//        BasicDBObject localSort = new BasicDBObject();
//        localSort.put("display_order", (int) 1);
//        localCursor = localCursor.sort(localSort);
//        if ((localCursor == null) || (!localCursor.hasNext())) {
//            return null;
//        }
//        ArrayList< SearchModelGroupDefine> localDefineList = new ArrayList< SearchModelGroupDefine>();
//        while (localCursor.hasNext()) {
//            DBObject localDefineObject = localCursor.next();
//            if (localDefineObject == null) {
//                continue;
//            }
//            localValue = localDefineObject.get("model_name");
//            if (localValue == null) {
//                continue;
//            }
//            SearchModelGroupDefine localModelDefine = new SearchModelGroupDefine();
//            localModelDefine.setSelectedDefault(1);
//
//            localModelDefine.setModelName(localValue.toString());
//            localValue = localDefineObject.get("model_group_display_name");
//            if (localValue == null) {
//                continue;
//            }
//            localModelDefine.setModelGroupDisplayName(localValue.toString());
//            localValue = localDefineObject.get("model_group_def_id");
//            if (localValue == null) {
//                continue;
//            }
//            int localModelDefId;
//            if (localValue instanceof Integer) {
//                localModelDefId = (int) (Integer) localValue;
//            } else {
//                localModelDefId = ((Double) localValue).intValue();
//            }
//            localModelDefine.setModelGroupDefId(localModelDefId);
//            localValue = localDefineObject.get("selected_default");
//            if (localValue != null) {
//                int localSelectedDefault;
//                if (localValue instanceof Integer) {
//                    localSelectedDefault = (int) (Integer) localValue;
//                } else {
//                    localSelectedDefault = ((Double) localValue).intValue();
//                }
//                localModelDefine.setSelectedDefault(localSelectedDefault);
//            }
//            localDefineList.add(localModelDefine);
//        }
//        return localDefineList;
//    }
    public static SearchModel getModel(String modelName) {
//        DBCollection localDefCol = db.getCollection("search_model_define");
//        Object localValue;//存放临时值的地方
//        SearchModelDefine localModelDefine = new SearchModelDefine();
//        localModelDefine.setSelectedDefault(1);
////  没有找到collection         
//        if (localDefCol == null) {
//            return null;
//        }
//        BasicDBObject localDefCond = new BasicDBObject();
//        localDefCond.put("model_name", paramModelName);
////  根据model_name查找search_model_def         
//        DBObject localDefineObject = localDefCol.findOne(localDefCond);
//        if (localDefineObject == null) {
//            return null;
//        }
//        localValue = localDefineObject.get("model_name");
//        if (localValue == null) {
//            return null;
//        }
//        localModelDefine.setModelName(localValue.toString());
//        localValue = localDefineObject.get("model_display_name");
//        if (localValue == null) {
//            return null;
//        }
//        localModelDefine.setModelDisplayName(localValue.toString());
//        localValue = localDefineObject.get("model_view_name");
//        if (localValue == null) {
//            return null;
//        }
//        localModelDefine.setModelViewName(localValue.toString());
//        localValue = localDefineObject.get("search_url");
//        if (localValue == null) {
//            return null;
//        }
//        localModelDefine.setSearchUrl(localValue.toString());
//
//        localValue = localDefineObject.get("search_sort");
//        if (localValue == null) {
//            localValue = "";
//        }
//        localModelDefine.setSearchSort(localValue.toString());
//
//        localValue = localDefineObject.get("model_def_id");
//        if (localValue == null) {
//            return null;
//        }
//        int localModelDefId = MongoUtil.getIntValue(localDefineObject, "model_def_id");
//        localModelDefine.setModelDefId(localModelDefId);
//        return localModelDefine;
//        
        //----------------------------

        DBObject query = new BasicDBObject();
        query.put("model_name", modelName);
        query.put("enable_flag", 1);

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("model_name", 0);
        fields.put("enable_flag", 0);

        DBObject sort = new BasicDBObject();
        sort.put("display_order", 1);

        MongoSource source = MongoSourceFactory.getSource(SearchConfiguration.MONGO_URI_RTDB);
        DB db = source.getClient().getDB(source.getClientURI().getDatabase());
        DBObject doc = db.getCollection(SearchConfiguration.SEARCH_MODEL).findOne(query, fields);

        SearchModel model = new SearchModel();
        model.setGroupId(MongoValuePaser.parseLong(doc.get("model_group_id")));
        model.setId(MongoValuePaser.parseLong(doc.get("model_def_id")));
        model.setName(modelName);
        model.setDisplayName(MongoValuePaser.parseString(doc.get("model_display_name")));
        model.setViewName(MongoValuePaser.parseString(doc.get("model_view_name")));
        model.setSearchUrl(MongoValuePaser.parseString(doc.get("search_url")));
        model.setSearchSort(MongoValuePaser.parseString(doc.get("search_sort")));
        model.setSelectedDefault(MongoValuePaser.parseInt(doc.get("selected_default")));
        model.setUseSpec(MongoValuePaser.parseInt(doc.get("use_spec")));
        model.setDisplayOrder(MongoValuePaser.parseInt(doc.get("display_order")));
        model.setAddComment(MongoValuePaser.parseString(doc.get("add_comment")));
        model.setEnable(1);

        return model;
    }

    public static ArrayList<SearchModel> getSearchModels() {

        DBObject query = new BasicDBObject();
        query.put("enable_flag", 1);

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("enable_flag", 0);

        DBObject sort = new BasicDBObject();
        sort.put("display_order", 1);

        MongoSource source = MongoSourceFactory.getSource(SearchConfiguration.MONGO_URI_RTDB);
        DB db = source.getClient().getDB(source.getClientURI().getDatabase());
        List<DBObject> docs = db.getCollection(SearchConfiguration.SEARCH_MODEL).find(query, fields).sort(sort).toArray();

        ArrayList< SearchModel> result = new ArrayList<>();
        for (DBObject doc : docs) {
            SearchModel model = new SearchModel();
            model.setGroupId(MongoValuePaser.parseLong(doc.get("model_group_id")));
            model.setId(MongoValuePaser.parseLong(doc.get("model_def_id")));
            model.setName(MongoValuePaser.parseString(doc.get("model_name")));
            model.setDisplayName(MongoValuePaser.parseString(doc.get("model_display_name")));
            model.setViewName(MongoValuePaser.parseString(doc.get("model_view_name")));
            model.setSearchUrl(MongoValuePaser.parseString(doc.get("search_url")));
            model.setSearchSort(MongoValuePaser.parseString(doc.get("search_sort")));
            model.setSelectedDefault(MongoValuePaser.parseInt(doc.get("selected_default")));
            model.setUseSpec(MongoValuePaser.parseInt(doc.get("use_spec")));
            model.setDisplayOrder(MongoValuePaser.parseInt(doc.get("display_order")));
            model.setAddComment(MongoValuePaser.parseString(doc.get("add_comment")));
            model.setEnable(1);

            result.add(model);
        }

        return result;
    }

    public static ArrayList< SearchModelDefine> GetModelDefineList(com.mongodb.DB db, String modelGroupId, HttpServletRequest request) {
        DBCollection localDefCol = db.getCollection("search_model_define");
        Object localValue;//存放临时值的地方
//  没有找到collection         
        if (localDefCol == null) {
            return null;
        }
        BasicDBObject localDefCond = new BasicDBObject();
        Pattern pattern = Pattern.compile(modelGroupId + "++", Pattern.CASE_INSENSITIVE);//利用正则表达式模糊查询
        localDefCond.put("model_group_id", pattern);
//  根据model_name查找search_model_def
        DBCursor localCursor = localDefCol.find(localDefCond);
        localCursor.sort(new BasicDBObject("display_order", 1));
        if ((localCursor == null) || (!localCursor.hasNext())) {
            return null;
        }
        ArrayList< SearchModelDefine> localDefineList = new ArrayList< SearchModelDefine>();
        while (localCursor.hasNext()) {
            DBObject localDefineObject = localCursor.next();
            if (localDefineObject == null) {
                continue;
            }
            localValue = localDefineObject.get("model_name");
            if (localValue == null) {
                continue;
            }
            SearchModelDefine localModelDefine = new SearchModelDefine();
            localModelDefine.setSelectedDefault(1);

            localModelDefine.setModelName(localValue.toString());
            localValue = localDefineObject.get("model_display_name");
            if (localValue == null) {
                continue;
            }
            localModelDefine.setModelDisplayName(localValue.toString());
            localValue = localDefineObject.get("model_view_name");
            if (localValue == null) {
                continue;
            }
            localModelDefine.setModelViewName(localValue.toString());
            localValue = localDefineObject.get("search_url");
            if (localValue == null) {
                continue;
            }
            localModelDefine.setSearchUrl(localValue.toString());
            localValue = localDefineObject.get("model_def_id");
            if (localValue == null) {
                continue;
            }
            int localModelDefId = MongoUtil.getIntValue(localDefineObject, "model_def_id");
            localModelDefine.setModelDefId(localModelDefId);
            localValue = localDefineObject.get("selected_default");
            if (localValue != null) {
                int localSelectedDefault = MongoUtil.getIntValue(localDefineObject, "selected_default");
                localModelDefine.setSelectedDefault(localSelectedDefault);
            }
            if (checkRoleModelAuth(db, localModelDefine.getModelName(), request) > 0) {//如果此用户包含的角色对这个模型有访问权限
                localDefineList.add(localModelDefine);
            }
        }
        return localDefineList;
    }

    public static ArrayList< SearchModelIdDefine> GetModelIdDefineList(com.mongodb.DB db, int paramModelDefId) {
        DBCollection localDefCol = db.getCollection("search_model_id_define");
        Object localValue;//存放临时值的地方
        SearchModelIdDefine localModelIdDefine;
//  没有找到collection         
        if (localDefCol == null) {
            return null;
        }
        BasicDBObject localDefCond = new BasicDBObject();
        localDefCond.put("model_def_id", paramModelDefId);
//  根据model_name查找search_model_def         
        DBCursor localCursor = localDefCol.find(localDefCond);
        if (localCursor == null) {
            return null;
        }
        ArrayList< SearchModelIdDefine> localModelIdDefineList = new ArrayList< SearchModelIdDefine>();
        while (localCursor.hasNext()) {
            DBObject localDefineObject = localCursor.next();
            localModelIdDefine = new SearchModelIdDefine();
            localValue = localDefineObject.get("id_field_name");
            if (localValue == null) {
                continue;
            }
            localModelIdDefine.setIdFieldName(localValue.toString());

            localValue = localDefineObject.get("name_field_name");
            if (localValue == null) {
                return null;
            }
            localModelIdDefine.setNameFieldName(localValue.toString());
            localModelIdDefineList.add(localModelIdDefine);
        }
        return localModelIdDefineList;
    }

    public static ArrayList< SearchModelFieldDefine> GetModelFieldDefine(com.mongodb.DB db, int paramModelDefId) {
        DBCollection localDefCol = db.getCollection("search_model_field_define");
        Object localValue;//存放临时值的地方
        SearchModelFieldDefine localModelFieldDefine;
//  没有找到collection         
        if (localDefCol == null) {
            return null;
        }
        BasicDBObject localDefCond = new BasicDBObject();
        localDefCond.put("model_def_id", paramModelDefId);
//  根据model_name查找search_model_def         
        DBCursor localCursor = localDefCol.find(localDefCond).sort(new BasicDBObject().append("field_order", 1));
        if (localCursor == null) {
            return null;
        }
        ArrayList< SearchModelFieldDefine> localModelFieldDefineList = new ArrayList< SearchModelFieldDefine>();
        int localIntValue = 0;
        while (localCursor.hasNext()) {
            DBObject localDefineObject = localCursor.next();
            localModelFieldDefine = new SearchModelFieldDefine();
            localValue = localDefineObject.get("field_name");
            if (localValue == null) {
                continue;
            }
            localModelFieldDefine.setFieldName(localValue.toString());

            localValue = localDefineObject.get("field_display_name");
            if (localValue == null) {
                localValue = localModelFieldDefine.getFieldName();
            }
            localModelFieldDefine.setFieldDisplayName(localValue.toString());
            localValue = localDefineObject.get("search_field_name");
            if (localValue == null) {
                return null;
            }
            localModelFieldDefine.searchFieldList = localValue.toString().split(",");
            if ((localModelFieldDefine.searchFieldList != null)
                    && (localModelFieldDefine.searchFieldList.length > 1)) {
                localModelFieldDefine.setSearchFieldName(localModelFieldDefine.searchFieldList[0]);
            } else {
                localModelFieldDefine.setSearchFieldName(localValue.toString());
            }
//          localModelFieldDefine.setSearchFieldName(localValue.toString());
            localValue = localDefineObject.get("display_in_grid");
            int localDisplayInGrid;
            if (localValue == null) {
                localModelFieldDefine.setDisplayInGrid(1);
            } else {
                if (localValue instanceof Integer) {
                    localDisplayInGrid = (int) (Integer) localValue;
                } else {
                    localDisplayInGrid = ((Double) localValue).intValue();
                }
                localModelFieldDefine.setDisplayInGrid(localDisplayInGrid);
            }
            localValue = localDefineObject.get("display_width");
            int localDisplayWidth;
            if (localValue == null) {
                localModelFieldDefine.setDisplayWidth(0);
            } else {
                if (localValue instanceof Integer) {
                    localDisplayWidth = (int) (Integer) localValue;
                } else {
                    localDisplayWidth = ((Double) localValue).intValue();
                }
                localModelFieldDefine.setDisplayWidth(localDisplayWidth);
            }
            localValue = localDefineObject.get("wild_match");
            if (localValue == null) {
                localModelFieldDefine.setWildMatch(0);
            } else {
                if (localValue instanceof Integer) {
                    localIntValue = (int) (Integer) localValue;
                } else {
                    localIntValue = ((Double) localValue).intValue();
                }
                localModelFieldDefine.setWildMatch(localIntValue);
            }
            localValue = localDefineObject.get("field_spec");
            if (localValue == null) {
                localModelFieldDefine.setFieldSpec(0);
            } else {
                if (localValue instanceof Integer) {
                    localIntValue = (int) (Integer) localValue;
                } else {
                    localIntValue = ((Double) localValue).intValue();
                }
                localModelFieldDefine.setFieldSpec(localIntValue);
            }
            localModelFieldDefineList.add(localModelFieldDefine);
        }
        return localModelFieldDefineList;
    }

    //检测当前用户拥有的角色对单个模型是否有访问权限
    public static int checkRoleModelAuth(com.mongodb.DB db, String modelName, HttpServletRequest paramRequest) {
        A_Role localRole = ((AuthUserContext) paramRequest.getSession().getAttribute("AuthInfo")).getRoles();
        if (localRole == null || null == localRole.getRoleName() || "".equals(localRole.getRoleName())) {
            return 0;
        }
        int typeInt = 0;

        typeInt = checkRoleModelAuth(db, modelName, localRole.getRoleID());

        return typeInt;
    }
    //检测当前用户拥有的角色对单个模型是否有访问权限

    public static int checkRoleModelAuth(com.mongodb.DB db, String modelName, String roleName) {
        try {
            DBCollection localModelGroupDefCol = db.getCollection("role_model_map");//角色模型映射表
            DBObject doc = new BasicDBObject();
            doc.put("model_name", modelName);
            doc.put("role_name", roleName);
            doc.put("enable_flag", 1);
            DBCursor localCursor = localModelGroupDefCol.find(doc);
            if (localCursor.hasNext()) {
                return 1;
            }
            return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    //查询模型组下所有模型可以搜索显示的字段 第一层输入模型组name
    public static ArrayList<SearchModelFieldDefine> GetModelFieldDefine(com.mongodb.DB db, String modelGroupName, HttpServletRequest paramRequest) {
        DBCollection localModelGroupDefCol = db.getCollection("search_model_group_define");//模型组
        Object localValue;//存放临时值的地方
        //  没有找到collection         
        if (localModelGroupDefCol == null) {
            return null;
        }
        if (modelGroupName == null || modelGroupName.compareToIgnoreCase("") == 0) {
            return null;
        }
        DBObject doc = new BasicDBObject();
        doc.put("model_group_name", modelGroupName);
        DBObject localObject = localModelGroupDefCol.findOne(doc);
        if (localObject == null) {
            return null;
        }
        ArrayList<SearchModelDefine> modelList = null;
        int modelGroupId = 0;//模型组id
        localValue = localObject.get("model_group_id");
        modelGroupId = MongoUtil.getIntValue(localObject, "model_group_id");

//        modelList = GetModelDefineList(db,modelGroupId);//得到模型组下的所有模型
        ArrayList<SearchModelFieldDefine> modelFieldList = null;
        ArrayList<SearchModelFieldDefine> modelFieldListTemp = null;
        modelFieldListTemp = modelFieldList = GetModelList(db, modelGroupId, paramRequest);//得到模型组下的所有模型搜索可显示字段
        Map<String, SearchModelFieldDefine> modelFieldMap = new HashMap<String, SearchModelFieldDefine>();
        for (int i = 0; i < modelFieldList.size(); i++) {
            SearchModelFieldDefine smfd = modelFieldList.get(i);
//            for (SearchModelFieldDefine smfd : modelFieldList) {
            if (!modelFieldMap.containsKey(smfd.getFieldName())) {
                modelFieldMap.put(smfd.getFieldName(), smfd);
            } else {
                modelFieldListTemp.remove(smfd);
            }
        }
        System.out.print(modelFieldList);
        return modelFieldListTemp;
    }

    //查询模型组下所有模型可以搜索显示的字段 第二层输入模型id
    public static ArrayList< SearchModelFieldDefine> GetModelList(com.mongodb.DB db, int modelGroupId, HttpServletRequest paramRequest) {
        DBCollection localDefCol = db.getCollection("search_model_define");
        Object localValue;//存放临时值的地方
//  没有找到collection         
        if (localDefCol == null) {
            return null;
        }
        BasicDBObject localDefCond = new BasicDBObject();
        localDefCond.put("model_group_id", modelGroupId);
        //  根据modelGroupId查找search_model_def
        DBCursor localCursor = localDefCol.find(localDefCond);
        if ((localCursor == null) || (!localCursor.hasNext())) {
            return null;
        }
        ArrayList< SearchModelFieldDefine> localModelFieldDefineList = new ArrayList< SearchModelFieldDefine>();
        while (localCursor.hasNext()) {
            DBObject localDefineObject = localCursor.next();
            if (localDefineObject == null) {
                continue;
            }
            localValue = localDefineObject.get("model_def_id");
            if (localValue == null) {
                continue;
            }
            int localModelDefId;
            if (localValue instanceof Integer) {
                localModelDefId = (int) (Integer) localValue;
            } else {
                localModelDefId = ((Double) localValue).intValue();
            }
            String modelDefName = null;//当前模型name
            localValue = localDefineObject.get("model_name");
            if (localValue == null) {
                continue;
            }
            modelDefName = localValue.toString();
            ArrayList< SearchModelFieldDefine> localFieldDefineList = GetModelFieldDefine(db, localModelDefId);
            if ((localFieldDefineList == null) || (localFieldDefineList.isEmpty())) {
                continue;
            }
            for (SearchModelFieldDefine tmpFieldDefine : localFieldDefineList) {
                if (tmpFieldDefine.getDisplayInGrid() != 1) {
                    continue;
                }
//  验证权限  
                HashMap<String, String> fieldMap = new HashMap<String, String>();
                A_Role roleList = ((AuthUserContext) paramRequest.getSession().getAttribute("AuthInfo")).getRoles();
                A_RoleModelFieldMap modelFieldMap = new A_RoleModelFieldMap();
                modelFieldMap.setRoleID(roleList.getRoleID());
                modelFieldMap.setModelname(modelDefName);
                LinkedList<A_RoleModelFieldMap> modelFieldMapsList = RoleModelFieldMapDao.GetInstance().GetListByRoleID(db, modelFieldMap);
                for (A_RoleModelFieldMap _modelField : modelFieldMapsList) {
                    fieldMap.put(_modelField.getFieldname(), _modelField.getFieldname());
                }
                String modelName = fieldMap.get(tmpFieldDefine.getFieldName());
                if (modelName != null && modelName.compareToIgnoreCase("") != 0) {
                    localModelFieldDefineList.add(tmpFieldDefine);
                }
            }
//            System.out.println("list长度："+localModelFieldDefineList.size());
        }
        return localModelFieldDefineList;
    }

    //根据模型id、模型字段name查询字段信息
    public static ArrayList<SearchModelFieldDefine> GetModelFieldDefine(com.mongodb.DB db, int modelId, String modelFieldName) {
        DBCollection localModelFieldDefCol = db.getCollection("search_model_field_define");//模型字段集合
        Object localValue;//存放临时值的地方
        SearchModelFieldDefine localModelFieldDefine;
        //  没有找到collection         
        if (localModelFieldDefCol == null) {
            return null;
        }
        if (modelId <= 0 || modelFieldName == null || modelFieldName.compareToIgnoreCase("") == 0) {
            return null;
        }
        DBObject doc = new BasicDBObject();
        doc.put("model_def_id", modelId);
        doc.put("field_name", modelFieldName);
        DBCursor localCursor = localModelFieldDefCol.find(doc);
        if ((localCursor == null) || (!localCursor.hasNext())) {
            return null;
        }

        ArrayList< SearchModelFieldDefine> localModelFieldDefineList = new ArrayList< SearchModelFieldDefine>();
        int localIntValue = 0;
        while (localCursor.hasNext()) {
            DBObject localDefineObject = localCursor.next();
            localModelFieldDefine = new SearchModelFieldDefine();
            localValue = localDefineObject.get("field_name");
            if (localValue == null) {
                continue;
            }
            localModelFieldDefine.setFieldName(localValue.toString());

            localValue = localDefineObject.get("field_display_name");
            if (localValue == null) {
                localValue = localModelFieldDefine.getFieldName();
            }
            localModelFieldDefine.setFieldDisplayName(localValue.toString());
            localValue = localDefineObject.get("search_field_name");
            if (localValue == null) {
                return null;
            }
            localModelFieldDefine.searchFieldList = localValue.toString().split(",");
            if ((localModelFieldDefine.searchFieldList != null)
                    && (localModelFieldDefine.searchFieldList.length > 1)) {
                localModelFieldDefine.setSearchFieldName(localModelFieldDefine.searchFieldList[0]);
            } else {
                localModelFieldDefine.setSearchFieldName(localValue.toString());
            }
//            localModelFieldDefine.setSearchFieldName(localValue.toString());
            localValue = localDefineObject.get("display_in_grid");
            int localDisplayInGrid;
            if (localValue == null) {
                localModelFieldDefine.setDisplayInGrid(1);
            } else {
                if (localValue instanceof Integer) {
                    localDisplayInGrid = (int) (Integer) localValue;
                } else {
                    localDisplayInGrid = ((Double) localValue).intValue();
                }
                localModelFieldDefine.setDisplayInGrid(localDisplayInGrid);
            }
            localValue = localDefineObject.get("display_width");
            int localDisplayWidth;
            if (localValue == null) {
                localModelFieldDefine.setDisplayWidth(0);
            } else {
                if (localValue instanceof Integer) {
                    localDisplayWidth = (int) (Integer) localValue;
                } else {
                    localDisplayWidth = ((Double) localValue).intValue();
                }
                localModelFieldDefine.setDisplayWidth(localDisplayWidth);
            }
            localValue = localDefineObject.get("wild_match");
            if (localValue == null) {
                localModelFieldDefine.setWildMatch(0);
            } else {
                if (localValue instanceof Integer) {
                    localIntValue = (int) (Integer) localValue;
                } else {
                    localIntValue = ((Double) localValue).intValue();
                }
                localModelFieldDefine.setWildMatch(localIntValue);
            }
            localValue = localDefineObject.get("field_spec");
            if (localValue == null) {
                localModelFieldDefine.setFieldSpec(0);
            } else {
                if (localValue instanceof Integer) {
                    localIntValue = (int) (Integer) localValue;
                } else {
                    localIntValue = ((Double) localValue).intValue();
                }
                localModelFieldDefine.setFieldSpec(localIntValue);
            }
            localModelFieldDefineList.add(localModelFieldDefine);
        }
        return localModelFieldDefineList;
    }

    public static HashSet< String> GetBlackListSet(com.mongodb.DB db) {
        DBCollection localDefCol = db.getCollection("gazhk_CZRK_SFZH");
        Object localValue;//存放临时值的地方
//  没有找到collection         
        if (localDefCol == null) {
            return null;
        }
        DBCursor localCursor = localDefCol.find();
        if (localCursor == null) {
            return null;
        }
        HashSet< String> localBlackListSet = new HashSet< String>();
        String localZjhm;
        DBObject localObject;
        while (localCursor.hasNext()) {
            localObject = localCursor.next();
            localZjhm = MongoUtil.getStringValue(localObject, "GMSFHM");
            if ((localZjhm == null)
                    || (localZjhm.isEmpty())) {
                continue;
            }
            localBlackListSet.add(localZjhm);
        }
        if (localBlackListSet.isEmpty()) {
            return null;
        }
        return localBlackListSet;
    }

    public static ArrayNode getBusiNameByField(com.mongodb.DB paramDb, String paramFieldName) {
        DBCollection busiTypeFieldDefineColl = paramDb.getCollection("query_busi_type_field_define");
        DBCollection busiTypeDefineColl = paramDb.getCollection("query_busi_type_define");
        ArrayNode localResNode = JsonNodeFactory.instance.arrayNode();
        DBObject queryObject = new BasicDBObject();
        queryObject.put("field_name", paramFieldName);
        DBCursor localCursor = busiTypeFieldDefineColl.find(queryObject);
        while (localCursor.hasNext()) {
            String tmpBusiTypeName;
            String tmpBusiTypeDisplayName;
            String tmpFieldDisplayName;
            ObjectNode tmpResNode = JsonNodeFactory.instance.objectNode();
            DBObject tmpQueryObject = new BasicDBObject();
            DBObject tmpDBObject = localCursor.next();
            tmpBusiTypeName = MongoUtil.getStringValue(tmpDBObject, "query_busi_type_name");
            tmpQueryObject.put("query_busi_type_name", tmpBusiTypeName);
            tmpBusiTypeDisplayName = MongoUtil.getStringValue(busiTypeDefineColl.findOne(tmpQueryObject), "query_busi_type_display_name");
            tmpFieldDisplayName = MongoUtil.getStringValue(tmpDBObject, "field_display_name");
            tmpResNode.put("busiName", tmpBusiTypeName);
            tmpResNode.put("busiDisplayName", tmpBusiTypeDisplayName);
            tmpResNode.put("fieldName", paramFieldName);
            tmpResNode.put("fieldDisplayName", tmpFieldDisplayName);
            localResNode.add(tmpResNode);
        }
        return localResNode;
    }

}
