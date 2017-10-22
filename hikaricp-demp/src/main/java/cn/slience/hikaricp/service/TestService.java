/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.hikaricp.service;

import cn.slience.hikaricp.dao.TestDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class TestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestService.class);

    public static final String PREFIX = "group_category.";

    @Autowired
    private TestDAO testDAO;

    public String getGroupCategoryNameById(int id) {

        String result = testDAO.getGroupCategoryNameById(id);
        LOGGER.info("get value from mysql with id:{}", id);

        return result;
    }

}
