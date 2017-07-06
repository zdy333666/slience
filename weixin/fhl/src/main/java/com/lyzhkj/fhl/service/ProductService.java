/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarProductDAO;
import com.lyzhkj.fhl.dto.GoodsPage;
import com.lyzhkj.fhl.pojo.GarProduct;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class ProductService {

    @Autowired
    private GarProductDAO garProductDAO;

    public GoodsPage list(String openId, int page, int size) {

        GoodsPage result = new GoodsPage();

        try {
            int count = garProductDAO.count();
            List<GarProduct> rows = garProductDAO.list(page, size);

            result.setCount(count);
            result.setRows(rows);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public GarProduct findById(String id) {
        return garProductDAO.findById(id);
    }

}
