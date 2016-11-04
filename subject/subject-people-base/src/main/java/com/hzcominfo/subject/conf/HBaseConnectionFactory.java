/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.subject.conf;

/**
 *
 * @author cominfo4
 */
import java.io.IOException;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HBaseConnectionFactory {

    private static final Logger logger = LoggerFactory.getLogger(HBaseConnectionFactory.class);

    private static volatile Connection connection;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                release();
                logger.info(new StringBuilder().append("-- 【 ").append(Thread.currentThread().getName()).append("】 -- 所有 HBase 数据源连接已释放 *********************").toString());
            }
        });
    }

    /**
     *
     * @return
     */
    public static Connection getConnection() {

        if (connection != null && (!connection.isClosed())) {
            return connection;
        }

        synchronized (HBaseConnectionFactory.class) {
            if (connection != null && (!connection.isClosed())) {
                return connection;
            }

            try {
                connection = ConnectionFactory.createConnection(HBaseConfiguration.create());
            } catch (IOException ex) {
                logger.error("", ex);
            }
        }

        return connection;
    }

    /**
     *
     */
    private static void release() {

        if (connection != null && (!connection.isClosed())) {
            try {
                connection.close();
            } catch (IOException ex) {
                logger.error("", ex);
            }
        }
    }

}
