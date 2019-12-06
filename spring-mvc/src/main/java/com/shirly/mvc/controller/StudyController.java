package com.shirly.mvc.controller;

import com.shirly.mvc.annotation.Controller;
import com.shirly.mvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shirly
 * @version 1.0
 * @date 2019/12/5 14:30
 */
@Controller
@RequestMapping("/study")
public class StudyController {

    @RequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        request.getSession().setAttribute("userName", userName);
        request.getSession().setAttribute("password", password);
        try {
            if ("admin".equals(userName) && "123456".equals(password)) {
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            } else {
                response.getWriter().write("404 NOT FOUND!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
