package com.shirly.neteasemaster.decoupingDemo.dao;

import com.shirly.neteasemaster.decoupingDemo.model.Order;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/18 10:37
 * @description 描述
 */
@Mapper
@CacheNamespace
public interface OrderDao {

    List<Order> query(@Param("customerId") String customerId);
}
