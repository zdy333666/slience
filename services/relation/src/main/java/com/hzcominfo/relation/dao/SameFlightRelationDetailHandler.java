/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import com.hzcominfo.relation.conf.MongoSource;
import com.hzcominfo.relation.conf.MongoSourceFactory;
import com.hzcominfo.relation.conf.RelationConfiguration;
import com.hzcominfo.relation.pojo.RelationDetail;
import com.hzcominfo.relation.pojo.SameFlightRelationDetail;
import com.mongodb.DBCollection;
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
public class SameFlightRelationDetailHandler implements RelationDetailHandler {

    private final Logger logger = LoggerFactory.getLogger(SameFlightRelationDetailHandler.class);

    @Override
    public Collection<RelationDetail> getDetail(Session session, String[] pair, String type) {

        String cypher = new StringBuilder().append("MATCH (:Person {SFZH:{SFZH_1}})-[r:").append(type).append("]-(:Person {SFZH:{SFZH_2}}) WITH r.FLIGHT_OFFDAY AS FLIGHT_OFFDAY,r.STRT AS STRT,r.DEST AS DEST RETURN FLIGHT_OFFDAY,STRT,DEST").toString();
        logger.info(cypher);

        List<Record> records = session.run(cypher, Values.parameters("SFZH_1", pair[0], "SFZH_2", pair[1])).list();

        MongoSource rtdb_source = MongoSourceFactory.getSource(RelationConfiguration.mongo_uri_rtdb);
        DBCollection airport_translate_coll = rtdb_source.getClient().getDB(rtdb_source.getClientURI().getDatabase()).getCollection(RelationConfiguration.airport_translate_table);
        AirPortCodeTranslater airPortCodeTranslater = new AirPortCodeTranslater();

        TreeMap<Long, List<RelationDetail>> orderedRecord = new TreeMap<>();
        for (Record record : records) {
            String[] flight_offday = record.get("FLIGHT_OFFDAY").asString().split("@");

            String flight = flight_offday[0];
            long offday = Long.parseLong(flight_offday[1]);
            String strt = record.get("STRT").asString();
            String dest = record.get("DEST").asString();

            strt = airPortCodeTranslater.translate(airport_translate_coll, strt);
            dest = airPortCodeTranslater.translate(airport_translate_coll, dest);

            if (!orderedRecord.containsKey(offday)) {
                orderedRecord.put(offday, new LinkedList<>());
            }

            String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(offday));
            orderedRecord.get(offday).add(new SameFlightRelationDetail(time, strt, dest, flight));
        }

        List<RelationDetail> result = new LinkedList<>();
        for (List<RelationDetail> value : orderedRecord.descendingMap().values()) {
            result.addAll(value);
        }

        return result;
    }

}
