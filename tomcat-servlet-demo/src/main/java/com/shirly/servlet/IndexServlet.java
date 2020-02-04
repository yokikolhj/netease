package com.shirly.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author shirly
 * @Date 2020/2/4 14:57
 * @Description servlet
 *     spring-boot 默认集成Tomcat，springmvc核心是servlet，运行在web容器
 */
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        httpServletRequest.getMethod();
        httpServletRequest.getRequestURI();
        System.out.println("我是servlet，我被调用了");
        // 可写返回
    }
}
