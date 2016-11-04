/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.relation.service;

import com.hzcominfo.relation.conf.MongoSource;
import com.hzcominfo.relation.conf.MongoSourceFactory;
import com.hzcominfo.relation.conf.RelationConfiguration;
import com.hzcominfo.relation.dao.SavePointDao;
import com.hzcominfo.relation.pojo.SavePoint;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class FullSourceReader {

    private int index;
    private boolean complete;
    private int sampleSize = 500;

    private String table;
    private DBObject query = new BasicDBObject();
    private DBObject fields = new BasicDBObject();
    private DBObject sort = new BasicDBObject();
    private String sortField = "ETL_TIMESTAMP";
    private int skip = 0;
    private int limit = 5_000;

    private SavePoint savePoint = new SavePoint();
    private Date savePointValue;

    private MongoSource source = null;
    private List<DBObject> docs = null;
    private Map<String, Object> tuple = null;
    private int size;

    private boolean ok;
    private Date tempSavePoint;

    private SimpleDateFormat dateFormat_s = new SimpleDateFormat("yyyyMMddHHmmss");
    private SimpleDateFormat dateFormat_S = new SimpleDateFormat("yyyyMMddHHmmssS");

    @Autowired
    private AsynRelationBuilder relationBuilder;

    @Autowired
    private SavePointDao savePointDao;

    private Logger logger = LoggerFactory.getLogger(FullSourceReader.class);
    private StringBuilder logBuilder = new StringBuilder();

    public FullSourceReader() {
        fields.put("_id", 0);
        fields.put("ZJHM", 1);
        fields.put("XM", 1);
        fields.put("ZJLX", 1);
        fields.put("RZSJ", 1);
        fields.put("LDSJ", 1);
        fields.put("FH", 1);
        fields.put("LGDM", 1);
        fields.put("LGDM_LGMC_FORMAT", 1);
        fields.put(sortField, 1);

        sort.put(sortField, 1);

        table = RelationConfiguration.GAZHK_LGY_NB;
        savePoint.setType(RelationConfiguration.RELATION_TYPE);
        savePoint.setTable(table);
    }

    /**
     *
     */
    @Scheduled(fixedRate = 3000)
    public void read() {

        if (complete) {
            logger.info(logBuilder.delete(0, logBuilder.length()).append(" ------------ full source process is completed").toString());
            try {
                Thread.sleep(10 * 60 * 1000);
            } catch (InterruptedException ex) {
                logger.error(null, ex);
            }
            complete = false;
            return;
        }

        if (relationBuilder.isOk()) {

            if (savePointValue != null) {
                savePoint.setValue(dateFormat_S.format(savePointValue));
                savePoint.setSkip(skip);
                savePointDao.set(savePoint);
            } else {
                savePoint = savePointDao.get(savePoint);

                String value = savePoint.getValue();
                if (!(value == null || value.trim().isEmpty())) {
                    try {
                        savePointValue = dateFormat_S.parse(value);
                        skip = savePoint.getSkip();
                    } catch (ParseException ex) {
                        logger.error(null, ex);
                    }
                }
            }

            if (savePointValue != null) {
                query.put(sortField, new BasicDBObject().append("$gte", savePointValue));
            } else {
                query.put(sortField, new BasicDBObject().append("$ne", savePointValue));
            }

            logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- table -->").append(table).toString());
            logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- query -->").append(query).toString());
            logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- fields -->").append(fields).toString());
            logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- sort -->").append(sort).toString());
            logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- skip -->").append(skip).toString());
            logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- limit -->").append(limit).toString());

            source = MongoSourceFactory.getSource(RelationConfiguration.MONGO_URI_HZGA);

            try {
                docs = source.getClient().getDB(source.getClientURI().getDatabase()).getCollection(table).find(query, fields).sort(sort).skip(skip).limit(limit).toArray();//.addOption(Bytes.QUERYOPTION_NOTIMEOUT);

                size = docs.size();
                while (!docs.isEmpty()) {
                    tuple = docs.get(0).toMap();
                    tempSavePoint = (Date) tuple.get(sortField);

                    ok = false;
                    tuple = check(tuple);
                    if (tuple == null) {
                        docs.remove(0);
                        ok = true;
                    } else if (relationBuilder.offer(tuple)) {
                        docs.remove(0);
                        ok = true;
                    }
                    tuple = null;

                    if (ok) {
                        index++;
                        if (tempSavePoint == null || tempSavePoint.equals(savePointValue)) {
                            skip++;
                        } else {
                            skip = 0;
                            savePointValue = tempSavePoint;
                        }
                    } else {
                        Thread.sleep(1000);
                    }
                }

                docs = null;
                logger.info(logBuilder.delete(0, logBuilder.length()).append(" -- full source spout to --> ").append(index).toString());

                if (size < limit) {
                    complete = true;
                }

            } catch (InterruptedException ex) {
                logger.error(null, ex);
            }

        }
//        else {
//            logger.info(logBuilder.delete(0, logBuilder.length()).append(" ------------ full source is processing ...... ").toString());
//        }

    }

    /**
     *
     * @param doc
     * @return
     */
    private Map<String, Object> check(Map<String, Object> doc) {

        try {
            String ZJLX = (String) doc.get("ZJLX");
            if (!"11".equals(ZJLX)) {
                if (index % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【ZJLX:").append(ZJLX).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String LGDM = (String) doc.get("LGDM");
            if (LGDM == null || LGDM.trim().isEmpty()) {
                if (index % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【LGDM:").append(LGDM).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String LGMC = (String) doc.get("LGDM_LGMC_FORMAT");
            if (LGMC == null || LGMC.trim().isEmpty()) {
                if (index % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【LGMC:").append(LGMC).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String RZSJ = (String) doc.get("RZSJ");
            if (RZSJ == null || RZSJ.trim().isEmpty()) {
                if (index % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【RZSJ:").append(RZSJ).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String LDSJ = (String) doc.get("LDSJ");
            if (LDSJ == null || LDSJ.trim().isEmpty()) {
                if (index % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【LDSJ:").append(LDSJ).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String ZJHM = (String) doc.get("ZJHM");
            if (ZJHM == null || ZJHM.trim().isEmpty() || (ZJHM.length() != 15 && ZJHM.length() != 18) || ZJHM.startsWith("00")) {
                if (index % sampleSize == 0) {
                    logger.info(logBuilder.delete(0, logBuilder.length()).append("--【ZJHM:").append(ZJHM).append("】-- ").append(" --- is unavailable").toString());
                }
                return null;
            }

            String XM = (String) doc.get("XM");
            if (XM == null) {
                XM = "*";
            }

            String FH = (String) doc.get("FH");
            if (FH == null) {
                FH = logBuilder.delete(0, logBuilder.length()).append("*-").append(UUID.randomUUID()).toString();
            }

            doc.clear();
            doc.put("sfzh", ZJHM);
            doc.put("xm", XM);
            doc.put("rzsj", dateFormat_s.parse(RZSJ));
            doc.put("ldsj", dateFormat_s.parse(LDSJ));
            doc.put("fh", FH);
            doc.put("lgdm", LGDM);
            doc.put("lgmc", LGMC);

        } catch (Exception ex) {
            logger.error(null, ex);
            doc = null;
        }

        return doc;
    }

}
