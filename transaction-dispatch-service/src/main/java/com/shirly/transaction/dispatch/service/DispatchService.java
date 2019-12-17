package com.shirly.transaction.dispatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/17 16:24
 * @description 运单的数据库服务
 */
@Service
public class DispatchService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void dispatch(String orderId) throws Exception {

    }
}
