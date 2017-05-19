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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author breeze
 */
@Repository
public class GarArticleDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     *
     * @return
     */
    public List<CategoryArticleIntro> listPolicy() {

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT ArticleId,ArticleTitle,ArticleInfo,SmallImage,UpdateTime FROM gar_Article WHERE CategoryId=2 ORDER BY UpdateTime DESC");

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
    public List<CategoryArticleIntro> findByCategoryId(int categoryId) {

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT ArticleId,ArticleTitle,ArticleInfo,SmallImage,UpdateTime FROM gar_Article WHERE CategoryId=? ORDER BY UpdateTime DESC", categoryId);

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
     * @param word
     * @return
     */
    public List<CategoryArticleIntro> search(int[] categoryIds, String word) {

        StringBuilder categoryIdsStr = new StringBuilder();
        for (int id : categoryIds) {
            if (categoryIdsStr.length() > 0) {
                categoryIdsStr.append(",");
            }
            categoryIdsStr.append(id);
        }

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT ArticleId,ArticleTitle,ArticleInfo,SmallImage,UpdateTime FROM gar_Article WHERE CategoryId IN (?) AND ArticleInfo LIKE '%?%' ORDER BY UpdateTime DESC", categoryIdsStr.toString(), word);

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
     * @param id
     * @return
     */
    public CategoryArticleDetail findById(long id) {

        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT ArticleTitle,ArticleInfo,ArticleContent,SmallImage,BigImage,hitCount,UpdateTime FROM gar_Article WHERE ArticleId=?", id);

        CategoryArticleDetail policyDetail = new CategoryArticleDetail();
        policyDetail.setId((long) row.get("ArticleId"));
        policyDetail.setTitle((String) row.get("ArticleTitle"));
        policyDetail.setIntro((String) row.get("ArticleInfo"));
        policyDetail.setContent((String) row.get("ArticleContent"));
        policyDetail.setPic((String) row.get("SmallImage"));
        policyDetail.setBigPic((String) row.get("BigImage"));
        policyDetail.setHitCount((int) row.get("hitCount"));
        policyDetail.setCreateTime((Date) row.get("UpdateTime"));

        return policyDetail;
    }

}
