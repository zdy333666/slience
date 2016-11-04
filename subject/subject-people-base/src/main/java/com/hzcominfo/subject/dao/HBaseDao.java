/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.subject.dao;

import com.hzcominfo.subject.conf.SubjectConfiguration;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Row;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class HBaseDao {

    private final Logger logger = LoggerFactory.getLogger(HBaseDao.class);

    /**
     *
     * @param table
     * @param rowKey
     * @param doc
     * @throws IOException
     */
    public boolean put(Table table, String rowKey, Map<String, Object> doc) throws IOException {

        Put put = new Put(Bytes.toBytes(rowKey));

        //int length = 0;
        try {
            for (Entry<String, Object> entry : doc.entrySet()) {
                Object valueObj = entry.getValue();
                if (valueObj == null) {
                    continue;
                }

                ByteArrayOutputStream bo = new ByteArrayOutputStream();
                ObjectOutputStream oo = new ObjectOutputStream(bo);

                oo.writeObject(valueObj);
                byte[] value = bo.toByteArray();

                bo.close();
                oo.close();

                put.addColumn(Bytes.toBytes(SubjectConfiguration.hbase_cf_base), Bytes.toBytes(SubjectConfiguration.hbase_column_prefix + entry.getKey()), value);
                //length += value.length;
            }

            table.put(put);
        } catch (Exception e) {
            logger.error("", e);
            return false;
        }

        return true;
    }

    /**
     *
     * @param table
     * @param params
     * @throws IOException
     * @throws InterruptedException
     */
    public boolean putBatch(Table table, Map<String, Map<String, Object>> params) throws IOException, InterruptedException {

        List<Row> batch = new ArrayList<>();

        //int length = 0;
        try {
            for (Entry<String, Map<String, Object>> rowEntry : params.entrySet()) {
                String rowKey = rowEntry.getKey();
                Map<String, Object> doc = rowEntry.getValue();

                Put put = new Put(Bytes.toBytes(rowKey));

                for (Map.Entry<String, Object> entry : doc.entrySet()) {
                    Object valueObj = entry.getValue();
                    if (valueObj == null) {
                        continue;
                    }

                    ByteArrayOutputStream bo = new ByteArrayOutputStream();
                    ObjectOutputStream oo = new ObjectOutputStream(bo);

                    oo.writeObject(valueObj);
                    byte[] value = bo.toByteArray();

                    bo.close();
                    oo.close();

                    put.addColumn(Bytes.toBytes(SubjectConfiguration.hbase_cf_base), Bytes.toBytes(SubjectConfiguration.hbase_column_prefix + entry.getKey()), value);
                    //length += value.length;
                }

                batch.add(put);
            }

            table.batch(batch);
        } catch (Exception e) {
            logger.error("", e);
            return false;
        }

        return true;
    }

    /**
     *
     * @param table
     * @param rowKey
     * @param doc
     * @throws IOException
     */
    public boolean delete(Table table, String rowKey, Map<String, Object> doc) throws IOException {

        Delete delete = new Delete(Bytes.toBytes(rowKey));

        try {
            for (Entry<String, Object> entry : doc.entrySet()) {
                delete.addColumn(Bytes.toBytes(SubjectConfiguration.hbase_cf_base), Bytes.toBytes(SubjectConfiguration.hbase_column_prefix + entry.getKey()));//, value);
            }

            table.delete(delete);
        } catch (Exception e) {
            logger.error("", e);
            return false;
        }

        return true;
    }

}
