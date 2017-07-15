/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.redis.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author breeze
 */
@Repository
public class TestMyBatisDAO {

    @Autowired
    private SqlSession sqlSession;

    public String getGroupCategoryNameById(int id) {

        return sqlSession.selectOne("cn.slience.redis.mybatis.mapper.getGroupCategoryNameById", id);
    }

}
