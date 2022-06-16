package com.shiro;

import com.shiro.utils.NetworkUtil;
import com.shiro.utils.RequestContext;
import com.shiro.utils.WebContextFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro
 * @Description: TODO
 * @date Date : 2022年06月15日 上午9:27
 */
@Slf4j
public class ContextInterceptor implements HandlerInterceptor {

    private void setRequestContext(HttpServletRequest request) {
        RequestContext requestContext = WebContextFacade.getRequestContext();
        requestContext.setIp(NetworkUtil.getIp(request));
        requestContext.setUri(request.getRequestURI());
        WebContextFacade.setRequestContext(requestContext);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        WebContextFacade.removeRequestContext();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            String name = handlerMethod.getMethod().getName();
            log.info("---------方法名:{}",name);
            boolean hasRestController = handlerMethod.getBeanType().isAnnotationPresent(RestController.class);
            if (hasRestController) {
                RequestContext requestContext = WebContextFacade.getRequestContext();
                requestContext.setApiRequest(hasRestController);
                WebContextFacade.setRequestContext(requestContext);
            }
        }
        return true;
    }
}