/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.subject.scheduler;

import com.hzcominfo.subject.conf.HBaseConnectionFactory;
import com.hzcominfo.subject.conf.SubjectConfiguration;
import com.hzcominfo.subject.dao.HBaseDao;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.Table;

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
public class AsynBuilder {

    private final BlockingQueue queue = new LinkedBlockingQueue(10_0000);
    private final AtomicLong counter = new AtomicLong();

    int taskSize = 40;
    private ExecutorService executor;

    @Autowired
    private HBaseDao hbaseDao;

    /**
     *
     */
    @Scheduled(fixedRate = 10_000)
    public void service() {

        if (executor == null) {
            executor = Executors.newFixedThreadPool(taskSize);
            for (int n = 0; n < taskSize; n++) {
                Runnable task = new Runnable() {

                    @Override
                    public void run() {

                        Logger logger = LoggerFactory.getLogger(this.getClass());
                        StringBuilder logBuilder = new StringBuilder();

                        long startTime = 0;
                        long count = 0;
                        boolean ok = false;

                        Connection connection = null;
                        Table table = null;
                        String rowKey = null;

                        try {
                            Map<String, Object> tuple = null;
                            while ((tuple = (Map<String, Object>) queue.take()) != null) {

                                startTime = System.currentTimeMillis();
                                try {

                                    rowKey = new StringBuilder((String) tuple.get("SFZH")).reverse().toString();

                                    connection = HBaseConnectionFactory.getConnection();
                                    table = connection.getTable(TableName.valueOf(SubjectConfiguration.table_hbase_person));
                                    ok = hbaseDao.put(table, rowKey, tuple);
                                    table.close();

                                    if (ok) {
                                        count = counter.incrementAndGet();
                                    } else {
                                        queue.put(tuple);
                                    }

                                    if (count % 4_000 == 0) {
                                        logger.info(logBuilder.delete(0, logBuilder.length()).append(count).append(" -- success --> ").append(rowKey).append(" -- ").append(tuple).append("-- spend time -- ").append(System.currentTimeMillis() - startTime).append(" ms").toString());
                                    }

                                } catch (Exception ex) {
                                    queue.put(tuple);
                                    if (table != null) {
                                        table.close();
                                    }
                                    logger.error("", ex);
                                }

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

    public boolean offer(Map<String, Object> tuple) {
        return queue.offer(tuple);
    }

    public boolean isOk() {
        return queue.isEmpty();
    }

}
