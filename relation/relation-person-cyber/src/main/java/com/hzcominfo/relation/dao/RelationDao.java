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
        String query = "MERGE (p:Person {SFZH:{SFZH}}) ON CREATE SET p.XM={XM}  "
        		+ "MERGE (c:Cybercafe {WBDM_SXRQ:{WBDM_SXRQ}}) ON CREATE SET c.WBMC={WBMC}  "
        		+ "MERGE (p)-[r:CUSTOMER {ONLINE_TIME:{ONLINE_TIME},OFFLINE_TIME:{OFFLINE_TIME},SEAT:{SEAT}}]->(c) "
        		+ "ON CREATE SET r.created=timestamp()";

        try {
            session.run(query, param);
            ok = true;
        } catch (Exception ex) {
            logger.error("", ex);
        }

        return ok;
    }

}
