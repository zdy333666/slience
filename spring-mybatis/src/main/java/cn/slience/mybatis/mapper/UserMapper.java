/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.mybatis.mapper;

import cn.slience.mybatis.pojo.User;

/**
 *
 * @author breeze
 */
public interface UserMapper {
    
    User getUserById(long id);
    
}
