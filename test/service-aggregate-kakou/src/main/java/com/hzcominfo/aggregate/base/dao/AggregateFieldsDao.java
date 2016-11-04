package com.hzcominfo.aggregate.base.dao;

import com.hzcominfo.aggregate.base.cache.AttrSetCache;
import com.hzcominfo.aggregate.base.pojo.Attr;
import com.hzcominfo.aggregate.base.pojo.AttrSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zdy
 */
@Repository
public class AggregateFieldsDao {

    public Map<String, Object> getFields(AttrSetCache cache) {

        AttrSet attrSet = cache.getAttrSet();
        List<Attr> attrs = cache.getAttrs();

        //long attrSetId = attrSet.getId();
        String attrSetName = attrSet.getName();
        String attrSetDisplayName = attrSet.getDisplayName();

        List<Map<String, Object>> attrNode = getFieldsOfAttrByAttrSet(attrs); //查询当前属性集的下属属性

        Map<String, Object> result = new HashMap<>();
        //result.put("attrSeId", attrSetId);
        result.put("attrSetCode", attrSetName);
        result.put("attrSetName", attrSetDisplayName);
        result.put("attr", attrNode);

        return result;
    }

    private List<Map<String, Object>> getFieldsOfAttrByAttrSet(List<Attr> attrs) {

        List<Map<String, Object>> result = new ArrayList<>();

        List<Attr> itemAttrs = new ArrayList<>();
        List<Attr> arrayAttrs = new ArrayList<>();
        List<Attr> groupAttrs = new ArrayList<>();
        Map<Long, List<Attr>> subAttrsInGroup = new LinkedHashMap<>();

        for (Attr attr : attrs) {
            if (attr.getDisplayFlag() == 0) {
                continue;
            }

            long attrOwnerId = attr.getOwnerId();
            if (attrOwnerId != 0) {
                List<Attr> items = subAttrsInGroup.get(attrOwnerId);
                if (items == null) {
                    items = new ArrayList<>();
                }
                items.add(attr);
                subAttrsInGroup.put(attrOwnerId, items);

            } else {

                //判断attr类型
                int attrItemType = attr.getItemType();//属性的数据类别:1 单个元素 item, 2 多个元素 array     
                if (attrItemType == 1) {
                    //收集单字段
                    itemAttrs.add(attr);
                } else if (attrItemType == 2) {
                    arrayAttrs.add(attr);
                } else if (attrItemType == 3) {
                    groupAttrs.add(attr);
                }
            }
        }

        //处理 itemAttr----------------------
        for (Attr attr : itemAttrs) {
            Map<String, Object> attrNode = new HashMap<>();
            attrNode.put("attrCode", attr.getName());
            attrNode.put("attrName", attr.getDisplayName());
            attrNode.put("itemType", "item");//item表示单值，array表示多值且多字段

            result.add(attrNode);
        }

        //处理 arrayAttr-----------------------------------
        for (Attr arrayAttr : arrayAttrs) {
            long attrId = arrayAttr.getId();//属性的编号
            List<Attr> attrItems = subAttrsInGroup.get(attrId);
            if (attrItems == null || attrItems.isEmpty()) {
                continue;
            }

            //生成集合的字段列表信息
            List<Map<String, Object>> items = new ArrayList<>();
            for (Attr attr : attrItems) {
                Map<String, Object> item = new HashMap<>();
                item.put("fieldName", attr.getName());
                item.put("fieldDisplayName", attr.getDisplayName());
                item.put("displayFlag", attr.getDisplayFlag());
                item.put("fieldSpec", attr.getFieldSpec());

                items.add(item);
            }

            Map<String, Object> attrArrayNode = new HashMap<>();
            attrArrayNode.put("attrCode", arrayAttr.getName());
            attrArrayNode.put("attrName", arrayAttr.getDisplayName());
            attrArrayNode.put("itemType", "array");
            attrArrayNode.put("itemFields", items);

            result.add(attrArrayNode);
        }

        //处理 groupAttr-------------------------------------------
        for (Attr groupAttr : groupAttrs) {
            List<Attr> arrayAttrItems = subAttrsInGroup.get(groupAttr.getId());
            if (arrayAttrItems == null || arrayAttrItems.isEmpty()) {
                continue;
            }

            List<Map<String, Object>> groupItems = new ArrayList<>();
            for (Attr arrayAttr : arrayAttrItems) {
                List<Attr> attrItems = subAttrsInGroup.get(arrayAttr.getId());
                if (attrItems == null || attrItems.isEmpty()) {
                    continue;
                }

                //生成集合的字段列表信息
                List<Map<String, Object>> arrayItems = new ArrayList<>();
                for (Attr Attr : attrItems) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("fieldName", Attr.getName());
                    item.put("fieldDisplayName", Attr.getDisplayName());
                    item.put("displayFlag", Attr.getDisplayFlag());
                    item.put("fieldSpec", Attr.getFieldSpec());

                    arrayItems.add(item);
                }

                Map<String, Object> item = new HashMap<>();
                item.put("attrCode", arrayAttr.getName());
                item.put("attrName", arrayAttr.getDisplayName());
                item.put("itemType", "array");
                item.put("itemFields", arrayItems);

                groupItems.add(item);
            }

            Map<String, Object> item = new HashMap<>();
            item.put("attrCode", groupAttr.getName());
            item.put("attrName", groupAttr.getDisplayName());
            item.put("itemType", "group");
            item.put("groupItems", groupItems);

            result.add(item);
        }

        return result;
    }

}
