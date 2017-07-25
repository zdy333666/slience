/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.dao;

import com.lyzhkj.fhl.pojo.GarProduct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
public class GarProductDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GarProductDAO.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int count() {
        Map<String, Object> row = jdbcTemplate.queryForMap("SELECT count(*) AS count FROM gar_Product WHERE sellstate=0 AND status=1 AND productNumber>0");

        return (int) row.get("count");
    }

    public List<GarProduct> list(int page, int size) {

        List<GarProduct> result = new ArrayList<>();

        try {
            int start = size * (page - 1);
            int end = size * page;

            //分页
            String sql = "SELECT w2.n, w1.productId,w1.productName,w1.unitprice,w1.marketPrice,w1.productThumb FROM gar_Product w1, "
                    + "(SELECT TOP " + end + " row_number() OVER (ORDER BY lastUpdateTime DESC) n, productId FROM gar_Product WHERE sellstate=0 AND status=1 AND productNumber>0) w2"
                    + " WHERE w1.productId = w2.productId AND w2.n > ? ORDER BY w2.n ASC";

            //"SELECT productId,productName,unitprice,marketPrice,productThumb FROM gar_Product WHERE sellstate=0 AND status=1 AND productNumber>0 ORDER BY lastUpdateTime DESC"
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, start);

            for (Map<String, Object> row : rows) {
                GarProduct item = new GarProduct();
                item.setId((String) row.get("productId"));
                item.setName((String) row.get("productName"));
                item.setPrice(((BigDecimal) row.get("marketPrice")).doubleValue());
                item.setUnitprice((int) row.get("unitprice"));
                item.setPic((String) row.get("productThumb"));
                result.add(item);
            }

        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return result;
    }

    /**
     *
     * @param id
     * @return
     */
    public GarProduct findById(String productId) {
        GarProduct result = new GarProduct();
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap("SELECT productName,unitprice,marketPrice,productThumb,productDescription,ExpiredDate,ExchangeCode FROM gar_Product WHERE productId=? ", productId);

            result.setId(productId);
            result.setName((String) row.get("productName"));
            result.setPrice(((BigDecimal) row.get("marketPrice")).doubleValue());
            result.setUnitprice((int) row.get("unitprice"));
            result.setPic((String) row.get("productThumb"));
            result.setDescription((String) row.get("productDescription"));
            result.setExpiredDate((Date) row.get("ExpiredDate"));
            result.setExchangeCode((String) row.get("ExchangeCode"));
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

        return result;
    }

    /**
     *
     * @param productId
     * @return
     */
    public String findExchangeCodeById(String productId) {
        String result = null;
        try {
            Map<String, Object> row = jdbcTemplate.queryForMap("SELECT ExchangeCode FROM gar_Product WHERE productId=?", productId);
            result = (String) row.get("ExchangeCode");
        } catch (Exception e) {
            LOGGER.error("", e);
            return null;
        }

        return result;
    }

    public boolean deductProductStore(String productId) {

        String sql = "UPDATE gar_Product SET productNumber=productNumber-1 WHERE  productId=? AND productNumber-1 >=0";
        int n = jdbcTemplate.update(sql, productId);

        return n > 0;
    }

}
