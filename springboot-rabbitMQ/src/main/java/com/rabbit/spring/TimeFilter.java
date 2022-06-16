package com.rabbit.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 过滤器
 */
@Component
public class TimeFilter implements Filter {
    private final static Logger log = LoggerFactory.getLogger(TimeFilter.class);
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    log.info("time filter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String requestURI = String.valueOf(req.getRequestURL());

        log.info("time filter start");
        long startTime = System.currentTimeMillis();
        if (requestURI.contains("favicon.ico")) {
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
        log.info(requestURI);
        long endTime = System.currentTimeMillis();
        log.info("time filter consume " + (endTime - startTime) + " ms");
        log.info("time filter end");
    }

    @Override
    public void destroy() {
        log.info("time filter destroy");
    }
}
