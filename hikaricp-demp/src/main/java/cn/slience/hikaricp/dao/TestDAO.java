/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.hikaricp.dao;

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
public class TestDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String getGroupCategoryNameById(int id) {
        String result = null;

        try {
            String sql ="SELECT name FROM group_category WHERE id=?";
            Map<String, Object> row = jdbcTemplate.queryForMap(sql, id);
            result = (String) row.get("name");

        } catch (Exception e) {
            LOGGER.error("id: {}", id, e); //new StringBuilder("id:").append(id).toString()
        }

        return result;
    }

}
