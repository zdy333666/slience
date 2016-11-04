/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import com.hzcominfo.relation.pojo.RelationDetail;
import com.hzcominfo.relation.pojo.SameStayRelationDetail;
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
public class SameStayRelationDetailHandler implements RelationDetailHandler {

    private final Logger logger = LoggerFactory.getLogger(SameStayRelationDetailHandler.class);

    @Override
    public Collection<RelationDetail> getDetail(Session session, String[] pair, String type) {

        String cypher = new StringBuilder().append("MATCH (:Person {SFZH:{SFZH_1}})-[r:").append(type).append("]-(:Person {SFZH:{SFZH_2}}) WITH r.ADDRESS AS ADDRESS,r.detail AS detail RETURN ADDRESS,detail").toString();
        logger.info(cypher);

        List<Record> records = session.run(cypher, Values.parameters("SFZH_1", pair[0], "SFZH_2", pair[1])).list();

        TreeMap<Long, List<RelationDetail>> orderedRecord = new TreeMap<>();
        for (Record record : records) {

            String address = record.get("ADDRESS").asString();
            String detail = record.get("detail").asString();

            String[] tracePair = detail.split(";");
            for (String traceItem : tracePair) {

                String[] itemPair = traceItem.split(":");
                if (!pair[0].equals(itemPair[0])) {
                    continue;
                }

                long djrq_l = Long.parseLong(itemPair[1].split("_")[0]);
                if (!orderedRecord.containsKey(djrq_l)) {
                    orderedRecord.put(djrq_l, new LinkedList<>());
                }

                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(djrq_l));
                orderedRecord.get(djrq_l).add(new SameStayRelationDetail(time, address, detail));
                break;
            }

        }

        List<RelationDetail> result = new LinkedList<>();
        for (List<RelationDetail> value : orderedRecord.descendingMap().values()) {
            result.addAll(value);
        }

        return result;
    }

}
