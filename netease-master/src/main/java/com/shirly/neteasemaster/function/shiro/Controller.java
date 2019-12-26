package com.shirly.neteasemaster.function.shiro;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/25 17:38
 * @description 描述
 */
@RestController
public class Controller {
    @RequestMapping("/admin/test1")
    public String test1() {
        return "test1";
    }

    @RequestMapping("/account/test2")
    // 方法级别的访问控制
    // 必须要有account:add权限才可以访问
    @RequiresPermissions("account:add")
    public String test2() {
        return "test2";
    }
}
