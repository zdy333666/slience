/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.util;

import com.hzcominfo.relation.conf.MongoSource;
import com.hzcominfo.relation.conf.MongoSourceFactory;
import com.hzcominfo.relation.conf.RelationConfiguration;
import com.hzcominfo.relation.dao.LinkDao;
import com.mongodb.DBCollection;
import java.util.Date;
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

            String JSZH = (String) doc.get("JSZH");
            if (verifySFZH(JSZH) == false) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【JSZH:").append(JSZH).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String HPZL = (String) doc.get("HPZL");
            if (HPZL == null || HPZL.trim().isEmpty()) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【HPZL:").append(HPZL).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String HPHM = (String) doc.get("HPHM");
            if (HPHM == null || HPHM.trim().isEmpty() || HPHM.length() < 6) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【HPHM:").append(HPHM).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            //---------------------------------------
            Map<String, Object> carOwner = null;
            try {
                MongoSource source = MongoSourceFactory.getSource(RelationConfiguration.mongo_uri_hzga);
                DBCollection coll = source.getClient().getDB(source.getClientURI().getDatabase()).getCollection(RelationConfiguration.link_table);

                carOwner = LinkDao.getCarOwner(coll, HPHM, HPZL);
            } catch (Exception ex) {
                logger.error("query link_table failure ... ", ex);

                //****************************
                System.exit(0);
            }

            if (carOwner == null) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【carOwner:").append(carOwner).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String carOwnerSFZH = (String) carOwner.get("SFZMHM");
            if (verifySFZH(carOwnerSFZH) == false || JSZH.equals(carOwnerSFZH)) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【carOwnerSFZH:").append(carOwnerSFZH).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String carOwnerXM = (String) carOwner.get("SYR");
            if (carOwnerXM == null || carOwnerXM.trim().isEmpty()) {
                carOwnerXM = "*";
            }
            //---------------------------------------

            Date WFSJ = (Date) doc.get("WFSJ");
            if (WFSJ == null) {
                if (counter.incrementAndGet() % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【WFSJ:").append(WFSJ).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String XM = (String) doc.get("DSR");
            if (XM == null || XM.trim().isEmpty()) {
                XM = "*";
            }

            doc.clear();

            doc.put("SFZH", JSZH);
            doc.put("XM", XM);
            doc.put("CPHM_HPZL", HPHM + "@" + HPZL);
            doc.put("WFSJ", WFSJ.getTime());
            doc.put("CZ_SFZH", carOwnerSFZH);
            doc.put("CZ_XM", carOwnerXM);

        } catch (Exception ex) {
            logger.error("", ex);
            doc = null;
        }

        return doc;
    }

    /**
     *
     * @param sfzh
     * @return
     */
    public static boolean verifySFZH(String sfzh) {
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
