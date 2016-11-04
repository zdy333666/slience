/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import com.hzcominfo.relation.pojo.RelationDetail;
import com.hzcominfo.relation.pojo.SameCaseRelationDetail;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cominfo4
 */
public class SameCaseRelationDetailHandler implements RelationDetailHandler {

    private final Logger logger = LoggerFactory.getLogger(SameCaseRelationDetailHandler.class);

    @Override
    public Collection<RelationDetail> getDetail(Session session, String[] pair, String type) {

        String cypher = new StringBuilder().append("MATCH (:Person {SFZH:{SFZH_1}})-[r:").append(type).append("]-(:Person {SFZH:{SFZH_2}}) WITH r.CASE_NO AS CASE_NO RETURN CASE_NO").toString();
        logger.info(cypher);

        List<Record> records = session.run(cypher, Values.parameters("SFZH_1", pair[0], "SFZH_2", pair[1])).list();

        List<RelationDetail> result = new LinkedList<>();
        for (Record record : records) {
            result.add(new SameCaseRelationDetail(record.get("CASE_NO").asString()));

        }

        return result;
    }

}
