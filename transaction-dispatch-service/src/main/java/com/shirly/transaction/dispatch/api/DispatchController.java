package com.shirly.transaction.dispatch.api;

import com.shirly.transaction.dispatch.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/17 16:23
 * @description 运单系统http api
 */
@RestController
@RequestMapping("/dispatch-api")
public class DispatchController {

    @Autowired
    DispatchService dispatchService;

    @GetMapping("/dispatch")
    public String dispatch(String orderId) throws Exception {
        Thread.sleep(3000L); // 此处模拟业务耗时
        dispatchService.dispatch(orderId);
        return "OK";
    }
}
