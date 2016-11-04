/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.conf;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cominfo4
 */
public class Neo4jSourceFactory {

    private static final Logger logger = LoggerFactory.getLogger(Neo4jSourceFactory.class);

    private static volatile Map<String, Driver> sources = new ConcurrentHashMap<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                release();
                logger.info("note :  ********* all the connection to neo4j has released *********");
            }
        });
    }

    /**
     *
     * @param uri
     * @return
     */
    public static Driver getSource(String uri) {

        if (uri == null || uri.trim().isEmpty()) {
            return null;
        }

        Driver source = sources.get(uri);
        if (source != null) {
            return source;
        }

        synchronized (Neo4jSourceFactory.class) {
            source = sources.get(uri);
            if (source != null) {
                return source;
            }

            try {
                source = GraphDatabase.driver(uri, Config.build().withMaxSessions(50).toConfig());
                sources.put(uri, source);
            } catch (Exception ex) {
                logger.error(null, ex);
            }
        }

        return source;
    }

    /**
     *
     */
    private static void release() {
        for (Driver source : sources.values()) {
            if (source != null) {
                source.close();
            }
        }
    }

}
