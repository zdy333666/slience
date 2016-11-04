/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.util;

import java.text.SimpleDateFormat;
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
public class TupleChecker {

	/**
	 *
	 * @param doc
	 * @return
	 */

	private static final Logger logger = LoggerFactory.getLogger(TupleChecker.class);
	private static final StringBuilder logBuilder = new StringBuilder();

	private static final AtomicLong counter = new AtomicLong(0L);
	private static final int sampleSize = 1_000;
	
	private static SimpleDateFormat dateFormat_day = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat dateFormat_S = new SimpleDateFormat("yyyyMMddHHmmss");
	
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

			String SERVICE_CODE = (String) doc.get("SERVICE_CODE");
			if (SERVICE_CODE == null || SERVICE_CODE.trim().isEmpty()) {
				if (counter.incrementAndGet() % sampleSize == 0) {
					logger.info(logBuilder.delete(0, logBuilder.length()).append("--【SERVICE_CODE:").append(SERVICE_CODE)
							.append("】-- ").append(" --- is unavailable").toString());
				}
				return null;
			}

			String ONLINE_TIME = (String) doc.get("ONLINE_TIME");
			if (ONLINE_TIME == null || ONLINE_TIME.length() != 14) {
				if (counter.incrementAndGet() % sampleSize == 0) {
					logger.info(logBuilder.delete(0, logBuilder.length()).append("--【ONLINE_TIME:").append(ONLINE_TIME)
							.append("】-- ").append(" --- is unavailable").toString());
				}
				return null;
			}
			
			String OFFLINE_TIME = (String) doc.get("OFFLINE_TIME");
			if (OFFLINE_TIME == null || OFFLINE_TIME.length() != 14) {
				if (counter.incrementAndGet() % sampleSize == 0) {
					logger.info(logBuilder.delete(0, logBuilder.length()).append("--【OFFLINE_TIME:").append(OFFLINE_TIME)
							.append("】-- ").append(" --- is unavailable").toString());
				}
				return null;
			}

			String ZJHM = (String) doc.get("CERTIFICATE_CODE");
			if (verifySFZH(ZJHM) == false) {
				if (counter.incrementAndGet() % sampleSize == 0) {
					logger.info(logBuilder.delete(0, logBuilder.length()).append("--【ZJHM:").append(ZJHM).append("】-- ")
							.append(" --- is unavailable").toString());
				}
				return null;
			}

			String COMPUTER_NO = (String) doc.get("COMPUTER_NO");
			if (COMPUTER_NO == null || COMPUTER_NO.trim().isEmpty()) {
				if (counter.incrementAndGet() % sampleSize == 0) {
					logger.info(logBuilder.delete(0, logBuilder.length()).append("--【COMPUTER_NO:").append(COMPUTER_NO).append("】-- ")
							.append(" --- is unavailable").toString());
				}
				COMPUTER_NO = "*";
			}
			
			String SERVICE_NAME = (String) doc.get("SERVICE_NAME");
			if (SERVICE_NAME == null || SERVICE_NAME.trim().isEmpty()) {
				if (counter.incrementAndGet() % sampleSize == 0) {
					logger.info(logBuilder.delete(0, logBuilder.length()).append("--【SERVICE_NAME:").append(SERVICE_NAME).append("】-- ")
							.append(" --- is unavailable").toString());
				}
				SERVICE_NAME = "*";
			}

			String USER_NAME = (String) doc.get("USER_NAME");
			if (USER_NAME == null || USER_NAME.trim().isEmpty() || !verifyChineseName(USER_NAME)) {
				if (counter.incrementAndGet() % sampleSize == 0) {
					logger.info(logBuilder.delete(0, logBuilder.length()).append("--【USER_NAME:").append(USER_NAME).append("】-- ")
							.append(" --- is unavailable").toString());
				}
				USER_NAME = "*";
			}
			
			Date day = dateFormat_day.parse((String) ONLINE_TIME.subSequence(0, ONLINE_TIME.length() - 6));
			Date onlineTime = dateFormat_S.parse(ONLINE_TIME);
			Date offlineTime = dateFormat_S.parse(OFFLINE_TIME);
			
			doc.clear();
			doc.put("SFZH", ZJHM);
			doc.put("WBDM_SXRQ", SERVICE_CODE + "@" + day.getTime());
			doc.put("ONLINE_TIME", onlineTime.getTime());
			doc.put("OFFLINE_TIME", offlineTime.getTime());
			doc.put("XM", USER_NAME);
			doc.put("WBMC", SERVICE_NAME);
			doc.put("SEAT", COMPUTER_NO);

		} catch (Exception ex) {
			logger.error("", ex);
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

	private static int[] powers = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
	private static char[] parityBit = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
}
