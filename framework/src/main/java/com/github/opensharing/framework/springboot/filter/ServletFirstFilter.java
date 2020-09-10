package com.github.opensharing.framework.springboot.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

/**
 * 自定义ServletFilter
 *
 * @author wenzongwei
 * Date 2020-09-10
 *
 * 3.1 配置方式一
 *
 * @WebFilter(filterName = "ServletFirstFilter", urlPatterns = {"/first", "*.do"})
 */
@WebFilter(filterName = "ServletFirstFilter", urlPatterns = "/first")
public class ServletFirstFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("doing before ServletFirstFilter......");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("doing after ServletFirstFilter......");
    }
}
