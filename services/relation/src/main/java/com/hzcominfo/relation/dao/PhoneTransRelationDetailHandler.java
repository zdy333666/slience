/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import com.hzcominfo.relation.pojo.PhoneTransRelationDetail;
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
public class PhoneTransRelationDetailHandler implements RelationDetailHandler {

    private final Logger logger = LoggerFactory.getLogger(PhoneTransRelationDetailHandler.class);

    @Override
    public Collection<RelationDetail> getDetail(Session session, String[] pair, String type) {

        String cypher = new StringBuilder().append("MATCH (:Person {SFZH:{SFZH_1}})-[r:").append(type).append("]-(:Person {SFZH:{SFZH_2}}) WITH r.TRANS_STARTTIME AS TRANS_STARTTIME,r.detail AS detail RETURN TRANS_STARTTIME,detail").toString();
        logger.info(cypher);

        List<Record> records = session.run(cypher, Values.parameters("SFZH_1", pair[0], "SFZH_2", pair[1])).list();

        TreeMap<Long, List<RelationDetail>> orderedRecord = new TreeMap<>();
        for (Record record : records) {
            long time_l = record.get("TRANS_STARTTIME").asLong();
            String detail = record.get("detail").asString();

            if (!orderedRecord.containsKey(time_l)) {
                orderedRecord.put(time_l, new LinkedList<>());
            }

            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(time_l));
            orderedRecord.get(time_l).add(new PhoneTransRelationDetail(time, detail));
        }

        List<RelationDetail> result = new LinkedList<>();
        for (List<RelationDetail> value : orderedRecord.descendingMap().values()) {
            result.addAll(value);
        }

        return result;
    }

}
