/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.util;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author llz
 */
public class TupleCheckerZFBA {

    private static final Logger logger = LoggerFactory.getLogger(TupleCheckerZFBA.class);
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
            String CASENO = (String) doc.get("CASENO");
            if (CASENO == null || CASENO.trim().isEmpty() || !CASENO.startsWith("A")) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【CASENO:").append(CASENO).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String RYLX = (String) doc.get("RYLX");
            if (!"违法(犯罪)嫌疑人".equals(RYLX)) {
            	if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【RYLX:").append(RYLX).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }
            
            String SFZH = (String) doc.get("IDNO");
            if (verifySFZH(SFZH) == false) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【IDNO:").append(SFZH).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String XM = (String) doc.get("SUSNAME");
            if (XM == null || !verifyChineseName(XM)) {
                XM = "*";
            }
            
            doc.clear();
            doc.put("SFZH", SFZH);
            doc.put("XM", XM);
            doc.put("CASENO", CASENO);

        } catch (Exception ex) {
            logger.error(null, ex);
            doc = null;
        }

        return doc;
    }
    
    /**
	 * 
	 * @param name
	 * @return
	 */
	private static boolean verifyChineseName(String name) {
		boolean isValid = false;
		
		Pattern numPattern = Pattern.compile("^([\\u4e00-\\u9fa5]{1,4})$");
		Matcher numMatcher = numPattern.matcher(name);
		isValid = numMatcher.matches();
		
		return isValid;
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
