package com.shirly.neteasemaster.decoupingDemo.controller;

import com.shirly.neteasemaster.decoupingDemo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/18 10:23
 * @description
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @GetMapping("/query")
    public Object query(String customerId, int pageNum, int pageSize) {
        return orderService.pageQuery(customerId, pageNum, pageSize);
    }
}
