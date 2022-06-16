package com.shiro.utils;


public class WebContextFacade {
    private static ThreadLocal<RequestContext> requestContextThreadLocal = new ThreadLocal<>();

    public static RequestContext getRequestContext() {
        RequestContext requestContext = requestContextThreadLocal.get();
        return requestContext == null ? new RequestContextConcrete() : requestContext;
    }

    public static void setRequestContext(RequestContext requestContext) {
        requestContextThreadLocal.set(requestContext);
    }

    public static void removeRequestContext() {
        requestContextThreadLocal.remove();
    }
}