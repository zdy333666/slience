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

            String ZJLX = (String) doc.get("ZJLX");
            if (!"11".equals(ZJLX)) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【ZJLX:").append(ZJLX).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String ZJHM = (String) doc.get("ZJHM");
            if (verifySFZH(ZJHM) == false) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【ZJHM:").append(ZJHM).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String JSBH = (String) doc.get("JSBH");
            if (JSBH == null || JSBH.trim().isEmpty() || JSBH.length() != 9) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【JSBH:").append(JSBH).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String JSH = (String) doc.get("JSH");
            if (JSH == null || JSH.trim().isEmpty()) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【JSH:").append(JSH).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String RSRQ = (String) doc.get("RSRQ");
            if (RSRQ == null || RSRQ.trim().isEmpty()) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【RSRQ:").append(RSRQ).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String OUTRQ = (String) doc.get("OUTRQ");
            if (OUTRQ == null || OUTRQ.trim().isEmpty()) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【OUTRQ:").append(OUTRQ).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String JSMC = (String) doc.get("JSMC");
            if (JSMC == null || JSMC.trim().isEmpty()) {
                JSMC = "*";
            }

            String XM = (String) doc.get("XM");
            if (XM == null || XM.trim().isEmpty()) {
                XM = "*";
            }

            doc.clear();

            SimpleDateFormat dateFormat_d = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat dateFormat_s = new SimpleDateFormat("yyyyMMddHHmmss");

            doc.put("SFZH", ZJHM);
            doc.put("XM", XM);
            doc.put("JSBH_JSH", JSBH + "@" + JSH);
            doc.put("JSMC", JSMC);
            doc.put("RSRQ", RSRQ.length() == 8 ? dateFormat_d.parse(RSRQ).getTime() : dateFormat_s.parse(RSRQ).getTime());
            doc.put("CSRQ", OUTRQ.length() == 8 ? dateFormat_d.parse(OUTRQ).getTime() : dateFormat_s.parse(OUTRQ).getTime());

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
