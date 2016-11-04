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
 * @author llz
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
        String query = "MERGE (p:testPerson {SFZH:{SFZH}}) ON CREATE SET p.XM={XM} "
        		+ " MERGE (u:testUnit {DW:{DW}}) ON CREATE SET u.DWMC={DWMC} "
        		+ " MERGE (p)-[r:MEMBER {KSSJ:{KSSJ},ZZSJ:{ZZSJ}}]->(u) "
        		+ " ON CREATE SET r.created=timestamp()";

        try {
            session.run(query, param);
            ok = true;
        } catch (Exception ex) {
            logger.error("", ex);
        }

        return ok;
    }

}
