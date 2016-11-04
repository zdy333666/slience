/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import com.hzcominfo.relation.pojo.RelationDetail;
import com.hzcominfo.relation.pojo.SameHotelRoomRelationDetail;
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
public class SameHotelRoomRelationDetailHandler implements RelationDetailHandler {

    private final Logger logger = LoggerFactory.getLogger(SameHotelRoomRelationDetailHandler.class);

    @Override
    public Collection<RelationDetail> getDetail(Session session, String[] pair, String type) {

        String cypher = new StringBuilder().append("MATCH (:Person {SFZH:{SFZH_1}})-[r:").append(type).append("]-(:Person {SFZH:{SFZH_2}}) WITH split(r.LGDM_FH,\"@\")[1] AS FH,r.LGMC AS LGMC,r.trace AS trace RETURN FH,LGMC,trace").toString();
        logger.info(cypher);

        List<Record> records = session.run(cypher, Values.parameters("SFZH_1", pair[0], "SFZH_2", pair[1])).list();

        TreeMap<Long, List<RelationDetail>> orderedRecord = new TreeMap<>();
        for (Record record : records) {
            String fh = record.get("FH").asString();
            String lgdm = record.get("LGMC").asString();
            String trace = record.get("trace").asString();

            String[] tracePair = trace.split(";");
            for (String traceItem : tracePair) {

                String[] itemPair = traceItem.split(":");
                if (!pair[0].equals(itemPair[0])) {
                    continue;
                }

                long rzsj_l = Long.parseLong(itemPair[1].split("_")[0]);
                if (!orderedRecord.containsKey(rzsj_l)) {
                    orderedRecord.put(rzsj_l, new LinkedList<>());
                }

                String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(rzsj_l));
                orderedRecord.get(rzsj_l).add(new SameHotelRoomRelationDetail(time, lgdm, fh));
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
