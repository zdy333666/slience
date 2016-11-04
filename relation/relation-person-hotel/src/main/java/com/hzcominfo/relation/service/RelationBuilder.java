/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.service;

import com.hzcominfo.relation.conf.RelationConfiguration;
import com.hzcominfo.relation.dao.RelationDao;
import com.hzcominfo.relation.pojo.RelationEdge;
import com.hzcominfo.relation.pojo.SourceNode;
import com.hzcominfo.relation.pojo.TargetNode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
//@Component
public class RelationBuilder {

    private final Logger logger = LoggerFactory.getLogger(RelationBuilder.class);
    private StringBuilder logBuilder = new StringBuilder();

    public final BlockingQueue queue = new LinkedBlockingQueue(10_000);
    private Connection conn;
    private AtomicLong counter = new AtomicLong();
    private long startTime;

    //@Autowired
    private RelationDao relationDao;

    public RelationBuilder() {
        try {
            // Make sure Neo4j Driver is registered
            Class.forName("org.neo4j.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            logger.error(null, ex);
            System.exit(0);
        }
    }

    //@Scheduled(fixedRate = 2000)
    public void service() {

        if (queue.isEmpty()) {
            logger.info(logBuilder.delete(0, logBuilder.length()).append(" ------------ queue is empty ..... ").toString());
            return;
        }

        try {
            if (conn == null) {
                conn = DriverManager.getConnection(RelationConfiguration.NEO4J_URI);
                conn.setAutoCommit(false);
            }

            Map<String, Object> tuple = (Map<String, Object>) queue.poll();
            while (tuple != null) {

                SourceNode sourceNode = new SourceNode((String) tuple.get("sfzh"), (String) tuple.get("xm"));
                TargetNode targetNode = new TargetNode((String) tuple.get("lgdm"), (String) tuple.get("lgmc"));
                RelationEdge relationEdge = new RelationEdge(((Date) tuple.get("rzsj")).getTime(), ((Date) tuple.get("ldsj")).getTime(), (String) tuple.get("fh"), sourceNode, targetNode);

                startTime = System.currentTimeMillis();
//                if (relationDao.buildSourceNode(conn, sourceNode) && relationDao.buildTargetNode(conn, targetNode) && relationDao.buildRelationEdge(conn, relationEdge)) {
//                    conn.commit();
//
//                    if (counter.incrementAndGet() % 500 == 0) {
//                        logger.info(logBuilder.delete(0, logBuilder.length()).append(counter.get()).append(" -- success --> ").append(tuple).append(" -- spend time -- ").append(System.currentTimeMillis() - startTime).append(" ms").toString());
//                    }
//                } else {
//                    queue.put(tuple);
//                    conn.rollback();
//                }

                tuple = (Map<String, Object>) queue.poll();
            }

        } catch (Exception ex) {
            logger.error(null, ex);
        }

    }

    public boolean offer(Map<String, Object> tuple) {
        return queue.offer(tuple);
    }

    public boolean isOk() {
        return queue.isEmpty();
    }

}
