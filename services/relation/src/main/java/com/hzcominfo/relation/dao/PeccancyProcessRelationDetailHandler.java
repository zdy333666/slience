/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import com.hzcominfo.relation.pojo.PeccancyProcessRelationDetail;
import com.hzcominfo.relation.pojo.RelationDetail;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cominfo4
 */
public class PeccancyProcessRelationDetailHandler implements RelationDetailHandler {

    private final Logger logger = LoggerFactory.getLogger(SameFlightRelationDetailHandler.class);

    @Override
    public Collection<RelationDetail> getDetail(Session session, String[] pair, String type) {

        String cypher = new StringBuilder().append("MATCH (:Person {SFZH:{SFZH_1}})-[r:").append(type).append("]-(:Person {SFZH:{SFZH_2}}) WITH r.CPHM_HPZL AS CPHM_HPZL,r.WFSJ AS WFSJ,r.detail AS detail RETURN CPHM_HPZL,WFSJ,detail").toString();
        logger.info(cypher);

        List<Record> records = session.run(cypher, Values.parameters("SFZH_1", pair[0], "SFZH_2", pair[1])).list();

        TreeMap<Long, List<RelationDetail>> orderedRecord = new TreeMap<>();
        for (Record record : records) {

            String[] cphm_hpzl = record.get("CPHM_HPZL").asString().split("@");
            long wfsj = record.get("WFSJ").asLong();
            String detail = record.get("detail").asString();

            if (!orderedRecord.containsKey(wfsj)) {
                orderedRecord.put(wfsj, new LinkedList<>());
            }

            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(wfsj));
            orderedRecord.get(wfsj).add(new PeccancyProcessRelationDetail(time, cphm_hpzl[0], cphm_hpzl[1], detail));
        }

        List<RelationDetail> result = new LinkedList<>();
        for (List<RelationDetail> value : orderedRecord.descendingMap().values()) {
            result.addAll(value);
        }

        return result;
    }

}
