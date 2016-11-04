/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.conf;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cominfo4
 */
public class MongoSourceFactory {

    private static final Logger logger = LoggerFactory.getLogger(MongoSourceFactory.class);

    private static volatile Map<String, MongoSource> sources = new ConcurrentHashMap<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                release();
                logger.info("note :  ********* all the connection to mongodb has released *********");
            }
        });
    }

    /**
     *
     * @param uri
     * @return
     */
    public static MongoSource getSource(String uri) {

        if (uri == null || uri.trim().isEmpty()) {
            return null;
        }

        MongoSource source = sources.get(uri);
        if (source != null && source.getClient() != null) {
            return source;
        }

        synchronized (MongoSourceFactory.class) {
            source = sources.get(uri);
            if (source != null && source.getClient() != null) {
                return source;
            }

            try {
                MongoClientURI source_uri = new MongoClientURI(uri);
                MongoClient source_client = new MongoClient(source_uri);

                source = new MongoSource();
                source.setClientURI(source_uri);
                source.setClient(source_client);

                sources.put(uri, source);
            } catch (Exception ex) {
                logger.error(null, ex);
            }
        }

        return source;
    }

    public static void release() {
        for (MongoSource source : sources.values()) {
            if (source != null && source.getClient() != null) {
                source.getClient().close();
            }
        }
    }

}
