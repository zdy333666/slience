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

    private static final AtomicLong counter = new AtomicLong();
    private static final int sampleSize = 1_000;

    private static final SimpleDateFormat dateFormat_s = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     *
     * @param doc
     * @return
     */
    public static Map<String, Object> check(Map<String, Object> doc) {

        if (doc == null) {
            return doc;
        }

        StringBuilder logBuilder = new StringBuilder();

        try {
            String ZJLX = (String) doc.get("ZJLX");
            if (!"11".equals(ZJLX)) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【ZJLX:").append(ZJLX).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String LGDM = (String) doc.get("LGDM");
            if (verifyLGDM(LGDM) == false) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【LGDM:").append(LGDM).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String LGMC = (String) doc.get("LGDM_LGMC_FORMAT");
            if (LGMC == null || LGMC.trim().isEmpty()) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【LGMC:").append(LGMC).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String RZSJ = (String) doc.get("RZSJ");
            if (RZSJ == null || RZSJ.trim().isEmpty()) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【RZSJ:").append(RZSJ).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String LDSJ = (String) doc.get("LDSJ");
            if (LDSJ == null || LDSJ.trim().isEmpty()) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【LDSJ:").append(LDSJ).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String FH = (String) doc.get("FH");
            if (FH == null || FH.trim().isEmpty() || FH.trim().equals("null")) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【FH:").append(FH).append("】-- ").append(" --- is unavailable").toString());
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

            String XM = (String) doc.get("XM");
            if (XM == null || XM.trim().isEmpty()) {
                XM = "*";
            }

            doc.clear();

            doc.put("SFZH", ZJHM);
            doc.put("XM", XM);
            doc.put("RZSJ", dateFormat_s.parse(RZSJ).getTime());
            doc.put("LDSJ", dateFormat_s.parse(LDSJ).getTime());
            doc.put("LGDM", LGDM);
            doc.put("LGMC", LGMC);
            doc.put("FH", FH);

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

    /**
     *
     * @param lgdm
     * @return
     */
    private static boolean verifyLGDM(String lgdm) {
        if (lgdm == null || lgdm.length() != 10 || lgdm.trim().isEmpty()) {
            return false;
        } else {
            char c = ' ';
            for (byte i = 0; i < lgdm.length(); i++) {
                c = lgdm.charAt(i);
                if (c < '0' || c > '9') {
                    return false;
                }
            }
        }

        return true;
    }

}
