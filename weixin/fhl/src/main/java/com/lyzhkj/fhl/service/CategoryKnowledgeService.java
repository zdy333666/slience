/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lyzhkj.fhl.service;

import com.lyzhkj.fhl.dao.GarKnowledgeDAO;
import com.lyzhkj.fhl.pojo.GarKnowledge;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class CategoryKnowledgeService {

    @Autowired
    private GarKnowledgeDAO garKnowledgeDAO;

    public List<GarKnowledge> list() {

        return garKnowledgeDAO.list();
    }

    public GarKnowledge search(String word) {

        return garKnowledgeDAO.search(word);
    }

}
