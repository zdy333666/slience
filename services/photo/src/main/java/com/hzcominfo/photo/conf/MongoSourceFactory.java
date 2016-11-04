/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.photo.conf;

import com.hzcominfo.photo.filter.SimpleFilter;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zdy
 */
public class MongoSourceFactory {

    private static Map<String, MongoSource> sources = new ConcurrentHashMap<>();

    private static org.slf4j.Logger log = LoggerFactory.getLogger(SimpleFilter.class);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                MongoSourceFactory.release();
                log.info("note :  ********* all the connection to mongodb has released *********");
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
        if (source != null) {
            return source;
        }

        try {
            MongoClientURI source_uri = new MongoClientURI(uri);
            MongoClient source_client = new MongoClient(source_uri);

            source = new MongoSource();
            source.setClientURI(source_uri);
            source.setClient(source_client);

            sources.put(uri, source);
            return source;

        } catch (Exception ex) {
            Logger.getLogger(MongoSourceFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public static void release() {
        for (MongoSource source : sources.values()) {
            if (source != null && source.getClient() != null) {
                source.getClient().close();
            }
        }
    }

}
