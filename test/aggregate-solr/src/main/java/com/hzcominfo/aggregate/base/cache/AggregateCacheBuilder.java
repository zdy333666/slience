/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.cache;

import com.hzcominfo.aggregate.base.conf.AggregateConfiguration;
import com.hzcominfo.aggregate.base.conf.MongoSource;
import com.hzcominfo.aggregate.base.conf.MongoSourceFactory;
import com.hzcominfo.aggregate.base.pojo.AggrModel;
import com.hzcominfo.aggregate.base.pojo.AggrModelField;
import com.hzcominfo.aggregate.base.pojo.AggrSet;
import com.hzcominfo.aggregate.base.pojo.AggrSetModelParam;
import com.hzcominfo.aggregate.base.pojo.AggregateInput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 *
 * @author zdy
 */
@Component
public class AggregateCacheBuilder {

    /**
     *
     * @param input
     * @return
     * @throws java.text.ParseException
     */
    public AggrSetCache buildCache(AggregateInput input) throws ParseException {

        long dimensionId = input.getDimensionId();
        long setId = input.getSetId();
        Map<String, String> auth = input.getAuth();
        String role = auth.get("role");

        DBObject query = new BasicDBObject();
        query.put("dimension_id", dimensionId);
        query.put("enable", 1);

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("id", 1);
        fields.put("name", 1);
        fields.put("display_name", 1);

        MongoSource source = MongoSourceFactory.getSource(AggregateConfiguration.MONGO_URI_RTDB);
        DB db = source.getClient().getDB(source.getClientURI().getDatabase());
        DBObject aggrSetDoc = db.getCollection(AggregateConfiguration.AGGREGATE_SET).findOne(query, new BasicDBObject().append("_id", 0).append("id", 1));
        if (aggrSetDoc == null) {
            return null;
        }

        NumberFormat numberFormat = NumberFormat.getInstance();

        AggrSet aggrSet = new AggrSet();
        aggrSet.setId(setId);
        aggrSet.setName(String.valueOf(aggrSetDoc.get("name")));
        aggrSet.setDisplayName(String.valueOf(aggrSetDoc.get("display_name")));

        query = new BasicDBObject();
        query.put("set_id", setId);
        query.put("enable", 1);

        fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("id", 1);
        fields.put("name", 1);
        fields.put("display_name", 1);

        DBObject sort = new BasicDBObject();
        sort.put("display_order", 1);

        List<DBObject> aggrModelDocs = db.getCollection(AggregateConfiguration.AGGREGATE_MODEL).find(query, fields).sort(sort).toArray();

        Set<String> distinctModelNames = new HashSet<>();
        for (DBObject aggrModelDoc : aggrModelDocs) {
            String modelName = String.valueOf(aggrModelDoc.get("name"));
            if (modelName == null || modelName.trim().isEmpty()) {
                continue;
            }

            distinctModelNames.add(modelName);
        }

        query = new BasicDBObject();
        query.put("model_view_name", new BasicDBObject().append("$in", distinctModelNames.toArray()));
        query.put("enable_flag", 1);

        fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("model_view_name", 1);
        fields.put("model_name", 1);
        fields.put("search_url", 1);

        List<DBObject> searchModelDocs = db.getCollection(AggregateConfiguration.SEARCH_MODEL).find(query, fields).toArray();

        Map<String, DBObject> searchModels = new LinkedHashMap<>();
        for (DBObject searchModelDoc : searchModelDocs) {
            String searchModelName = String.valueOf(searchModelDoc.get("model_view_name"));
            searchModels.put(searchModelName, searchModelDoc);
        }

        Map<Long, AggrModel> distinctAggrModels = new LinkedHashMap<>();
        for (DBObject aggrModelDoc : aggrModelDocs) {
            String modelName = String.valueOf(aggrModelDoc.get("name"));
            DBObject searchModelDoc = searchModels.get(modelName);
            if (searchModelDoc == null) {
                continue;
            }

            long modelId = numberFormat.parse(String.valueOf(aggrModelDoc.get("id"))).longValue();

            AggrModel aggrModel = new AggrModel();
            aggrModel.setId(modelId);
            aggrModel.setName(modelName);
            aggrModel.setDisplayName(String.valueOf(aggrModelDoc.get("display_name")));
            aggrModel.setSearchModel(String.valueOf(searchModelDoc.get("model_name")));
            aggrModel.setSearchUrl(String.valueOf(searchModelDoc.get("search_url")));

            distinctAggrModels.put(modelId, aggrModel);
        }

        query = new BasicDBObject();
        query.put("set_id", setId);
        query.put("model_id", new BasicDBObject().append("$in", distinctAggrModels.keySet().toArray()));

        fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("set_param", 1);
        fields.put("model_id", 1);
        fields.put("model_param", 1);

        sort = new BasicDBObject();
        sort.put("param_order", 1);

        List<DBObject> aggrSetModelParamDocs = db.getCollection(AggregateConfiguration.AGGREGATE_SET_MODEL_PARAM).find(query, fields).sort(sort).toArray();

        Map<Long, List<AggrSetModelParam>> distinctParamsGroupByModel = new LinkedHashMap<>();
        for (DBObject aggrSetModelParamDoc : aggrSetModelParamDocs) {

            String setParam = String.valueOf(aggrSetModelParamDoc.get("set_param"));
            if (setParam == null || setParam.trim().isEmpty()) {
                continue;
            }

            String modelParam = String.valueOf(aggrSetModelParamDoc.get("model_param"));
            if (modelParam == null || modelParam.trim().isEmpty()) {
                continue;
            }

            long modelId = numberFormat.parse(String.valueOf(aggrSetModelParamDoc.get("model_id"))).longValue();

            AggrSetModelParam aggrSetModelParam = new AggrSetModelParam();
            aggrSetModelParam.setSetParam(setParam);
            aggrSetModelParam.setModelId(modelId);
            aggrSetModelParam.setModelParam(modelParam);

            List<AggrSetModelParam> paramsInModel = distinctParamsGroupByModel.get(modelId);
            if (paramsInModel == null) {
                paramsInModel = new ArrayList<>();
            }
            paramsInModel.add(aggrSetModelParam);
            distinctParamsGroupByModel.put(modelId, paramsInModel);
        }

        //---------------
        Map<Long, AggrModel> availiableAggrModels = new LinkedHashMap<>();
        for (Entry<Long, AggrModel> entry:distinctAggrModels.entrySet()){
            if (distinctParamsGroupByModel.containsKey(entry.getKey())) {
                availiableAggrModels.put(entry.getKey(),entry.getValue());
            }
        }
        distinctAggrModels=null;
        //---------------

        query = new BasicDBObject();
        query.put("enable", 1);
        query.put("model_id", new BasicDBObject().append("$in", availiableAggrModels.keySet().toArray()));

        fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("model_id", 1);
        fields.put("id", 1);
        fields.put("name", 1);
        fields.put("alias", 1);
        fields.put("display_name", 1);

        fields.put("display_enable", 1);
        fields.put("distinct_enable", 1);
        fields.put("sort_type", 1);
        fields.put("field_spec", 1);

        sort = new BasicDBObject();
        sort.put("display_order", 1);

        List<DBObject> modelFieldDocs = db.getCollection(AggregateConfiguration.AGGREGATE_MODEL_FIELD).find(query, fields).sort(sort).toArray();

        Map<Long, List<AggrModelField>> modelFieldsGroupByModel = new LinkedHashMap<>();
        for (DBObject modelFieldDoc : modelFieldDocs) {
            String name = String.valueOf(modelFieldDoc.get("name"));
            if (name == null || name.trim().isEmpty()) {
                continue;
            }

            String alias = String.valueOf(modelFieldDoc.get("alias"));
            if (alias == null || alias.trim().isEmpty()) {
                continue;
            }

            long modelId = numberFormat.parse(String.valueOf(modelFieldDoc.get("model_id"))).longValue();

            AggrModelField aggrModelField = new AggrModelField();
            aggrModelField.setId(numberFormat.parse(String.valueOf(modelFieldDoc.get("id"))).longValue());
            aggrModelField.setName(name);
            aggrModelField.setAlias(alias);
            aggrModelField.setDisplayName(String.valueOf(modelFieldDoc.get("display_name")));
            aggrModelField.setDisplayEnable(numberFormat.parse(String.valueOf(modelFieldDoc.get("display_enable"))).intValue());
            aggrModelField.setDistinctEnable(numberFormat.parse(String.valueOf(modelFieldDoc.get("distinct_enable"))).intValue());
            aggrModelField.setSortType(numberFormat.parse(String.valueOf(modelFieldDoc.get("sort_type"))).intValue());
            aggrModelField.setFieldSpec(numberFormat.parse(String.valueOf(modelFieldDoc.get("field_spec"))).intValue());

            List<AggrModelField> modelFields = modelFieldsGroupByModel.get(modelId);
            if (modelFields == null) {
                modelFields = new ArrayList<>();
            }
            modelFields.add(aggrModelField);

            modelFieldsGroupByModel.put(modelId, modelFields);
        }

        Map<Long, AggrModelCache> aggrModelCaches = new LinkedHashMap<>();
        for (Entry<Long, AggrModel> entry : availiableAggrModels.entrySet()) {
            long aggrModelId = entry.getKey();

            List<AggrModelField> modelFields = modelFieldsGroupByModel.get(aggrModelId);
            if (modelFields == null) {
                continue;
            }

            AggrModelCache aggrModelCache = new AggrModelCache();
            aggrModelCache.setModel(entry.getValue());
            aggrModelCache.setParams(distinctParamsGroupByModel.get(aggrModelId));
            aggrModelCache.setFields(modelFields);

            aggrModelCaches.put(aggrModelId, aggrModelCache);
        }

        AggrSetCache cache = new AggrSetCache();
        cache.setAggrSet(aggrSet);
        cache.setAggrModelCaches(aggrModelCaches);

        return cache;
    }

    /**
     *
     * @return
     */
    public Map<String, String> buildModelSearchCache(DB db, String role) {

        DBObject query = new BasicDBObject();
        query.put("enable_flag", 1);
        query.put("role_name", "role");

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("model_name", 1);

//        DBObject sort = new BasicDBObject();
//        sort.put("display_order", 1);
        List<DBObject> modelInRoleDocs = db.getCollection(AggregateConfiguration.AGGREGATE_MODEL_FIELD).find(query, fields).toArray();
        Set<String> modelNames = new HashSet<>();
        for (DBObject modelInRoleDoc : modelInRoleDocs) {
            modelNames.add(String.valueOf(modelInRoleDoc.get("model_name")));
        }

        return null;
    }

}
