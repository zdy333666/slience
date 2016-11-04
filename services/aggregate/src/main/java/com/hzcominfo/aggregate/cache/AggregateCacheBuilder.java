/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.cache;

import com.hzcominfo.aggregate.conf.AggregateConfiguration;
import com.hzcominfo.aggregate.conf.MongoSource;
import com.hzcominfo.aggregate.conf.MongoSourceFactory;
import com.hzcominfo.aggregate.pojo.Attr;
import com.hzcominfo.aggregate.pojo.AttrSet;
import com.hzcominfo.aggregate.pojo.AttrSetParam;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.stereotype.Component;

/**
 *
 * @author zdy
 */
@Component
public class AggregateCacheBuilder {

    /**
     *
     * @param dimension
     * @return
     */
    public DimensionCache buildCache(String dimension, long setId) throws ParseException {
        DBObject query = new BasicDBObject();
        query.put("query_dimension_name", dimension);
        
        MongoSource source = MongoSourceFactory.getSource(AggregateConfiguration.MONGO_URI_RTDB);
        DB db = source.getClient().getDB(source.getClientURI().getDatabase());
        DBObject dimensionDoc = db.getCollection(AggregateConfiguration.AGGREGATE_DIMENSION).findOne(query, new BasicDBObject().append("_id", 0));
        if (dimensionDoc == null) {
            return null;
        }
        
        query = new BasicDBObject();
        query.put("query_dimension_name", dimension);
        query.put("query_attrset_id", setId);
        query.put("enable_flag", 1);
        
        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("query_attrset_id", 1);
        fields.put("query_attrset_name", 1);
        fields.put("query_attrset_display_name", 1);
        fields.put("aggr_flag", 1);
        
        DBObject sort = new BasicDBObject();
        sort.put("display_order", 1);
        
        List<DBObject> attrSetDocs = db.getCollection(AggregateConfiguration.AGGREGATE_DIMENSION_ATTRSET).find(query, fields).sort(sort).toArray();
        Map<Long, AttrSet> distinctAttrSets = new LinkedHashMap<>();
        
        NumberFormat numberFormat = NumberFormat.getInstance();
        for (DBObject attrSetDoc : attrSetDocs) {
            
            String attrSetName = String.valueOf(attrSetDoc.get("query_attrset_name"));
            if (attrSetName == null || attrSetName.trim().isEmpty()) {
                continue;
            }
            
            long attrSetId = numberFormat.parse(String.valueOf(attrSetDoc.get("query_attrset_id"))).longValue();
            
            AttrSet attrSet = new AttrSet();
            attrSet.setId(attrSetId);
            attrSet.setName(attrSetName);
            attrSet.setDisplayName(String.valueOf(attrSetDoc.get("query_attrset_display_name")));
            
            distinctAttrSets.put(attrSetId, attrSet);
        }
        
        query = new BasicDBObject();
        query.put("query_dimension_name", dimension);
        query.put("query_attrset_id", new BasicDBObject().append("$in", distinctAttrSets.keySet().toArray()));
        
        fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("query_dimension_param", 1);
        fields.put("query_attrset_id", 1);
        fields.put("query_attrset_field", 1);
        
        sort = new BasicDBObject();
        sort.put("display_order", 1);
        
        List<DBObject> attrSetParamDocs = db.getCollection(AggregateConfiguration.AGGREGATE_ATTRSET_PARAM).find(query, fields).sort(sort).toArray();
        
        Map<Long, List<AttrSetParam>> distinctAttrSetParamsInGroup = new LinkedHashMap<>();
        for (DBObject attrSetParamDoc : attrSetParamDocs) {
            
            String dimension_param = String.valueOf(attrSetParamDoc.get("query_dimension_param"));
            if (dimension_param == null || dimension_param.trim().isEmpty()) {
                continue;
            }
            
            String attrset_param = String.valueOf(attrSetParamDoc.get("query_attrset_field"));
            if (attrset_param == null || attrset_param.trim().isEmpty()) {
                continue;
            }
            
            long attrSetId = numberFormat.parse(String.valueOf(attrSetParamDoc.get("query_attrset_id"))).longValue();
            
            AttrSetParam attrSetParam = new AttrSetParam();
            attrSetParam.setAttrSetId(attrSetId);
            attrSetParam.setDimensionParam(dimension_param);
            attrSetParam.setAttrSetParam(attrset_param);
            
            List<AttrSetParam> attrSetParams = distinctAttrSetParamsInGroup.get(attrSetId);
            if (attrSetParams == null) {
                attrSetParams = new ArrayList<>();
            }
            attrSetParams.add(attrSetParam);
            distinctAttrSetParamsInGroup.put(attrSetId, attrSetParams);
        }

        //---------------
        for (long attrSetId : distinctAttrSets.keySet()) {
            if (!distinctAttrSetParamsInGroup.containsKey(attrSetId)) {
                distinctAttrSets.remove(attrSetId);
            }
        }
        //---------------

        query = new BasicDBObject();
        query.put("query_attrset_id", new BasicDBObject().append("$in", distinctAttrSets.keySet().toArray()));
        
        fields = new BasicDBObject();
        fields.put("_id", 0);
        
        fields.put("query_attrset_id", 1);
        fields.put("query_attr_def_id", 1);
        fields.put("attr_name", 1);
        fields.put("attr_display_name", 1);
        fields.put("query_attr_item_type", 1);
        fields.put("query_attr_owner_id", 1);
        fields.put("col_name", 1);
        fields.put("col_alias", 1);
        fields.put("col_field_name", 1);
        
        fields.put("param_flag", 1);
        fields.put("distinct_flag", 1);
        fields.put("display_flag", 1);
        fields.put("order_flag", 1);
        fields.put("field_spec", 1);
        
        sort = new BasicDBObject();
        sort.put("field_order", 1);
        
        List<DBObject> attrDocs = db.getCollection(AggregateConfiguration.AGGREGATE_ATTR).find(query, fields).sort(sort).toArray();
        
        Map<Long, List<Attr>> attrsInGroup = new LinkedHashMap<>();
        for (DBObject attrDoc : attrDocs) {
            String attr_name = String.valueOf(attrDoc.get("attr_name"));
            if (attr_name == null || attr_name.trim().isEmpty()) {
                continue;
            }
            
            long attrSetId = numberFormat.parse(String.valueOf(attrDoc.get("query_attrset_id"))).longValue();
            
            Attr attr = new Attr();
            attr.setId(numberFormat.parse(String.valueOf(attrDoc.get("query_attr_def_id"))).longValue());
            attr.setName(attr_name);
            attr.setDisplayName(String.valueOf(attrDoc.get("attr_display_name")));
            attr.setItemType(numberFormat.parse(String.valueOf(attrDoc.get("query_attr_item_type"))).intValue());
            attr.setOwnerId(numberFormat.parse(String.valueOf(attrDoc.get("query_attr_owner_id"))).longValue());
            attr.setCollName(String.valueOf(attrDoc.get("col_name")));
            attr.setCollAlias(String.valueOf(attrDoc.get("col_alias")));
            attr.setCollFieldName(String.valueOf(attrDoc.get("col_field_name")));
            attr.setParamFlag(numberFormat.parse(String.valueOf(attrDoc.get("param_flag"))).intValue());
            attr.setDistinctFlag(numberFormat.parse(String.valueOf(attrDoc.get("distinct_flag"))).intValue());
            attr.setDisplayFlag(numberFormat.parse(String.valueOf(attrDoc.get("display_flag"))).intValue());
            attr.setSortFlag(numberFormat.parse(String.valueOf(attrDoc.get("order_flag"))).intValue());
            attr.setFieldSpec(numberFormat.parse(String.valueOf(attrDoc.get("field_spec"))).intValue());
            attr.setFieldOrder(numberFormat.parse(String.valueOf(attrDoc.get("field_order"))).intValue());
            
            List<Attr> attrs = attrsInGroup.get(attrSetId);
            if (attrs == null) {
                attrs = new ArrayList<>();
            }
            attrs.add(attr);
            
            attrsInGroup.put(attrSetId, attrs);
        }
        
        Map<Long, AttrSetCache> attrSetCaches = new LinkedHashMap<>();
        for (Entry<Long, AttrSet> entry : distinctAttrSets.entrySet()) {
            long attrSetId = entry.getKey();
            
            List<Attr> attrs = attrsInGroup.get(attrSetId);
            if (attrs == null) {
                continue;
            }
            
            AttrSetCache attrSetCache = new AttrSetCache();
            attrSetCache.setAttrSet(entry.getValue());
            attrSetCache.setAttrSetParams(distinctAttrSetParamsInGroup.get(attrSetId));
            attrSetCache.setAttrs(attrs);
            
            attrSetCaches.put(attrSetId, attrSetCache);
        }
        
        DimensionCache dimensionCache = new DimensionCache();
        dimensionCache.setDimension(dimension);
        dimensionCache.setAttrSetCaches(attrSetCaches);
        
        return dimensionCache;
    }
    
}
