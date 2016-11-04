/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabaggr.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 针对 公安部信息查询接口-XML格式输出数据 的解析器
 *
 * @author zdy
 */
public class XMLResultParser {

    /**
     * 解析XML文档中的单条有效记录
     *
     * @param source
     * @return
     * @throws DocumentException
     */
    public static Map<String, String> parseRow(String source) throws DocumentException {

        Document doc = DocumentHelper.parseText(source); // 将字符串转为XML
        if (doc == null) {
            return null;
        }

        Iterator rowIter = getRowIterator(doc); // 获取子节点Value下的子节点Row
        if (rowIter == null) {
            return null;
        }

        Map<String, String> result = null;
        List<String> fields = null; // 缓存查询结果的字段列表
        int rowIndex = 1;

        // 遍历Row节点
        while (rowIter.hasNext()) {
            Element rowElt = (Element) rowIter.next();
            if (rowElt == null) {
                continue;
            }

            Iterator dataIter = rowElt.elementIterator("Data"); // 获取子节点Row下的子节点Data
            if (rowIndex == 1) {
                if (dataIter.hasNext()) {
                    Element dataElt = (Element) dataIter.next();
                    if (dataElt == null) {
                        break;
                    }

                    String resultCode = dataElt.getTextTrim(); // 获取查询结果的状态码
                    if (!"000".equals(resultCode)) {
                        break;
                    }
                }
            } else if (rowIndex == 2) {   // 获取字段名称
                while (dataIter.hasNext()) {
                    if (fields == null) {
                        fields = new ArrayList<>();
                    }
                    Element dataElt = (Element) dataIter.next();
                    fields.add(dataElt == null ? null : dataElt.getTextTrim());
                }

                if (fields == null) {
                    break;
                }
            } else if (rowIndex > 2) {  // 获取字段值
                for (String fieldName : fields) {
                    if (!dataIter.hasNext()) {
                        break;
                    }
                    if (result == null) {
                        result = new HashMap<>();
                    }
                    Element dataElt = (Element) dataIter.next();
                    result.put(fieldName, dataElt == null ? null : dataElt.getTextTrim());
                }

                if (result != null) {
                    break;
                }
            }

            rowIndex++;
        }//end---遍历Row节点

        return result;
    }

    /**
     * 解析XML文档中的多条有效记录
     *
     * @param source
     * @return
     * @throws DocumentException
     */
    public static List<Map<String, String>> parseRows(String source) throws DocumentException {

        Document doc = DocumentHelper.parseText(source); // 将字符串转为XML
        if (doc == null) {
            return null;
        }

        Iterator rowIter = getRowIterator(doc); // 获取子节点Value下的子节点Row
        if (rowIter == null) {
            return null;
        }

        List<Map<String, String>> result = null;
        List<String> fields = null; // 缓存查询结果的字段列表
        int rowIndex = 1;

        // 遍历Row节点
        while (rowIter.hasNext()) {
            Element rowElt = (Element) rowIter.next();
            if (rowElt == null) {
                continue;
            }

            Iterator dataIter = rowElt.elementIterator("Data"); // 获取子节点Row下的子节点Data
            if (rowIndex == 1) {
                if (dataIter.hasNext()) {
                    Element dataElt = (Element) dataIter.next();
                    if (dataElt == null) {
                        break;
                    }

                    String resultCode = dataElt.getTextTrim(); // 获取查询结果的状态码
                    if (!"000".equals(resultCode)) {
                        break;
                    }
                }
            } else if (rowIndex == 2) {   // 获取字段名称
                while (dataIter.hasNext()) {
                    if (fields == null) {
                        fields = new ArrayList<>();
                    }
                    Element dataElt = (Element) dataIter.next();
                    fields.add(dataElt == null ? null : dataElt.getTextTrim());
                }

                if (fields == null) {
                    break;
                }
            } else if (rowIndex > 2) {  // 获取字段值
                Map<String, String> resultRow = null;
                for (String fieldName : fields) {
                    if (!dataIter.hasNext()) {
                        break;
                    }
                    if (resultRow == null) {
                        resultRow = new HashMap<>();
                    }
                    Element dataElt = (Element) dataIter.next();
                    resultRow.put(fieldName, dataElt == null ? null : dataElt.getTextTrim());
                }

                if (resultRow == null) {
                    continue;
                }

                if (result == null) {
                    result = new ArrayList<>();
                }
                result.add(resultRow);
            }

            rowIndex++;
        } //end---遍历Row节点

        return result;
    }

    /**
     * 获取XML文档中有效的 Row 子节点
     *
     * @param doc
     * @return
     */
    private static Iterator getRowIterator(Document doc) {

        Element rootElt = doc.getRootElement(); // 获取根节点
        if (rootElt == null) {
            return null;
        }

        Element methodElt = rootElt.element("Method");// 获取Method节点
        if (methodElt == null) {
            return null;
        }

        Element itemsElt = methodElt.element("Items");// 获取Method节点下的子节点Items
        if (itemsElt == null) {
            return null;
        }

        Iterator itemIter = itemsElt.elementIterator("Item"); // 获取子节点Items下的子节点Item
        if (itemIter == null) {
            return null;
        }
        // 遍历Items节点
        if (!itemIter.hasNext()) {
            return null;
        }

        Element itemElt = (Element) itemIter.next();
        if (itemElt == null) {
            return null;
        }

        Element valueElt = itemElt.element("Value");// 获取Item节点下的子节点Value
        if (valueElt == null) {
            return null;
        }

        return valueElt.elementIterator("Row"); // 获取子节点Value下的子节点Row
    }

}
