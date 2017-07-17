/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.mybatis.service;

import cn.slience.mybatis.mapper.UserMapper;
import cn.slience.mybatis.pojo.User;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author breeze
 */
@Service
public class UserService {

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    public User getUserById(long id) {

        User user = null;

        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserMapper mapper = session.getMapper(UserMapper.class);
            user = mapper.getUserById(id);
        } finally {
            session.close();
        }

        return user;
    }

}
