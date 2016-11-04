/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import java.util.Map;
import org.neo4j.driver.v1.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class RelationDao {

    private final Logger logger = LoggerFactory.getLogger(RelationDao.class);

    /**
     *
     * @param conn
     * @param edge
     * @return
     */
    public boolean build(Session session, Map<String, Object> tuple) {

        boolean ok = false;
        String cypher = "MERGE (p:Person {SFZH:{SFZH}}) ON CREATE SET p.XM={XM}  MERGE (h:Household {HHID:{HHID}})  MERGE (p)-[r:REGISTER]->(h) ON CREATE SET r.created=timestamp()";

//        try (Transaction tx = session.beginTransaction()) {
//            StatementResult rs =tx.run(sql, Values.parameters("name", "Arthur"));
//            tx.success();
//        }
        try {
            //StatementResult result = 
            session.run(cypher, tuple);
            ok = true;
        } catch (Exception ex) {
            logger.error(null, ex);
        }

        return ok;
    }

}
