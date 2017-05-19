/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.handler;

import com.lyzhkj.fhl.conf.WeiXinConfig;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author breeze
 */
public class WeiXinBaseHandler {

    /**
     * 从微信服务器获得令牌(有效时间2H)
     *
     * @return
     * @throws ParseException
     * @throws IOException
     */
//    public static Map<String, Object> getAccessToken() {
//        Map<String, Object> result = null;
//        try {
//            String url = WeiXinConfig.ACCESS_TOKEN_URL.replace("APPID", WeiXinConfig.APP_ID).replace("APPSECRET", WeiXinConfig.APP_SECRET);
//            result = new RestTemplate().getForObject(url, Map.class);
//
//            System.out.println("result-->" + result);
//
////            if (result != null && result.containsKey("access_token")) {
////                token = new AccessToken();
////                token.setToken((String) result.get("access_token"));
////                token.setExpiresIn((int) result.get("expires_in"));
//            //日志里保存一下
//            //FileConser.save("getAccessToken", "(getAccessToken Sucess)access_token:" + (String) result.get("access_token"), "Auto");
//            // }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }

//    public static Map<String, String> xmlToMap(String ins) throws DocumentException {
//        Map<String, String> map = new HashMap<String, String>();
//
//        SAXReader reader = new SAXReader();
//        Document doc = reader.read(ins);
//        Element root = doc.getRootElement();
//        List<Element> list = root.elements();
//
//        for (Element e : list) {
//            map.put(e.getName(), e.getText());
//            for (Element se : (List<Element>) e.elements()) {
//                map.put(se.getName(), se.getText());
//            }
//        }
//
//        return map;
//    }
//    
//    public static Map<String, String> xmlToMap(InputStream ins) throws DocumentException {
//        Map<String, String> map = new HashMap<String, String>();
//
//        SAXReader reader = new SAXReader();
//        Document doc = reader.read(ins);
//        Element root = doc.getRootElement();
//        List<Element> list = root.elements();
//
//        for (Element e : list) {
//            map.put(e.getName(), e.getText());
//            for (Element se : (List<Element>) e.elements()) {
//                map.put(se.getName(), se.getText());
//            }
//        }
//
//        return map;
//    }

//    /**
//     * textMessageToXml
//     *
//     * @param textMessage
//     * @return
//     */
//    public static String textMessageToXml(TextMessage textMessage) {
//        XStream xstream = new XStream();
//        xstream.alias("xml", textMessage.getClass());
//        return xstream.toXML(textMessage);
//    }

}
