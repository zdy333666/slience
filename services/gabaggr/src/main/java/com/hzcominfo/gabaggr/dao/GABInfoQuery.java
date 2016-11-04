/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabaggr.dao;

import com.dragonsoft.adapter.service.QueryAdapterSend;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 调用公安部信息查询接口
 *
 * @author breeze
 */
@Component
public class GABInfoQuery {
    
    private final Logger logger = LoggerFactory.getLogger(GABInfoQuery.class);

    /**
     *
     * @param queryServiceName
     * @param paramMap
     * @param userCardId
     * @param userName
     * @param userDept
     * @return
     */
    public List<Map<String, String>> queryGABInfo(String queryServiceName, //查询服务名称
            Map<String, String> paramMap, //查询参数
            String userCardId,
            String userName,
            String userDept) {
        
        List<Map<String, String>> results = new ArrayList<>();

        //信息查询条件
        StringBuilder conditionBuilder = new StringBuilder();
        for (Entry<String, String> entry : paramMap.entrySet()) {
            if (conditionBuilder.length() < 1) {
                conditionBuilder.append(entry.getKey()).append("='").append(entry.getValue()).append("'");
            } else {
                conditionBuilder.append(" AND ").append(entry.getKey()).append("='").append(entry.getValue()).append("'");
            }
        }
        
        String condition = conditionBuilder.toString();
        if (condition.trim().isEmpty()) {
            return results;
        }

        //System.out.println("query condition--->" + condition);
        //String condition = "(XM='李勇') AND (SFZH='XXXXXXXX')";
        String strReturns = null;//strReturns即为返回的结果报文，为xml字符串

        try {
            QueryAdapterSend adapter = new QueryAdapterSend();
            strReturns = adapter.sendQuery(queryServiceName, condition, userCardId, userName, userDept);

            //System.out.println("去全国人口信息库中查找返回XML内容-->" + strReturns);
            if (strReturns == null) {
                return results;
            }
            
            Document doc = DocumentHelper.parseText(strReturns); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点
            if (rootElt == null) {
                return results;
            }
            
            Element methodElt = rootElt.element("Method");// 获取Method节点
            if (methodElt == null) {
                return results;
            }
            
            Element itemsElt = methodElt.element("Items");// 获取Method节点下的子节点Items
            if (itemsElt == null) {
                return results;
            }
            
            Iterator itemIter = itemsElt.elementIterator("Item"); // 获取子节点Items下的子节点Item
            // 遍历Items节点
            if (itemIter.hasNext()) {
                Element itemElt = (Element) itemIter.next();
                
                Element valueElt = itemElt.element("Value");// 获取Item节点下的子节点Value
                if (valueElt == null) {
                    return results;
                }

                //缓存查询结果的字段列表
                List<String> fieldNames = null;
                
                Iterator rowIter = valueElt.elementIterator("Row"); // 获取子节点Value下的子节点Row
                // 遍历Row节点
                int rowIndex = 1;
                while (rowIter.hasNext()) {
                    Element rowElt = (Element) rowIter.next();
                    
                    Iterator dataIter = rowElt.elementIterator("Data"); // 获取子节点Row下的子节点Data
                    if (rowIndex == 1) {
                        
                        if (dataIter.hasNext()) {
                            Element dataElt = (Element) dataIter.next();
                            String resultCode = dataElt.getTextTrim(); // 获取查询结果的状态码
                            if (!"000".equals(resultCode)) {
                                break;
                            }
                        }
                        rowIndex++;
                        
                    } else if (rowIndex == 2) {   // 获取字段名称

                        while (dataIter.hasNext()) {
                            Element dataElt = (Element) dataIter.next();
                            String fieldName = dataElt.getTextTrim();
                            
                            if (fieldNames == null) {
                                fieldNames = new ArrayList<>();
                            }
                            fieldNames.add(fieldName);
                        }
                        rowIndex++;
                        
                    } else if (rowIndex > 2 && !(fieldNames == null || fieldNames.isEmpty())) {  // 获取字段值

                        Map<String, String> result = new HashMap<>();
                        for (String fieldName : fieldNames) {
                            if (dataIter.hasNext()) {
                                Element dataElt = (Element) dataIter.next();
                                String fieldValue = dataElt.getTextTrim();
                                result.put(fieldName, fieldValue);
                            }
                        }
                        if (result.isEmpty()) {
                            continue;
                        }
                        
                        results.add(result);
                    }
                    
                }//end---遍历Row节点

            }//end---遍历Items节点

        } catch (Exception ex) {
            logger.error("", ex);
        }
        
        return results;
    }
}
