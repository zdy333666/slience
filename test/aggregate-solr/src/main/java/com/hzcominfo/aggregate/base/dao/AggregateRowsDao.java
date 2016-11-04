/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.dao;

import com.hzcominfo.aggregate.base.cache.AggrModelCache;
import com.hzcominfo.aggregate.base.cache.AggrSetCache;
import com.hzcominfo.aggregate.base.pojo.AggrModel;
import com.hzcominfo.aggregate.base.pojo.AggregateInput;
import com.hzcominfo.aggregate.base.pojo.AggrModelField;
import com.hzcominfo.aggregate.base.pojo.AggrSet;
import com.hzcominfo.aggregate.base.pojo.AggrSetModelParam;
import com.hzcominfo.aggregate.base.pojo.SolrSearchInput;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zdy
 */
@Repository
public class AggregateRowsDao {

    private final Logger logger = LoggerFactory.getLogger(AggregateRowsDao.class);

    //@Autowired
    //private GABQGRKDao _GABQGRKDao;

    @Autowired
    private SolrSearchDao _SolrSearchDao;

    /**
     *
     * @param input
     * @param cache
     * @return
     * @throws java.lang.Exception
     */
    public Map<String, Object> getRows(AggregateInput input, AggrSetCache cache) throws Exception {

        //Map<String, String> condition = input.getCondition();
        AggrSet aggrSet = cache.getAggrSet();
        Collection<AggrModelCache> aggrModelCaches = cache.getAggrModelCaches().values();

//        Map<String, String> paramsOfAggrSet = new LinkedHashMap<>();
//        for (AggrSetModelParam aggrSetParam : aggrSetParams) {
//            paramsOfAggrSet.put(aggrSetParam.getModelParam(), (String) condition.get(aggrSetParam.getSetParam()));
//        }
        List<Map<String, Object>> attrNode = getRowsOfAttrSet(input, aggrModelCaches); //查询当前属性集的下属属性

        Map<String, Object> result = new HashMap<>();
        result.put("attrSetCode", aggrSet.getName());
        result.put("attr", attrNode);

        return result;
    }

    /**
     *
     * @param attrSet
     * @param attrs
     * @param paramsOfAttrSet
     * @return
     */
    private List<Map<String, Object>> getRowsOfAttrSet(AggregateInput input, Collection<AggrModelCache> caches) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        caches.parallelStream().forEach((cache) -> {
            AggrModel model = cache.getModel();
            List<AggrModelField> modelFields = cache.getFields();
            List<AggrSetModelParam> paramsInModel = cache.getParams();

            Map<String, Object> condition = input.getCondition();

            Map<String, Object> params = new LinkedHashMap<>();
            for (AggrSetModelParam paramInModel : paramsInModel) {
                params.put(paramInModel.getModelParam(), (String) condition.get(paramInModel.getSetParam()));
            }

            if (params.keySet().size() != condition.size()) {
                return;
            }

            StringBuilder queryBuilder = null;
            for (Entry<String, Object> entry : params.entrySet()) {
                if (queryBuilder == null) {
                    queryBuilder = new StringBuilder();
                    queryBuilder.append(entry.getKey()).append(":").append(entry.getValue());
                } else {
                    queryBuilder.append(" AND ").append(entry.getKey()).append(":").append(entry.getValue());
                }
            }
            if (queryBuilder == null) {
                return;
            }

            Set<String> fields = new HashSet();
            LinkedHashMap<String, Integer> sort = new LinkedHashMap();

            for (AggrModelField modelField : modelFields) {
                Integer sortType = modelField.getSortType();  //排序标志
                if (sortType == 1 || sortType == -1) {
                    sort.put(modelField.getSourceField(), sortType);
                }

                if (modelField.getDisplayEnable() == 1) {
                    fields.add(modelField.getSourceField());
                }
            }

            //----------------
            SolrSearchInput searchInput = new SolrSearchInput();
            searchInput.setUrl(model.getSearchUrl());
            searchInput.setQuery(queryBuilder.toString());
            searchInput.setFields((String[]) fields.toArray());
            searchInput.setSort(sort);
            searchInput.setRows(0);

            List<Map<String, Object>> valueDocs = _SolrSearchDao.search(searchInput);
            if (valueDocs == null || valueDocs.isEmpty()) {
                return;
            }

            List<Map<String, Object>> items = handFieldValue(valueDocs, modelFields);

            Map<String, Object> itemNode = new LinkedHashMap<>();
            itemNode.put("attrId", model.getId());
            itemNode.put("attrCode", model.getName());
            itemNode.put("attrName", model.getDisplayName());
            itemNode.put("itemType", "array");
            itemNode.put("itemFields", items);

            result.add(itemNode);
        });

        return result;
    }

    /**
     *
     * @param source
     * @param arrayAttr
     * @param attrItems
     * @param paramsOfAttrSet
     * @return
     */
    private List<Map<String, Object>> handFieldValue(List<Map<String, Object>> valueDocs, List<AggrModelField> modelFields) {

        List<String> distinctFields = new ArrayList();//去重字段列表
        Map<String, List<AggrModelField>> itemsOfField = new LinkedHashMap<>();

        for (AggrModelField modelField : modelFields) {
            if (modelField.getDisplayEnable() == 0) {
                continue;
            }

            String alias = modelField.getAlias(); //字段名

            //去重标志
            if (modelField.getDistinctEnable() == 1) {
                distinctFields.add(alias);
            }

            List<AggrModelField> items = itemsOfField.get(alias);
            if (items == null) {
                items = new ArrayList<>();
            }
            items.add(modelField);
            itemsOfField.put(alias, items);
        }

        List<Map<String, Object>> result = new ArrayList<>();

        Set<String> distinctValues = new HashSet<>();//去重值列表
        for (Map<String, Object> valueDoc : valueDocs) {

            StringBuilder distinctValueBuilder = new StringBuilder();
            Map<String, Object> itemNode = new LinkedHashMap<>();

            for (Entry<String, List<AggrModelField>> entry : itemsOfField.entrySet()) {
                String alias = entry.getKey();
                List<AggrModelField> itemModelFields = entry.getValue();

                List<String> itemValues = new ArrayList<>();
                for (AggrModelField modelField : itemModelFields) {
                    String value = parseValueToString(valueDoc.get(modelField.getSourceField()));
                    if (value.trim().isEmpty()) {
                        continue;
                    }

                    if (!itemValues.contains(value)) {
                        itemValues.add(value);
                    }
                }

                StringBuilder valueBuilder = new StringBuilder();
                int size = itemValues.size();
                for (int index = 0; index < size; index++) {
                    String value = itemValues.get(index);
                    if (index > 0) {
                        valueBuilder.append(",");
                    }
                    valueBuilder.append(value);
                }
                String value = valueBuilder.toString();

                //添加单条记录中的单个字段
                itemNode.put(alias, value);

                if (distinctFields.contains(alias)) {
                    distinctValueBuilder.append(value);
                }
            }

            //对本条记录进行去重操作
            String distinctValue = distinctValueBuilder.toString();
            if (distinctValues.contains(distinctValue)) {
                continue;
            }
            distinctValues.add(distinctValue);

            //添加单条记录
            if (!(result.contains(itemNode))) {
                result.add(itemNode);
            }
        }

        return result;
    }

    public String parseValueToString(Object object) {
        String localStr = "";
        if (object == null) {
            return localStr;
        } else if (object instanceof Boolean) {
            localStr = String.valueOf((Boolean) object);
        } else if (object instanceof Integer) {
            localStr = String.valueOf((Integer) object);
        } else if (object instanceof Long) {
            localStr = String.valueOf((Long) object);
        } else if (object instanceof Double) {
            localStr = String.valueOf((Double) object);
        } else if (object instanceof String) {
            localStr = (String) object;
        } else if (object instanceof java.util.Date) {
            localStr = String.valueOf(dateFormat.format((java.util.Date) object));
        } else {
            localStr = String.valueOf(object.toString());
        }

        return localStr;
    }

    private static final java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMddHHmmss");

}
