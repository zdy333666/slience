package com.hzcominfo.aggregate.base.dao;

import com.hzcominfo.aggregate.base.cache.AggrModelCache;
import com.hzcominfo.aggregate.base.cache.AggrSetCache;
import com.hzcominfo.aggregate.base.pojo.AggrModel;
import com.hzcominfo.aggregate.base.pojo.AggrModelField;
import com.hzcominfo.aggregate.base.pojo.AggrSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zdy
 */
@Repository
public class AggregateFieldsDao {

    /**
     *
     * @param cache
     * @return
     */
    public Map<String, Object> getFields(AggrSetCache cache) {

        AggrSet aggrSet = cache.getAggrSet();
        Collection<AggrModelCache> aggrModelCaches = cache.getAggrModelCaches().values();

        List<Map<String, Object>> attrNode = getFieldsOfModels(aggrModelCaches); //查询当前属性集的下属属性

        Map<String, Object> result = new HashMap<>();
        result.put("attrSeId", aggrSet.getId());
        result.put("attrSetCode", aggrSet.getName());
        result.put("attrSetName", aggrSet.getDisplayName());
        result.put("attr", attrNode);

        return result;
    }

    /**
     *
     * @param caches
     * @return
     */
    private List<Map<String, Object>> getFieldsOfModels(Collection<AggrModelCache> caches) {

        List<Map<String, Object>> result = new ArrayList<>();

        for (AggrModelCache cache : caches) {
            AggrModel model = cache.getModel();
            List<AggrModelField> modelFields = cache.getFields();

            //生成集合的字段列表信息
            List<Map<String, Object>> items = new ArrayList<>();
            for (AggrModelField aggrModelField : modelFields) {
                Map<String, Object> item = new HashMap<>();
                item.put("fieldName", aggrModelField.getName());
                item.put("fieldDisplayName", aggrModelField.getDisplayName());
                item.put("displayFlag", aggrModelField.getDisplayEnable());
                item.put("fieldSpec", aggrModelField.getFieldSpec());

                items.add(item);
            }

            Map<String, Object> item = new HashMap<>();
            item.put("attrId", model.getId());
            item.put("attrCode", model.getName());
            item.put("attrName", model.getDisplayName());
            item.put("itemType", "array");
            item.put("itemFields", items);

            result.add(item);
        }

        return result;
    }

}
