/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.rchl.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.PivotField;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.util.NamedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class RCHL_JCZCountDao {

    private final Logger logger = LoggerFactory.getLogger(RCHL_JCZCountDao.class);

    public List<Map<String, Object>> count(String uri, Map<String, Object> params) {

        List<Map<String, Object>> result = new ArrayList<>();

        StringBuilder queryBuilder = null;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (queryBuilder == null) {
                queryBuilder = new StringBuilder();
                queryBuilder.append(entry.getKey()).append(":").append(entry.getValue());
            } else {
                queryBuilder.append(" AND ").append(entry.getKey()).append(":").append(entry.getValue());
            }
        }

        if (queryBuilder == null) {
            return result;
        }

        SolrQuery query = new SolrQuery();
        query.setQuery(queryBuilder.toString());
        query.setFacet(true);
        //query.add("facet.query", queryBuilder.toString());
        query.add("facet.pivot", "HCDWJGDM_s,HCJG_s");
        //query.add("facet.prefix", facetPrefix);
        query.setFacetLimit(2147483630);
        query.setRows(0); // 2147483630

        logger.debug("SolrQuery --> " + query);

        HttpSolrClient server = null;
        try {
            server = new HttpSolrClient(uri);
            server.setSoTimeout(15000);
            server.setConnectionTimeout(2000);
            server.setAllowCompression(true);
            QueryResponse response = server.query(query, SolrRequest.METHOD.POST);

            NamedList<List<PivotField>> facetPivot = response.getFacetPivot();

            //logger.debug("facetPivot --> " + facetPivot);
            if (facetPivot == null || facetPivot.size() == 0) {
                return result;
            }

            List<PivotField> pivots = facetPivot.getVal(0);
            if (pivots == null) {
                return result;
            }

            for (PivotField pivot : pivots) {
                List<PivotField> pivotFields = pivot.getPivot();
                if (pivotFields == null) {
                    continue;
                }

                List<Map<String, Object>> items = new ArrayList<>();
                for (PivotField field : pivotFields) {
                    Map<String, Object> HCJG_sItem = new HashMap<>();
                    HCJG_sItem.put("value", field.getValue());
                    HCJG_sItem.put("count", field.getCount());

                    items.add(HCJG_sItem);
                }

                String value = (String) pivot.getValue();

                Map<String, Object> row = new HashMap<>();
                row.put("value", value);
                row.put("items", items);

                result.add(row);
            }

            server.close();
        } catch (Exception e) {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException ex) {
                }
            }
            logger.error("", e);
        }

        return result;
    }

}
