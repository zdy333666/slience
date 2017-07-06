/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.pojo.CategoryArticleDetail;
import com.lyzhkj.fhl.pojo.CategoryArticleIntro;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author breeze
 */
@Repository
public class GarArticleDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(GarArticleDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     *
     * @param city
     * @return
     */
    public int count(String city) {
        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT count(1) AS count FROM gar_Article WHERE CategoryId=2 AND City=?", city);

        return (int) row.get("count");
    }

    /**
     *
     * @return
     */
    public List<CategoryArticleIntro> listIntro(String city, int page, int size) {

        int start = size * (page - 1);
        int end = size * page;

        //分页
        String sql = "SELECT w2.n, w1.* FROM gar_Article w1, (SELECT TOP " + end + " row_number() OVER (ORDER BY UpdateTime DESC) n, ArticleId FROM gar_Article WHERE CategoryId=2 AND City=?) w2 WHERE w1.ArticleId = w2.ArticleId AND w2.n > ? ORDER BY w2.n ASC";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, city, start);

        //List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT TOP 10 ArticleId,ArticleTitle,ArticleInfo,SmallImage,UpdateTime FROM gar_Article WHERE CategoryId=2 AND City=? ORDER BY UpdateTime DESC", city);
        List<CategoryArticleIntro> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            CategoryArticleIntro policyIntro = new CategoryArticleIntro();
            policyIntro.setId((long) row.get("ArticleId"));
            policyIntro.setTitle((String) row.get("ArticleTitle"));
            policyIntro.setIntro((String) row.get("ArticleInfo"));
            policyIntro.setPic((String) row.get("SmallImage"));
            policyIntro.setCreateTime((Date) row.get("UpdateTime"));
            result.add(policyIntro);
        }

        return result;
    }

    /**
     *
     * @param categoryId
     * @return
     */
    public List<CategoryArticleDetail> findByCategoryId(int categoryId) {

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT ArticleId,ArticleTitle,ArticleInfo,ArticleContent,SmallImage,BigImage,hitCount,UpdateTime FROM gar_Article WHERE CategoryId=? ORDER BY UpdateTime DESC", categoryId);

        List<CategoryArticleDetail> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            CategoryArticleDetail item = new CategoryArticleDetail();
            item.setId((long) row.get("ArticleId"));
            item.setTitle((String) row.get("ArticleTitle"));
            item.setIntro((String) row.get("ArticleInfo"));
            item.setContent((String) row.get("ArticleContent"));
            item.setPic((String) row.get("SmallImage"));
            item.setBigPic((String) row.get("BigImage"));
            item.setHitCount((int) row.get("hitCount"));
            item.setCreateTime((Date) row.get("UpdateTime"));
            result.add(item);
        }

        return result;
    }

    /**
     *
     * @param id
     * @return
     */
    public CategoryArticleDetail findById(long id) {

        CategoryArticleDetail policyDetail = new CategoryArticleDetail();
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap("SELECT ArticleTitle,ArticleInfo,ArticleContent,SmallImage,BigImage,hitCount,UpdateTime FROM gar_Article WHERE ArticleId=?", id);

            policyDetail.setId(id);
            policyDetail.setTitle((String) row.get("ArticleTitle"));
            policyDetail.setIntro((String) row.get("ArticleInfo"));
            policyDetail.setContent((String) row.get("ArticleContent"));
            policyDetail.setPic((String) row.get("SmallImage"));
            policyDetail.setBigPic((String) row.get("BigImage"));
            policyDetail.setHitCount((int) row.get("hitCount"));
            policyDetail.setCreateTime((Date) row.get("UpdateTime"));

        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return policyDetail;
    }

    @Transactional
    public void increaseArticleHitCount(long id) {
        jdbcTemplate.update("UPDATE gar_Article SET hitCount=hitCount+1 WHERE ArticleId=?", id);
    }

}
