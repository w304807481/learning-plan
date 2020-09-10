package com.github.opensharing.framework.springboot.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义ServletFilter
 *
 * @author wenzongwei
 * Date 2020-09-10
 * @WebFilter(filterName = "ServletSecondFilter", urlPatterns = {"/first", "*.do"})
 */
public class ServletSecondFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("doing before ServletSecondFilter......");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("doing after ServletSecondFilter......");
    }
}
