/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.dao;

import com.hzcominfo.aggregate.base.cache.AttrSetCache;
import com.hzcominfo.aggregate.base.conf.AggregateConfiguration;
import com.hzcominfo.aggregate.base.conf.MongoSource;
import com.hzcominfo.aggregate.base.conf.MongoSourceFactory;
import com.hzcominfo.aggregate.base.pojo.AggregateInput;
import com.hzcominfo.aggregate.base.pojo.Attr;
import com.hzcominfo.aggregate.base.pojo.AttrSet;
import com.hzcominfo.aggregate.base.pojo.AttrSetParam;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.ArrayList;
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

    @Autowired
    private GABQGRKDao _GABQGRKDao;

    /**
     *
     * @param input
     * @param cache
     * @return
     * @throws java.lang.Exception
     */
    public Map<String, Object> getRows(AggregateInput input, AttrSetCache cache) throws Exception {

        Map<String, String> condition = input.getCondition();
        List<AttrSetParam> attrSetParams = cache.getAttrSetParams();

        Map<String, String> paramsOfAttrSet = new LinkedHashMap<>();
        for (AttrSetParam attrSetParam : attrSetParams) {
            paramsOfAttrSet.put(attrSetParam.getAttrSetParam(), (String) condition.get(attrSetParam.getDimensionParam()));
        }

        List<Map<String, Object>> attrNode = getRowsOfAttrSet(input, cache, paramsOfAttrSet); //查询当前属性集的下属属性
        AttrSet attrSet = cache.getAttrSet();

        Map<String, Object> result = new HashMap<>();
        result.put("attrSetCode", attrSet.getName());
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
    private List<Map<String, Object>> getRowsOfAttrSet(AggregateInput input, AttrSetCache cache, Map<String, String> paramsOfAttrSet) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        AttrSet attrSet = cache.getAttrSet();
        List<Attr> attrs = cache.getAttrs();

        List<Attr> arrayAttrs = new ArrayList<>();
        List<Attr> groupAttrs = new ArrayList<>();
        Map<Long, List<Attr>> subAttrsInGroup = new LinkedHashMap<>();
        Map<String, List<Attr>> itemsOfColName = new LinkedHashMap<>();
        Map<String, Map<String, String>> attrMap = new LinkedHashMap<>();

        for (Attr attr : attrs) {
            long ownerId = attr.getOwnerId();
            if (ownerId != 0) {
                List<Attr> itemAttrs = subAttrsInGroup.get(ownerId);
                if (itemAttrs == null) {
                    itemAttrs = new ArrayList<>();
                }
                itemAttrs.add(attr);
                subAttrsInGroup.put(ownerId, itemAttrs);

            } else {

                String attrName = attr.getName();
                int itemType = attr.getItemType();//属性的数据类别：1 单个元素 item, 2 多个元素 array     
                int displayFlag = attr.getDisplayFlag(); //显示标志

                if (itemType == 1) {
                    String colName = attr.getCollName();
                    if (colName == null || colName.trim().isEmpty()) {
                        continue;
                    }

                    List<Attr> items = itemsOfColName.get(colName);
                    if (items == null) {
                        items = new ArrayList<>();
                    }
                    items.add(attr);
                    itemsOfColName.put(colName, items);

                    //收集单字段名称
                    if (displayFlag == 1) {
                        attrMap.put(attrName, new LinkedHashMap<>());
                    }

                } else if (displayFlag == 1) {
                    if (itemType == 2) {
                        arrayAttrs.add(attr);
                    } else if (itemType == 3) {
                        groupAttrs.add(attr);
                    }
                }
            }
        }

        MongoSource source = MongoSourceFactory.getSource(AggregateConfiguration.MONGO_URI_HZGA);
        DB db_hzga = source.getClient().getDB(source.getClientURI().getDatabase());

        //处理属性集下属的 itemAttr-----------------------------------------------------------------------
        boolean isfetched = false;

        for (Entry<String, List<Attr>> itemsOfColNameEntry : itemsOfColName.entrySet()) {
            String colName = itemsOfColNameEntry.getKey();
            List<Attr> itemsInColName = itemsOfColNameEntry.getValue();

            DBObject query = new BasicDBObject();
            DBObject fields = new BasicDBObject();
            DBObject sort = new BasicDBObject();
            Map<String, List<Attr>> itemsOfAttrName = new LinkedHashMap<>();

            for (Attr attr : itemsInColName) {
                String attrName = attr.getName(); //字段名
                String colFieldName = attr.getCollFieldName(); //目标集合字段名

                fields.put(colFieldName, 1);

                int orderFlag = attr.getSortFlag();  //排序标志
                if (orderFlag == 1) {
                    sort.put(colFieldName, 1);
                } else if (orderFlag == 2) {
                    sort.put(colFieldName, -1);
                }
                //将查询参数中的字段名转换成数据集合中的真实字段名
                if (attr.getParamFlag() == 1) {
                    if (paramsOfAttrSet.containsKey(attrName)) {
                        String paramValue = paramsOfAttrSet.get(attrName);
                        query.put(colFieldName, paramValue);
                    }
                }

                if (attr.getDisplayFlag() == 0) {
                    continue;
                }

                List<Attr> itemsInAttrName = itemsOfAttrName.get(attrName);
                if (itemsInAttrName == null) {
                    itemsInAttrName = new ArrayList<>();
                }

                itemsInAttrName.add(attr);
                itemsOfAttrName.put(attrName, itemsInAttrName);
            }

            if (query.keySet().size() != paramsOfAttrSet.size()) {
                continue;
            }

            long startTime = System.currentTimeMillis();
            List<DBObject> valueDocs = db_hzga.getCollection(colName).find(query, fields).sort(sort).limit(1).toArray();//对每个集合只取一条记录
            logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- Aggregate Query -- ").append(colName).append(" -- ").append(query).append(" -- spend time -- ").append(System.currentTimeMillis() - startTime).append(" ms").toString());

            if (valueDocs.isEmpty()) {
                continue;
            }

            isfetched = true;

            //处理 colNameKey 下属的 itemAttr-----------------------------------------------------
            for (Entry<String, List<Attr>> itemsOfAttrNameEntry : itemsOfAttrName.entrySet()) {
                String attrName = itemsOfAttrNameEntry.getKey();
                List<Attr> itemsInAttrName = itemsOfAttrNameEntry.getValue();

                List<String> itemValues = new ArrayList<>();
                for (DBObject valueDoc : valueDocs) {
                    for (Attr attr : itemsInAttrName) {
                        String colFieldName = attr.getCollFieldName(); //目标集合字段名
                        String value = parseValueToString(valueDoc.get(colFieldName));
                        if (value == null || value.trim().isEmpty() || itemValues.contains(value)) {
                            continue;
                        }

                        itemValues.add(value);
                    }
                }

                if (itemValues.isEmpty()) {
                    continue;
                }

                Map<String, String> attrValueOfCol = attrMap.get(attrName);
                if (attrValueOfCol == null) {
                    continue;
                }

                StringBuilder attrValueOfColBuilder = new StringBuilder();
                int size = itemValues.size();
                for (int index = 0; index < size; index++) {
                    String value = itemValues.get(index);
                    if (index > 0) {
                        attrValueOfColBuilder.append(",");
                    }
                    attrValueOfColBuilder.append(value);
                }

                attrValueOfCol.put(colName, attrValueOfColBuilder.toString());
            }
        }

        if (attrSet.getId() == 1 && isfetched != true) {

            //在mongo数据库中的全国人口备份库中进行查找
            long startTime = System.currentTimeMillis();
            Map<String, String> doc = _GABQGRKDao.fetchGABQGRKBackup(db_hzga, paramsOfAttrSet, attrMap.keySet());
            logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- Aggregate Query -- ").append("GABQGRK_BACKUP").append(" -- ").append(paramsOfAttrSet).append(" -- spend time -- ").append(System.currentTimeMillis() - startTime).append(" ms").toString());

            if (doc == null) {
                //去全国人口信息库中查找
                startTime = System.currentTimeMillis();
                doc = _GABQGRKDao.fetchGABQGRK(input.getAuth(), paramsOfAttrSet, attrMap.keySet());
                logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- Aggregate Query -- ").append("GABQGRK").append(" -- ").append(paramsOfAttrSet).append(" -- spend time -- ").append(System.currentTimeMillis() - startTime).append(" ms").toString());
            }

            String baseAttrColName = "GABQGRK";
            if (doc != null) {
                for (String attrName : attrMap.keySet()) {
                    String Value = doc.get(attrName);
                    Map<String, String> attrValueMap = attrMap.get(attrName);
                    attrValueMap.put(baseAttrColName, Value);
                }
            }
        }

        for (Entry<String, Map<String, String>> entry : attrMap.entrySet()) {
            String attrName = entry.getKey();
            List<String> attrValues = new ArrayList<>();

            if (paramsOfAttrSet.containsKey(attrName)) {
                String attrValue = paramsOfAttrSet.get(attrName);
                if (attrValue == null || attrValue.trim().isEmpty()) {
                    continue;
                }

                String[] attrValueArray = attrValue.split(",");
                for (String tempAttrValue : attrValueArray) {
                    if (!attrValues.contains(tempAttrValue)) {
                        attrValues.add(tempAttrValue);
                    }
                }
            } else {
                Map<String, String> attrValueOfCol = entry.getValue();
                for (String attrValue : attrValueOfCol.values()) {
                    if (attrValue == null || attrValue.trim().isEmpty()) {
                        continue;
                    }
                    String[] attrValueArray = attrValue.split(",");
                    for (String tempAttrValue : attrValueArray) {
                        if (!attrValues.contains(tempAttrValue)) {
                            attrValues.add(tempAttrValue);
                        }
                    }
                }
            }

            StringBuilder attrValueSB = new StringBuilder();
            int size = attrValues.size();
            for (int index = 0; index < size; index++) {
                String tempAttrValue = attrValues.get(index);
                if (index > 0) {
                    attrValueSB.append(",");
                }
                attrValueSB.append(tempAttrValue);
            }

            Map<String, Object> attrNode = new LinkedHashMap<>();
            attrNode.put("attrCode", attrName);
            attrNode.put("itemType", "item");
            attrNode.put("attrValue", attrValueSB.toString());

            result.add(attrNode);
        }

        //处理属性集中的 arrayAttr-----------------------------------------------------------------------
        arrayAttrs.parallelStream().forEach((arrayAttr) -> {
            List<Attr> attrItems = subAttrsInGroup.get(arrayAttr.getId());
            if (!(attrItems == null || attrItems.isEmpty())) {

                List<Map<String, Object>> itemsNode = null;
                try {
                    itemsNode = handArrayAttr(source, arrayAttr, attrItems, paramsOfAttrSet);
                } catch (Exception e) {
                    logger.error(null, e);
                }

                if (!(itemsNode == null || itemsNode.isEmpty())) {
                    Map<String, Object> arrayNode = new LinkedHashMap<>();

                    arrayNode.put("attrCode", arrayAttr.getName());//字段名
                    arrayNode.put("attrName", arrayAttr.getDisplayName());
                    arrayNode.put("itemType", "array");
                    arrayNode.put("itemFields", itemsNode);

                    result.add(arrayNode);
                }
            }
        });

        //处理属性集下属的 groupAttr------------------------------------------------------
        for (Attr groupAttr : groupAttrs) {
            List<Attr> groupItems = subAttrsInGroup.get(groupAttr.getId());
            if (groupItems == null || groupItems.isEmpty()) {
                continue;
            }
            String attrName = groupAttr.getName();

            long startTime = System.currentTimeMillis();

            List<Map<String, Object>> groupItemsNode = new ArrayList<>();
            for (Attr arrayAttr : groupItems) {
                List<Attr> attrItems = subAttrsInGroup.get(arrayAttr.getId());
                if (attrItems == null || attrItems.isEmpty()) {
                    continue;
                }

                List<Map<String, Object>> itemsNode = handArrayAttr(source, arrayAttr, attrItems, paramsOfAttrSet);
                if (itemsNode == null || itemsNode.isEmpty()) {
                    continue;
                }

                Map<String, Object> arrayNode = new LinkedHashMap<>();
                arrayNode.put("attrCode", arrayAttr.getName());//字段名
                arrayNode.put("attrName", arrayAttr.getDisplayName());
                arrayNode.put("itemType", "array");
                arrayNode.put("itemFields", itemsNode);

                groupItemsNode.add(arrayNode);
            }

            logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- Aggregate Group -- ").append(attrName).append(" -- ").append(groupItemsNode.size()).append(" -- spend time -- ").append(System.currentTimeMillis() - startTime).append(" ms").toString());

            Map<String, Object> groupNode = new LinkedHashMap<>();
            groupNode.put("attrCode", attrName);
            groupNode.put("attrName", groupAttr.getDisplayName());
            groupNode.put("itemType", "group");
            groupNode.put("groupItems", groupItemsNode);

            result.add(groupNode);
        }

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
    private List<Map<String, Object>> handArrayAttr(MongoSource source, Attr arrayAttr, List<Attr> attrItems, Map<String, String> paramsOfAttrSet) throws Exception {

        String arrayAttrColName = arrayAttr.getCollName(); //目标集合名

        //处理下属的的 itemAttr-----------------------------------
        DBObject query = new BasicDBObject();
        DBObject fields = new BasicDBObject();
        DBObject sort = new BasicDBObject();

        Map<String, List<Attr>> itemsOfAttrName = new LinkedHashMap<>();
        List<String> distinctFields = new ArrayList();//去重字段列表

        for (Attr attr : attrItems) {
            String attrName = attr.getName(); //字段名
            String colFieldName = attr.getCollFieldName();

            Integer orderFlag = attr.getSortFlag();  //排序标志
            if (orderFlag == 1) {
                sort.put(colFieldName, 1);
            } else if (orderFlag == 2) {
                sort.put(colFieldName, -1);
            }
            //将查询参数中的字段名转换成数据集合中的真实字段名
            if (attr.getParamFlag() == 1) {
                if (paramsOfAttrSet.containsKey(attrName)) {
                    String paramValue = paramsOfAttrSet.get(attrName);
                    query.put(colFieldName, paramValue);
                }
            }

            if (attr.getDisplayFlag() == 0) {
                continue;
            }

            fields.put(attr.getCollFieldName(), 1);

            //去重标志
            if (attr.getDistinctFlag() == 1) {
                distinctFields.add(attrName);
            }

            List<Attr> items = itemsOfAttrName.get(attrName);
            if (items == null) {
                items = new ArrayList<>();
            }

            items.add(attr);
            itemsOfAttrName.put(attrName, items);
        }

        if (query.keySet().size() != paramsOfAttrSet.size()) {
            return null;
        }

        //添加扩展的查询条件--------------------------
        //------------------------------------------
        DB db_hzga = source.getClient().getDB(source.getClientURI().getDatabase());

        long startTime = System.currentTimeMillis();
        List<DBObject> valueDocs = db_hzga.getCollection(arrayAttrColName).find(query, fields).sort(sort).toArray();
        logger.info(new StringBuilder(Thread.currentThread().getName()).append(" -- Aggregate Query -- ").append(arrayAttrColName).append(" -- ").append(query).append(" -- spend time -- ").append(System.currentTimeMillis() - startTime).append(" ms").toString());

        if (valueDocs.isEmpty()) {
            return null;
        }

        List<Map<String, Object>> result = new ArrayList<>();
        Set<String> distinctValues = new HashSet<>();//去重值列表

        //对每一个attr进行处理-----------------------------------------
        for (DBObject valueDoc : valueDocs) {
            boolean isNotEmptyFlag = false;
            StringBuilder distinctValueBuilder = new StringBuilder();

            Map<String, Object> itemNode = new LinkedHashMap<>();

            for (Entry<String, List<Attr>> entry : itemsOfAttrName.entrySet()) {
                String attrName = entry.getKey();
                List<Attr> items = entry.getValue();

                List<String> itemValues = new ArrayList<>();
                for (Attr attr : items) {
                    if (attr.getDisplayFlag() == 0) {
                        continue;
                    }

                    String colFieldName = attr.getCollFieldName(); //目标集合字段名
                    String value = parseValueToString(valueDoc.get(colFieldName));
                    if (value == null || value.trim().isEmpty()) {
                        continue;
                    }

                    if (!itemValues.contains(value)) {
                        itemValues.add(value);
                    }
                }

                StringBuilder finalValueBuilder = new StringBuilder();

                int size = itemValues.size();
                for (int index = 0; index < size; index++) {
                    String value = itemValues.get(index);
                    if (index > 0) {
                        finalValueBuilder.append(",");
                    }
                    finalValueBuilder.append(value);
                }

                String finalValue = finalValueBuilder.toString();
                if (!finalValue.trim().isEmpty()) {
                    isNotEmptyFlag = true;
                }

                //添加单条记录中的单个字段
                itemNode.put(attrName, finalValue);

                if (distinctFields.contains(attrName)) {
                    distinctValueBuilder.append(finalValue);
                }
            }

            //对本条记录进行去重操作
            String distinctValue = distinctValueBuilder.toString();
            if (distinctValues.contains(distinctValue)) {
                continue;
            }
            distinctValues.add(distinctValue);

            //添加单条记录
            if (!isNotEmptyFlag || result.contains(itemNode)) {
                continue;
            }

            result.add(itemNode);
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
