/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarArticleDAO;
import com.lyzhkj.fhl.pojo.CategoryArticleDetail;
import com.lyzhkj.fhl.pojo.CategoryArticleIntro;
import java.util.List;
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

    public List<CategoryArticleIntro> listPolicy() {

        return garArticleDAO.listPolicy();
    }
    
    public CategoryArticleDetail policyDetail(long id){
        
        return garArticleDAO.findById(id);
    }

}
