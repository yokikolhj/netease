package com.shirly.neteasemaster.decoupingDemo.dao;

import com.shirly.neteasemaster.decoupingDemo.model.User;
import org.apache.ibatis.annotations.*;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/18 10:37
 * @description 描述
 */
@Mapper
@CacheNamespace
public interface UserDao {

    @Select("select id,name from user where id=#{id}")
    User find(@Param("id")String id);

    @Insert("insert user(id,name) values(#{id}, #{userName})")
    void insert(User user);

    @Update("update user set name=#{userName} where id=#{id}")
    void update(User user);

    @Delete("delete from user where id=#{id}")
    void delete(@Param("id")String id);
}
