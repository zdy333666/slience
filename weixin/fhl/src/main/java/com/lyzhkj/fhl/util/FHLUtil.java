/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 *
 * @author breeze
 */
public class FHLUtil {
    
    public static Map<String, String> xmlToMap(InputStream ins) throws DocumentException {
        Map<String, String> map = new HashMap<String, String>();

        SAXReader reader = new SAXReader();
        Document doc = reader.read(ins);
        Element root = doc.getRootElement();
        List<Element> list = root.elements();

        for (Element e : list) {
            map.put(e.getName(), e.getText());
            for (Element se : (List<Element>) e.elements()) {
                map.put(se.getName(), se.getText());
            }
        }

        return map;
    }
    
}
