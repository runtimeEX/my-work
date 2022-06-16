package com.shiro;

import cn.hutool.extra.servlet.ServletUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shiro.utils.AesCbcUtil;
import com.shiro.utils.RequestContext;
import com.shiro.utils.SecretConstants;
import com.shiro.utils.WebContextFacade;
import com.shiro.utils.parm.ICommonParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-shiro
 * @Package com.shiro
 * @Description: TODO
 * @date Date : 2022年06月15日 下午4:35
 */
@Slf4j
public class CommonParamInterceptor implements HandlerInterceptor {
    private final ICommonParam commonParamModel;

    private final ObjectMapper objectMapper;


    public CommonParamInterceptor(ICommonParam commonParamModel, ObjectMapper objectMapper) {
        this.commonParamModel = commonParamModel;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String commonParamValue = request.getHeader("Common-Param");
        //如果header中的信息为空，获取cookie中的信息
        if (StringUtils.isEmpty(commonParamValue)) {
            Cookie userCookie = ServletUtil.getCookie(request, "Common-Param");
            if (userCookie != null) {
                commonParamValue = userCookie.getValue();
            }
        }
        //如果header中为空，获取请求参数中的
        if (StringUtils.isEmpty(commonParamValue)) {
            commonParamValue = request.getParameter("commonParam");
        }
        //如果为空，结束
        if (StringUtils.isEmpty(commonParamValue)) {
            return true;
        }
        //解密公参
        try {
            commonParamValue = AesCbcUtil.decode(commonParamValue, SecretConstants.AES_KEY_REQ, SecretConstants.AES_IV_REQ);
        } catch (Exception exception) {
            log.info("CommonParamInterceptor decode exception:{}", exception.getMessage());
            return true;
        }

        //如果当前项目中注入的对象为空，结束
        if (commonParamModel == null) {
            return true;
        }
        //获取全局请求
        RequestContext requestContext = WebContextFacade.getRequestContext();
        try {
            ICommonParam commonParam = objectMapper.readValue(commonParamValue, commonParamModel.getClass());
            requestContext.setCommonParam(commonParam);
        } catch (Exception exception) {
            log.info("CommonParamInterceptor exception:{}", exception.getMessage());
        }
        WebContextFacade.setRequestContext(requestContext);
        log.info("公参拦截器:{}", commonParamValue);
        return true;
    }
}
