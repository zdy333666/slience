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
    public boolean build(Session session, Map<String, Object> param) {

        boolean ok = false;
        String cypher = "MERGE (p:Person {SFZH:{SFZH}}) ON CREATE SET p.XM={XM}  MERGE (h:HotelRoom {LGDM_FH:{LGDM_FH}}) ON CREATE SET h.LGMC={LGMC}  MERGE (p)-[r:CUSTOMER {RZSJ:{RZSJ},LDSJ:{LDSJ}}]->(h) ON CREATE SET r.created=timestamp()";

        try {
            session.run(cypher, param);
            ok = true;

        } catch (Exception ex) {
            logger.error(null, ex);
        }

        return ok;
    }

}
