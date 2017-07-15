/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.redis.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author breeze
 */
@Mapper
public interface TestAnnoMapper {

    @Select("SELECT name FROM group_category WHERE id= #{id}")
    String getGroupCategoryNameById(@Param("id") int id);

}
