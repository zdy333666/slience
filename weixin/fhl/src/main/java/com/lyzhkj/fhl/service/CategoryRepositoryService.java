/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarArticleCategoryDAO;
import com.lyzhkj.fhl.dao.GarArticleDAO;
import com.lyzhkj.fhl.pojo.CategoryArticleDetail;
import com.lyzhkj.fhl.pojo.CategoryArticleIntro;
import com.lyzhkj.fhl.pojo.GarArticleCategory;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class CategoryRepositoryService {

    @Autowired
    private GarArticleDAO garArticleDAO;

    @Autowired
    private GarArticleCategoryDAO garArticleCategoryDAO;

    public List<CategoryArticleIntro> findByCategoryId(int categoryId) {

        return garArticleDAO.findByCategoryId(categoryId);
    }

    public List<CategoryArticleIntro> search(String word) {

        //查出分类知识库的文本类别列表
        List<GarArticleCategory> categoryItems = garArticleCategoryDAO.findItemsByParentId("3");
        int size = categoryItems.size();
        int[] categoryIds = new int[size];
        for (int i = 0; i < size; i++) {
            categoryIds[i] = categoryItems.get(i).getId();
        }

        return garArticleDAO.search(categoryIds, word);
    }

    public CategoryArticleDetail knowledgeDetail(long id) {

        return garArticleDAO.findById(id);
    }

}
