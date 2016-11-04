/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.subject.util;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cominfo4
 */
public class TupleChecker {

    private static final Logger logger = LoggerFactory.getLogger(TupleChecker.class);
    private static final StringBuilder logBuilder = new StringBuilder();

    private static final AtomicLong counter = new AtomicLong(0L);
    private static final int sampleSize = 1_000;

    /**
     *
     * @param doc
     * @return
     */
    public static Map<String, Object> check(Map<String, Object> doc) {

        String SFZH = (String) doc.get("SFZH");
        if (SFZH == null || SFZH.trim().isEmpty()) {
            if (counter.incrementAndGet() % sampleSize == 0) {
                logger.info(logBuilder.delete(0, logBuilder.length()).append("--【SFZH:").append(SFZH).append("】-- ").append(" --- is unavailable").toString());
            }
            return null;
        }

        doc.remove("_id");
        doc.remove("MODEL_NAME");
        doc.remove("UPDATE_DATE");
        doc.remove("PRIORITY");

        return doc;
    }

    /**
     *
     * @param sfzh
     * @return
     */
    private static boolean verifySFZH(String sfzh) {
        if (sfzh == null || sfzh.trim().isEmpty() || sfzh.length() != 18) {
            return false;
        } else {
            char c = ' ';
            for (byte i = 0; i < 17; i++) {
                c = sfzh.charAt(i);
                if (c < '0' || c > '9') {
                    return false;
                }
            }
        }

        return true;
    }
}
