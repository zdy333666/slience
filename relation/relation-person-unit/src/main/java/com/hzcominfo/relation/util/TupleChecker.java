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
 * @author llz
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

            String SFZH = (String) doc.get("ZJHM18");
            if (verifySFZH(SFZH) == false) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【SFZH:").append(SFZH).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String CBZZBH = "";
            if (doc.get("CBZZBH") == null) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【CBZZBH:").append(doc.get("CBZZBH")).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }
            CBZZBH = (int) doc.get("CBZZBH") + "";
            
            String DWMC = (String) doc.get("DWMC");
            if (DWMC == null || DWMC.trim().isEmpty()) {
            	DWMC = "*";
            }

            String KSNY = "";
            if (doc.get("KSNY") == null) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【KSNY:").append(doc.get("KSNY")).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }
            KSNY = (int) doc.get("KSNY") + "";
            
            String XM = (String) doc.get("XM");
            if (XM == null || XM.trim().isEmpty()) {
                XM = "*";
            }

            doc.clear();

            SimpleDateFormat dateFormat_d = new SimpleDateFormat("yyyyMM");

            doc.put("SFZH", SFZH);
            doc.put("XM", XM);
            doc.put("DW", CBZZBH);
            doc.put("DWMC", DWMC);
            doc.put("KSSJ", dateFormat_d.parse(KSNY).getTime());
            
            String ZZNY = "";
            if (doc.get("ZZNY") == null) {
            	doc.put("ZZSJ", System.currentTimeMillis());
            } else {
            	ZZNY = (int) doc.get("ZZNY") + "";
            	doc.put("ZZSJ", dateFormat_d.parse(ZZNY).getTime());
            }

        } catch (Exception ex) {
            logger.error("", ex);
            doc = null;
        }

        return doc;
    }

    private static int[] powers = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
	private static char[] parityBit = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };

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
}
