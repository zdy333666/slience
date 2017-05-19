/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.pojo.CategoryArticleIntro;
import com.lyzhkj.fhl.pojo.GarArticleCategory;
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
public class GarArticleCategoryDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<GarArticleCategory> findItemsByParentId(String parentId) {

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT CategoryId,CategoryName FROM gar_ArticleCategory WHERE Enabled=1 AND ParentId=? ORDER BY CategoryOrder", parentId);

        List<GarArticleCategory> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {

            GarArticleCategory item = new GarArticleCategory();
            item.setId((int) row.get("CategoryId"));
            item.setName((String) row.get("CategoryName"));
            result.add(item);
        }

        return result;
    }

}
