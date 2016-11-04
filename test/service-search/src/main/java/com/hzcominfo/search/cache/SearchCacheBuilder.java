/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.cache;

import com.hzcominfo.search.conf.SearchConfiguration;
import com.hzcominfo.search.conf.MongoSource;
import com.hzcominfo.search.conf.MongoSourceFactory;
import com.hzcominfo.search.pojo.SearchModel;
import com.hzcominfo.search.pojo.SearchModelField;
import com.hzcominfo.search.service.SearchInput;
import com.hzcominfo.search.util.MongoValuePaser;
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
public class SearchCacheBuilder {

    /**
     *
     * @param input
     * @return
     * @throws java.text.ParseException
     */
    public static SearchModelCache buildCache(SearchInput input) throws ParseException {

        String modelName = input.getBusiModel();

        DBObject query = new BasicDBObject();
        query.put("model_name", modelName);
        query.put("enable_flag", 1);

        DBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("model_name", 0);
        fields.put("enable_flag", 0);

        MongoSource source = MongoSourceFactory.getSource(SearchConfiguration.MONGO_URI_RTDB);
        DB db = source.getClient().getDB(source.getClientURI().getDatabase());
        DBObject modelDoc = db.getCollection(SearchConfiguration.SEARCH_MODEL).findOne(query, fields);
        if (modelDoc == null) {
            return null;
        }

        long modelId = MongoValuePaser.parseLong(modelDoc.get("model_def_id"));

        SearchModel model = new SearchModel();
        model.setGroupId(MongoValuePaser.parseLong(modelDoc.get("model_group_id")));
        model.setId(modelId);
        model.setName(modelName);
        model.setDisplayName(MongoValuePaser.parseString(modelDoc.get("model_display_name")));
        model.setViewName(MongoValuePaser.parseString(modelDoc.get("model_view_name")));
        model.setSearchUrl(MongoValuePaser.parseString(modelDoc.get("search_url")));
        model.setSearchSort(MongoValuePaser.parseString(modelDoc.get("search_sort")));
        model.setSelectedDefault(MongoValuePaser.parseInt(modelDoc.get("selected_default")));
        model.setUseSpec(MongoValuePaser.parseInt(modelDoc.get("use_spec")));
        model.setDisplayOrder(MongoValuePaser.parseInt(modelDoc.get("display_order")));
        model.setAddComment(MongoValuePaser.parseString(modelDoc.get("add_comment")));
        model.setEnable(1);

        query = new BasicDBObject();
        query.put("model_def_id", modelId);

        fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("model_def_id", 0);

        DBObject sort = new BasicDBObject();
        sort.put("field_order", 1);

        List<DBObject> modelFieldDocs = db.getCollection(SearchConfiguration.SEARCH_MODEL_FIELD).find(query, fields).sort(sort).toArray();

        List<SearchModelField> modelFields = new ArrayList<>();
        for (DBObject modelFieldDoc : modelFieldDocs) {

            String fieldName = MongoValuePaser.parseString(modelFieldDoc.get("field_name"));
            if (fieldName == null || fieldName.trim().isEmpty()) {
                continue;
            }

            String sourceField = MongoValuePaser.parseString(modelFieldDoc.get("search_field_name"));
            if (sourceField == null || sourceField.trim().isEmpty()) {
                continue;
            }

            String[] sourceFields = sourceField.split(",");
            if (sourceFields.length > 1) {
                sourceField = sourceFields[0];
            }

            SearchModelField modelField = new SearchModelField();
            modelField.setModelId(modelId);
            modelField.setId(MongoValuePaser.parseLong(modelFieldDoc.get("model_field_def_id")));
            modelField.setName(fieldName);
            modelField.setDisplayName(MongoValuePaser.parseString(modelFieldDoc.get("field_display_name")));
            modelField.setSourceField(sourceField);
            modelField.setSourceFields(sourceFields);
            modelField.setDisplayInGrid(MongoValuePaser.parseInt(modelFieldDoc.get("display_in_grid")));
            modelField.setDisplayInCollision(MongoValuePaser.parseInt(modelFieldDoc.get("display_in_collision")));
            modelField.setFieldOrder(MongoValuePaser.parseInt(modelFieldDoc.get("field_order")));
            modelField.setFieldSpec(MongoValuePaser.parseInt(modelFieldDoc.get("field_spec")));
            modelField.setWildMatch(MongoValuePaser.parseInt(modelFieldDoc.get("wild_match")));
            modelField.setAddressFlag(MongoValuePaser.parseInt(modelFieldDoc.get("address_flag")));
            modelField.setListDisplayOrder(MongoValuePaser.parseInt(modelFieldDoc.get("list_display_order")));
            modelField.setLargeIconDisplayOrder(MongoValuePaser.parseInt(modelFieldDoc.get("large_icon_display_order")));
            modelField.setLatticeNum(MongoValuePaser.parseInt(modelFieldDoc.get("lattice_num")));
            modelField.setRowNum(MongoValuePaser.parseInt(modelFieldDoc.get("row_num")));
            modelField.setPkField(MongoValuePaser.parseInt(modelFieldDoc.get("pk_field")));
            modelField.setDisplayFieldNameFlag(MongoValuePaser.parseInt(modelFieldDoc.get("display_field_name_flag")));
            modelField.setStatisticsFilterField(MongoValuePaser.parseInt(modelFieldDoc.get("statistics_filter_field")));
            modelField.setStatisticsFilterCode1(MongoValuePaser.parseString(modelFieldDoc.get("statistics_filter_code1")));
            modelField.setStatisticsFilterCode2(MongoValuePaser.parseString(modelFieldDoc.get("statistics_filter_code2")));
            modelField.setStatisticsFilterCode3(MongoValuePaser.parseString(modelFieldDoc.get("statistics_filter_code3")));
            modelField.setStatisticsFilterFieldOrder(MongoValuePaser.parseInt(modelFieldDoc.get("statistics_filter_field_order")));
            modelField.setListFieldFlag(MongoValuePaser.parseInt(modelFieldDoc.get("list_field_flag")));
            modelField.setLargeIconFieldFlag(MongoValuePaser.parseInt(modelFieldDoc.get("large_icon_field_flag")));
            modelField.setPolymerizerFlag(MongoValuePaser.parseInt(modelFieldDoc.get("polymerizer_flag")));
            modelField.setDetailFlag(MongoValuePaser.parseInt(modelFieldDoc.get("detail_flag")));

            modelFields.add(modelField);
        }

        SearchModelCache cache = new SearchModelCache();
        cache.setModel(model);
        cache.setModelFields(modelFields);

        return cache;
    }

}
