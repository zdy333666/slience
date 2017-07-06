/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.pojo.GarKnowledge;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author breeze
 */
@Repository
public class GarKnowledgeDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GarKnowledgeDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     *
     * @return
     */
    public List<GarKnowledge> list() {

        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT KnowledgeId,KnowledgeName,Keyword,Description FROM gar_Knowledge WHERE Enabled=1");

        List<GarKnowledge> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            GarKnowledge item = new GarKnowledge();
            item.setId((int) row.get("KnowledgeId"));
            item.setName((String) row.get("KnowledgeName"));
            item.setKeyword((String) row.get("Keyword"));
            item.setDescription((String) row.get("Description"));
            result.add(item);
        }

        return result;
    }

    /**
     *
     * @param word
     * @return
     */
    public GarKnowledge search(String word) {

        GarKnowledge result = null;
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap("SELECT KnowledgeId,KnowledgeName,Keyword,Description FROM gar_Knowledge WHERE Enabled=1 AND Keyword LIKE ?", "%" + word + "%");

            result = new GarKnowledge();
            result.setId((int) row.get("KnowledgeId"));
            result.setName((String) row.get("KnowledgeName"));
            result.setKeyword((String) row.get("Keyword"));
            result.setDescription((String) row.get("Description"));
        } catch (Exception e) {
            LOGGER.error("",e);
            return null;
        }

        return result;
    }

}
