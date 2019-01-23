/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.slience.dao.mapper;

import cn.slience.dao.model.Test;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 * @author Len
 */
@Mapper
public interface TestMapper {

    @Update("insert into test (data) values (#{data})")
    public int insert(@Param("data") String data);

    @Select("SELECT * FROM test LIMIT 10")
    public List<Test> query();
    
    @Select("SELECT * FROM test WHERE id = #{id}")
    public Test queryById(@Param("id") long id);

}
