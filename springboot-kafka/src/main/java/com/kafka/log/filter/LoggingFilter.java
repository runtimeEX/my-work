package com.kafka.log.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-kafka
 * @Package com.kafka.log.filter
 * @Description: TODO
 * @date Date : 2021年09月27日 上午9:21
 */

@Slf4j
public class LoggingFilter implements Filter {
    /**
     * 链路id
     */
    private static String TRACE_ID = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            String traceId = java.util.UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
            MDC.put(TRACE_ID, traceId);
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID);
        }
    }
}
