package com.github.opensharing.framework.springboot.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * FirstServlet
 *
 * 3.1 配置方式一 通过注解@WebServlet 配置
 *
 * @author jwen
 * Date 2020-09-09
 * 多个url
 * @WebServlet(name = "FfrstServlet", urlPatterns = {"", ""})
 */
@WebServlet(name = "FirstServlet", urlPatterns = "/first")
public class FirstServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("FirstServlet....");
    }
}
