/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.aggregate.base.dao;

import com.hzcominfo.aggregate.base.pojo.SolrSearchInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 *
 * @author cominfo4
 */
@Repository
public class SolrSearchDao {

    private final Logger logger = LoggerFactory.getLogger(SolrSearchDao.class);

    /**
     *
     * @param input
     * @return
     * @throws SolrServerException
     * @throws IOException
     */
    public List<Map<String, Object>> search(SolrSearchInput input) {

        SolrQuery query = new SolrQuery();
        query.setQuery(input.getQuery());
        query.setFields(input.getFields());

        for (Entry<String, Integer> entry : input.getSort().entrySet()) {
            int sortType = entry.getValue();
            if (sortType == 1) {
                query.addSort(entry.getKey(), SolrQuery.ORDER.asc);
            } else if (sortType == -1) {
                query.addSort(entry.getKey(), SolrQuery.ORDER.desc);
            }
        }

        query.setRows(10000); // 2147483630

        SolrDocumentList docs = null;
        try {
            HttpSolrClient server = new HttpSolrClient(input.getUrl());
            server.setSoTimeout(1000);
            server.setConnectionTimeout(1000);
            server.setAllowCompression(true);
            QueryResponse rsp = server.query(query);
            docs = rsp.getResults();
            server.close();
            server = null;
        } catch (Exception e) {
            logger.error(null, e);
        }
        if (docs == null) {
            return null;
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (SolrDocument document : docs) {
            result.add(document.getFieldValueMap());
        }

        return result;
    }

}
