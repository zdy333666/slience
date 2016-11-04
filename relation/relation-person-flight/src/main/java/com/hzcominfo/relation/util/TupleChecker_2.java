/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.util;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author cominfo4
 */
public class TupleChecker_2 {

    private static final Logger logger = LoggerFactory.getLogger(TupleChecker_2.class);
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

            String FLIGHT = (String) doc.get("FLIGHT");
            if (FLIGHT == null || FLIGHT.trim().isEmpty()) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【FLIGHT:").append(FLIGHT).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String STRT = (String) doc.get("STRT_NAME");
            if (STRT == null || STRT.trim().isEmpty()) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【STRT:").append(STRT).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String DEST = (String) doc.get("DEST_NAME");
            if (DEST == null || DEST.trim().isEmpty()) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【DEST:").append(DEST).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            Date OFFDAY = (Date) doc.get("OFFDAY");
            if (OFFDAY == null) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【OFFDAY:").append(OFFDAY).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String ZJHM = (String) doc.get("FOID");
            if (verifySFZH(ZJHM) == false) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【ZJHM:").append(ZJHM).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String SEAT = (String) doc.get("SEAT");
            if (SEAT == null) {
                SEAT = "";
            }

            String CHINESE_NAME = (String) doc.get("CHINESE_NAME");
            String ENGLISH_NAME = (String) doc.get("ENGLISH_NAME");

            doc.clear();
            doc.put("SFZH", ZJHM);
            doc.put("FLIGHT_OFFDAY", FLIGHT + "@" + OFFDAY.getTime());
            doc.put("SEAT", SEAT);
            doc.put("STRT", STRT);
            doc.put("DEST", DEST);

            if (!(CHINESE_NAME == null || CHINESE_NAME.trim().isEmpty())) {
                doc.put("XM", CHINESE_NAME);
                return doc;
            } else if (!(ENGLISH_NAME == null || ENGLISH_NAME.trim().isEmpty())) {
                doc.put("XM", ENGLISH_NAME);
                return doc;
            } else {
                doc.put("XM", "*");
            }

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
        }

        int _power = 0;
        char c = ' ';
        for (int i = 0; i < 17; i++) {
            c = sfzh.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            } else {
                _power += Integer.parseInt(String.valueOf(c)) * powers[i];
            }
        }

        return sfzh.charAt(17) == parityBit[_power % 11];
    }

    private static int[] powers = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static char[] parityBit = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
}
