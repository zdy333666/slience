/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarArticleDAO;
import com.lyzhkj.fhl.dto.PolicyPage;
import com.lyzhkj.fhl.pojo.CategoryArticleDetail;
import com.lyzhkj.fhl.pojo.CategoryArticleIntro;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class CategoryPolicyService {

    @Autowired
    private GarArticleDAO garArticleDAO;

    public PolicyPage listPolicy(String city, int page, int size) {

        PolicyPage result = new PolicyPage();

        try {
            int count = garArticleDAO.count(city);
            List<CategoryArticleIntro> rows = garArticleDAO.listIntro(city, page, size);

            result.setCount(count);
            result.setRows(rows);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public CategoryArticleDetail policyDetail(long id) {

        CategoryArticleDetail detail = garArticleDAO.findById(id);
        if (detail != null) {
            garArticleDAO.increaseArticleHitCount(id);
        }

        return detail;
    }

}
