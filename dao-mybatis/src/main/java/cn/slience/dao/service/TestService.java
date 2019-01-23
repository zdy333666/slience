/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.dao.service;

import cn.slience.dao.mapper.TestMapper;
import cn.slience.dao.model.Test;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Len
 */
@Service
public class TestService {
    
    private final Logger logger = LoggerFactory.getLogger(TestService.class);
    
    @Autowired
    private TestMapper testMapper;
    
    @Transactional(rollbackFor = Exception.class)
    public int test() throws Exception {
        
        int result1 = testMapper.insert("hello2");
        
        if (result1 < 1) {
            throw new SQLException();
        }
        
        int result2 = testMapper.insert("hello3");
        
        return result1 & result2;
    }
    
    public List<Test> query() {
        
        return testMapper.query();
    }
    
    public Test queryById() {
        
        Test test = testMapper.queryById(1);
        
        logger.info("queryById-->{}", test);
        
        return test;
    }
    
}
