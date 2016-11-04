/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.search.dao;

import com.hzcominfo.search.pojo.SolrSearchInput;
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
import org.springframework.stereotype.Repository;

/**
 *
 * @author cominfo4
 */
@Repository
public class SolrSearchDao {

    /**
     *
     * @param input
     * @return
     * @throws SolrServerException
     * @throws IOException
     */
    public List<Map<String, Object>> search(SolrSearchInput input) throws SolrServerException, IOException {

        HttpSolrClient server = new HttpSolrClient(input.getUrl());
        server.setSoTimeout(1000);
        server.setConnectionTimeout(1000);
        server.setAllowCompression(true);

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

        QueryResponse rsp = server.query(query);
        SolrDocumentList docs = rsp.getResults();
        server.close();

        List<Map<String, Object>> result = new ArrayList<>();
        for (SolrDocument document : docs) {
            result.add(document.getFieldValueMap());
        }

        return result;
    }

}
