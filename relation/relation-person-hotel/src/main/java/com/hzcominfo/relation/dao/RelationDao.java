/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.dao;

import com.hzcominfo.relation.pojo.RelationEdge;
import com.hzcominfo.relation.pojo.SourceNode;
import com.hzcominfo.relation.pojo.TargetNode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
     * @param node
     * @return
     */
    public boolean buildSourceNode(Connection conn, SourceNode node) {

        boolean ok = false;
        String sql = "MERGE (p:Person { SFZH:{1} }) ON CREATE SET p.XM={2}";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, node.getSfzh());
            stmt.setString(2, node.getXm());

            stmt.executeUpdate();
            ok = true;

            stmt.close();
        } catch (SQLException ex) {
            logger.error(null, ex);
        }
        return ok;
    }

    /**
     *
     * @param conn
     * @param node
     * @return
     */
    public boolean buildTargetNode(Connection conn, TargetNode node) {

        boolean ok = false;
        String sql = "MERGE (h:Hotel { LGDM:{1} }) ON CREATE SET h.LGMC={2}";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, node.getLgdm());
            stmt.setString(2, node.getLgmc());

            stmt.executeUpdate();
            ok = true;

            stmt.close();
        } catch (SQLException ex) {
            logger.error(null, ex);
        }
        return ok;
    }

    /**
     *
     * @param conn
     * @param edge
     * @return
     */
    public boolean buildRelationEdge(Connection conn, RelationEdge edge) {

        boolean ok = false;
        String sql = "MATCH (p:Person { SFZH:{1} }),(h:Hotel { LGDM:{2} }) MERGE (p)-[r:CUSTOMER { RZSJ:{3},LDSJ:{4},FH:{5} }]->(h) ON CREATE SET r.created=timestamp()";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, edge.getSourceNode().getSfzh());
            stmt.setString(2, edge.getTargetNode().getLgdm());
            stmt.setLong(3, edge.getRzsj());
            stmt.setLong(4, edge.getLdsj());
            stmt.setString(5, edge.getFh());

            stmt.executeUpdate();
            ok = true;

            stmt.close();
        } catch (SQLException ex) {
            logger.error(null, ex);
        }

        return ok;
    }

    /**
     *
     * @param conn
     * @param edge
     * @return
     */
    public boolean build(Connection conn, RelationEdge edge) {

        boolean ok = false;
        String sql = "MERGE (p:Person {SFZH:{1}}) ON CREATE SET p.XM={2}  MERGE (h:Hotel {LGDM:{3}}) ON CREATE SET h.LGMC={4}  MERGE (p)-[r:CUSTOMER {RZSJ:{5},LDSJ:{6},FH:{7}}]->(h) ON CREATE SET r.created=timestamp()";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, edge.getSourceNode().getSfzh());
            stmt.setString(2, edge.getSourceNode().getXm());
            stmt.setString(3, edge.getTargetNode().getLgdm());
            stmt.setString(4, edge.getTargetNode().getLgmc());
            stmt.setLong(5, edge.getRzsj());
            stmt.setLong(6, edge.getLdsj());
            stmt.setString(7, edge.getFh());

            stmt.executeUpdate();
            ok = true;

            stmt.close();
        } catch (SQLException ex) {
            logger.error(null, ex);
        }

        return ok;
    }

}
