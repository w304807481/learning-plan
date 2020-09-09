package com.github.opensharing.framework.springboot.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * HelloServlet
 *
 * @author wenzongwei
 * Date 2020-09-09
 * <p>
 * 配置方式一
 * <p>
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
