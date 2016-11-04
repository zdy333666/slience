/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.util;

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

        if (doc == null) {
            return doc;
        }

        try {
            String JLBZ = (String) doc.get("JLBZ");
            if (JLBZ == null || (!"1".equals(JLBZ.trim()))) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【JLBZ:").append(JLBZ).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String RYZT = (String) doc.get("RYZT");
            if (RYZT == null || (!"0".equals(RYZT.trim()))) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【RYZT:").append(RYZT).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String XXJB = (String) doc.get("XXJB");
            if (XXJB == null || (!"5".equals(XXJB.trim()))) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【XXJB:").append(XXJB).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String CXBZ = (String) doc.get("CXBZ");
            if (CXBZ == null || (!"0".equals(CXBZ.trim()))) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【CXBZ:").append(CXBZ).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String GMSFHM = (String) doc.get("GMSFHM");
            if (verifySFZH(GMSFHM) == false) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【GMSFHM:").append(GMSFHM).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String HHID = (String) doc.get("HHID");
            if (HHID == null || HHID.trim().isEmpty()) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【HHID:").append(HHID).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String XM = (String) doc.get("XM");
            if (XM == null || XM.trim().isEmpty()) {
                XM = "*";
            }

            doc.clear();
            doc.put("SFZH", GMSFHM);
            doc.put("XM", XM);
            doc.put("HHID", HHID);

        } catch (Exception ex) {
            logger.error(null, ex);
            doc = null;
        }

        return doc;
    }

    /**
     *
     * @param sfzh
     * @return
     */
    private static boolean verifySFZH(String sfzh) {
        if (sfzh == null || sfzh.trim().isEmpty() || (sfzh.length() != 18) || sfzh.startsWith("00")) {
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
