/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hzcominfo.rchl.dao;

import com.hzcominfo.rchl.pojo.DetailInput;
import com.hzcominfo.rchl.pojo.RCHLCarDetail;
import com.hzcominfo.rchl.pojo.RCHLPersonDetail;
import com.hzcominfo.rchl.pojo.Page;
import com.hzcominfo.rchl.pojo.RCHLCarDetailResult;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author cominfo4
 */
@Component
public class RCHL_DPTCarDetailDao {

    private final Logger logger = LoggerFactory.getLogger(RCHL_DPTCarDetailDao.class);

    public RCHLCarDetailResult getDetail(String uri, DetailInput input) {

        RCHLCarDetailResult result = new RCHLCarDetailResult();

        HttpSolrClient server = null;
        try {
            String cjbmcode = input.getCjbmcode();
            String startTime = input.getStartTime();
            String endTime = input.getEndTime();

            int page = input.getPage();
            page = page < 1 ? 1 : page;

            int limit = input.getLimit();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormat_l = new SimpleDateFormat("yyyyMMddHHmmss");

            Map<String, Object> params = new HashMap<>();
            params.put("SJLY_s", input.getFlag());
            params.put("HCBB_s", input.getYycj());
            params.put("HCJG_s", input.getReturncode());
            if (!(cjbmcode == null || cjbmcode.trim().isEmpty() || "*".equals(cjbmcode))) {
                params.put("HCDWJGDM_s", cjbmcode + "*");
            }
            params.put("HCSJ_l", "[" + (startTime == null ? "*" : Long.valueOf(dateFormat_l.format(dateFormat.parse(startTime)))) + " TO " + (endTime == null ? "*" : Long.valueOf(dateFormat_l.format(dateFormat.parse(endTime)))) + "]");

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

            int start = limit * (page - 1);

            SolrQuery query = new SolrQuery();
            query.setQuery(queryBuilder.toString());
            query.setFields("HCDWJGDM_s,HCDW_s,HCRXM_s,HCSJ_dt,HCBB_s,HCBBFORMAT_s,CLHP_s,HCJG_s,HCJGFORMAT_s,SJLY_s,SJLYFORMAT_s");
            query.addSort("HCSJ_l", SolrQuery.ORDER.desc);
            query.setStart(start);
            query.setRows(limit); // 2147483630

            logger.debug("SolrQuery --> " + query);

            server = new HttpSolrClient(uri);
            server.setSoTimeout(15000);
            server.setConnectionTimeout(2000);
            server.setAllowCompression(true);
            QueryResponse response = server.query(query, SolrRequest.METHOD.POST);

            int total = (int) response.getResults().getNumFound();
            SolrDocumentList docs = response.getResults();

            List<RCHLCarDetail> InspectCarDetails = new LinkedList<>();
            for (SolrDocument document : docs) {
                RCHLCarDetail detail = new RCHLCarDetail();
                detail.setCjbm((String) document.getFieldValue("HCDWJGDM_s"));
                detail.setCjbmmc((String) document.getFieldValue("HCDW_s"));
                detail.setCjr("");
                detail.setCjrmc((String) document.getFieldValue("HCRXM_s"));
                detail.setCjsjFormat(dateFormat.format(new Date(((Date) document.getFieldValue("HCSJ_dt")).getTime() - 1000 * 60 * 60 * 8)));
                detail.setClsbdh("");
                detail.setClsyr("");
                detail.setDptcode((String) document.getFieldValue("HCDWJGDM_s"));
                detail.setDptcode2(cjbmcode);
                detail.setDptname("");
                detail.setFdjh(null);
                detail.setFdjxlh(null);
                detail.setFxdh(null);
                detail.setFxdhFormat(null);
                detail.setHclx((String) document.getFieldValue("HCBB_s"));
                detail.setHclxFormat((String) document.getFieldValue("HCBBFORMAT_s"));
                detail.setHphm((String) document.getFieldValue("CLHP_s"));
                detail.setHpzl(null);
                detail.setLylx(null);
                detail.setPcdz(null);
                detail.setPp(null);
                detail.setReturncode((String) document.getFieldValue("HCJG_s"));
                detail.setReturncodeFormat((String) document.getFieldValue("HCJGFORMAT_s"));
                detail.setSjly((String) document.getFieldValue("SJLY_s"));
                detail.setSjlyFormat((String) document.getFieldValue("SJLYFORMAT_s"));
                detail.setSyrzjhm(null);
                detail.setXh(null);
                detail.setYs(null);
                detail.setYycj(null);
                detail.setYycjFormat(null);

                InspectCarDetails.add(detail);
            }

            Page _Page = new Page();
            _Page.setCapacity(limit);
            _Page.setCurr(page);
            _Page.setLast(total % limit == 0 ? (total / limit) : (total / limit) + 1);
            _Page.setNext(limit * page > total ? page : page + 1);
            _Page.setPrev(page > 1 ? page - 1 : page);
            _Page.setSize(docs.size());
            _Page.setStart(start);
            _Page.setTotal(total);

            result.setMongoCLHCList(InspectCarDetails);
            result.setPage(_Page);

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
