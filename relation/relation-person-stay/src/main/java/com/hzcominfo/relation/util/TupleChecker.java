/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.util;

import java.text.SimpleDateFormat;
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

            String SFZH = (String) doc.get("SFZH");
            if (verifySFZH(SFZH) == false) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【SFZH:").append(SFZH).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String DJRQ = (String) doc.get("DJRQ");
            if (DJRQ == null || DJRQ.trim().isEmpty()) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【DJRQ:").append(DJRQ).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String DQRQ = (String) doc.get("DQRQ");
            if (DQRQ == null || DQRQ.trim().isEmpty()) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【DQRQ:").append(DQRQ).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            StringBuilder ADDRESSBuilder = null;

            String QX_FORMAT = (String) doc.get("QX_FORMAT");
            if (!(QX_FORMAT == null || QX_FORMAT.trim().isEmpty())) {
                ADDRESSBuilder = (ADDRESSBuilder == null) ? new StringBuilder() : ADDRESSBuilder;
                ADDRESSBuilder.append(QX_FORMAT);
            }

            String JXDM_FORMAT = (String) doc.get("JXDM_FORMAT");
            if (!(JXDM_FORMAT == null || JXDM_FORMAT.trim().isEmpty())) {
                ADDRESSBuilder = (ADDRESSBuilder == null) ? new StringBuilder() : ADDRESSBuilder;
                ADDRESSBuilder.append(JXDM_FORMAT);
            }

            String ZZDZ = (String) doc.get("ZZDZ");
            if (!(ZZDZ == null || ZZDZ.trim().isEmpty())) {
                ADDRESSBuilder = (ADDRESSBuilder == null) ? new StringBuilder() : ADDRESSBuilder;
                ADDRESSBuilder.append(ZZDZ);
            }

            if (ADDRESSBuilder == null || "null".equals(ADDRESSBuilder.toString())) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【ADDRESS:").append(ADDRESSBuilder).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String XM = (String) doc.get("XM");
            if (XM == null || XM.trim().isEmpty()) {
                XM = "*";
            }

            doc.clear();

            SimpleDateFormat dateFormat_d = new SimpleDateFormat("yyyyMMdd");

            doc.put("SFZH", SFZH);
            doc.put("XM", XM);
            doc.put("DJRQ", dateFormat_d.parse(DJRQ).getTime());
            doc.put("DQRQ", dateFormat_d.parse(DQRQ).getTime());
            doc.put("ADDRESS", ADDRESSBuilder.toString());

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
