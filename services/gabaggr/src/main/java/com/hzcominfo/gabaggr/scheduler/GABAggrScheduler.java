/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.gabaggr.scheduler;

import com.hzcominfo.gabaggr.conf.GABAggrConfiguration;
import com.hzcominfo.gabaggr.conf.MongoSource;
import com.hzcominfo.gabaggr.conf.MongoSourceFactory;
import com.hzcominfo.gabaggr.dao.GABAggrFetchDao;
import com.hzcominfo.gabaggr.dao.GABAggrResultDao;
import com.hzcominfo.gabaggr.pojo.SubmitInput;
import com.hzcominfo.gabaggr.service.SubmitQueue;
import com.mongodb.DB;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
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
public class GABAggrScheduler {

    //private final Logger logger = LoggerFactory.getLogger(GABAggrScheduler.class);
    //private final StringBuilder logBuilder = new StringBuilder();
    private final AtomicLong counter = new AtomicLong(0L);

    private ExecutorService executor;

    @Autowired
    private GABAggrFetchDao _GABAggrFetchDao;

    @Autowired
    private GABAggrResultDao _GABAggrResultDao;

    /**
     *
     */
    @Scheduled(fixedDelay = 60_000)
    private void execute() {

        if (executor == null || executor.isShutdown()) {
            executor = Executors.newFixedThreadPool(GABAggrConfiguration.submit_queue_processor_max_size);

            for (int n = 0; n < GABAggrConfiguration.submit_queue_processor_max_size; n++) {

                Runnable task = new Runnable() {
                    @Override
                    public void run() {

                        Logger logger = LoggerFactory.getLogger(this.getClass());
                        StringBuilder logBuilder = new StringBuilder();

                        long startTime = 0;
                        long count = 0;

                        try {
                            SubmitInput tuple = (SubmitInput) SubmitQueue.take();
                            while (tuple != null) {

                                try {
                                    startTime = System.currentTimeMillis();

                                    String sessionId = tuple.getSessionId();
                                    String demision = tuple.getDimension();
                                    long[] servicesIds = tuple.getServiceIds();
                                    Map<String, String> condition = tuple.getCondition();
                                    String userCardId = tuple.getUserCardId();
                                    String userName = tuple.getUserName();
                                    String userDept = tuple.getUserDept();

                                    MongoSource source = MongoSourceFactory.getSource(GABAggrConfiguration.mongo_uri_rtdb);
                                    DB db = source.getClient().getDB(source.getClientURI().getDatabase());

                                    ConcurrentMap<Long, Map<String, Object>> serviceInfos = _GABAggrFetchDao.getServiceInfos(db, demision, servicesIds, condition);

                                    //int batchSize = servicesIds.length <= GABAggrConfiguration.submit_tuple_processor_max_size ? servicesIds.length : GABAggrConfiguration.submit_tuple_processor_max_size;
                                    //List<List<Long>> serviceIdGroups = new ArrayList<>(batchSize);
                                    //ExecutorService fetchExecutor = Executors.newFixedThreadPool(batchSize);
                                    _GABAggrResultDao.setBegin(db, sessionId, serviceInfos.size());

                                    serviceInfos.values().parallelStream().forEach((serviceInfo) -> {
//                                        System.out.println("serviceInfo --> " + serviceInfo);
//                                        System.out.println();

                                        Map<String, Object> serviceResult = _GABAggrFetchDao.getServiceResult(userCardId, userName, userDept, serviceInfo);

                                        DB _db = source.getClient().getDB(source.getClientURI().getDatabase());
                                        _GABAggrResultDao.save(_db, sessionId, serviceResult);
                                    });

                                    _GABAggrResultDao.setOk(db, sessionId);

                                    count = counter.incrementAndGet();
                                    //if (count % 1_000 == 0) {
                                    logger.info(logBuilder.delete(0, logBuilder.length()).append(count).append(" -- success --> ").append(sessionId).toString());//.append("-- spend time -- ").append((System.currentTimeMillis() - startTime) / 1_000).append(" ms").toString());
                                    //}

                                } catch (Exception ex) {
                                    SubmitQueue.put(tuple);
                                    logger.error("", ex);
                                }

                                tuple = (SubmitInput) SubmitQueue.take();
                            }
                        } catch (Exception ex) {
                            logger.error("", ex);
                        }
                    }
                };

                executor.submit(task);
            }
        }

    }

}
