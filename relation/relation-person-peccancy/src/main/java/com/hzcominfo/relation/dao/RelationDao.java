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

    private Logger logger = LoggerFactory.getLogger(RelationDao.class);

    /**
     *
     * @param conn
     * @param edge
     * @return
     */
    public boolean build(Session session, Map<String, Object> param) {

        boolean ok = false;
        String query = "MERGE (p:Person {SFZH:{SFZH}}) ON CREATE SET p.XM={XM}  MERGE (p2:Person {SFZH:{CZ_SFZH}}) ON CREATE SET p2.XM={CZ_XM}  MERGE (c:Car {CPHM_HPZL:{CPHM_HPZL}}) "
                + "MERGE (p)-[r1:PECCANCY {WFSJ:{WFSJ}}]->(c) ON CREATE SET r1.created=timestamp() "
                + "MERGE (p2)-[r2:OWNER]->(c) ON CREATE SET r2.created=timestamp()";

        try {
            session.run(query, param);
            ok = true;
        } catch (Exception ex) {
            logger.error("", ex);
        }

        return ok;
    }

}
