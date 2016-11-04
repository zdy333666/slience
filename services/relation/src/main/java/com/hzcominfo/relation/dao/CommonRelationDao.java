/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class CommonRelationDao {

    private final Logger logger = LoggerFactory.getLogger(CommonRelationDao.class);

    /**
     *
     * @param session
     * @param sfzh
     * @return
     */
    public Map<String, Object> fetchSinglePerson(Session session, String sfzh) {

        String cypher = "MATCH (p:Person {SFZH:{SFZH}}) RETURN p.XM AS XM";// UNION MATCH (p:testPerson {SFZH:{SFZH}}) RETURN p.XM AS XM";
        logger.info(cypher);

        List<Record> records = session.run(cypher, Values.parameters("SFZH", sfzh)).list();
        if (records.isEmpty()) {
            return null;
        }
        //logger.info("records.get(0).asMap() ----> " + records.get(0).asMap());
        return records.get(0).asMap();
    }

    /**
     *
     * @param sfzh
     */
    public Map<String, List<String>> fetchSinglePersonRelation(Session session, String sfzh, String[] types) {

        StringBuilder cypherBuilder = new StringBuilder().append("MATCH (:Person {SFZH:{SFZH}})-[r");
        if (types != null) {
            for (int i = 0; i < types.length; i++) {
                if (i == 0) {
                    cypherBuilder.append(":").append(types[i]);
                } else {
                    cypherBuilder.append("|:").append(types[i]);
                }
            }
        }
        cypherBuilder.append("]-(t) WITH t.SFZH AS SFZH,t.XM AS XM,type(r) AS _TYPE RETURN SFZH,XM,_TYPE");

        // 合并测试数据 --------------------------------------
//        cypherBuilder.append(" UNION ALL MATCH (:testPerson {SFZH:{SFZH}})-[r");
//        if (types != null) {
//            for (int i = 0; i < types.length; i++) {
//                if (i == 0) {
//                    cypherBuilder.append(":").append(types[i]);
//                } else {
//                    cypherBuilder.append("|:").append(types[i]);
//                }
//            }
//        }
//        cypherBuilder.append("]-(t) WITH t.SFZH AS SFZH,t.XM AS XM,type(r) AS _TYPE RETURN SFZH,XM,_TYPE");

        // 合并测试数据 --------------------------------------
        String cypher = cypherBuilder.toString();
        logger.info(cypher);

        List<Record> records = session.run(cypher, Values.parameters("SFZH", sfzh)).list();

        Map<String, List<String>> result = new HashMap<>();
        for (Record record : records) {
            String type = record.get("_TYPE").asString();
            String key = record.get("SFZH").asString();
            String xm = record.get("XM").asString();

            key += ":" + xm;

            //List<String> value = result.get(key);
            if (!result.containsKey(key)) {
                result.put(key, new LinkedList<>());
            }

            result.get(key).add(type);
            //result.put(key, value);
        }

        return result;
    }

}
