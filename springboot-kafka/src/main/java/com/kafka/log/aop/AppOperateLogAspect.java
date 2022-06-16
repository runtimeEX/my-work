package com.kafka.log.aop;

import com.kafka.NetworkUtil;
import com.kafka.log.AppOperateLogMessage;
import com.kafka.log.message.SendAppOperateLogMessage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.CharBuffer;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Aspect
@Data
public class AppOperateLogAspect {
    public static final int DEFAULT_BUFFER_SIZE = 2 << 12;

    private String appName;

    /**
     * 操作日志模式，GLOBAL 所有的都记录日志， PART 只有使用注解的日志
     */
    private String model;

    /**
     * 该设置路径地址，都不记录任何操作日志, 如果路径里包含 注解，则还是会用注解
     */
    private Set<String> excludeUri;

    @Autowired
    private SendAppOperateLogMessage sendAppOperateLogMessage;

    public AppOperateLogAspect(String appName, String model, Set<String> excludeUri) {
        this.appName = appName;
        this.model = model;
        this.excludeUri = excludeUri;
    }

    /**
     *
     */
    // @Pointcut("@annotation(AppOperateLog) || @annotation(org.apache.shiro.authz.annotation.RequiresPermissions)")
    @Pointcut("@annotation(AppOperateLog)")
    public void appOperateLog() {
        log.debug("appOperateLog");

    }

    /**
     *
     */
    @Before("appOperateLog()")
    public void beforeAppOperateLog(JoinPoint joinPoint) {
        log.debug("beforeAppOperateLog");
    }

    /**
     *
     */
    @After(value = "appOperateLog()")
    public void afterAppOperateLog(JoinPoint joinPoint) {
        log.debug("afterAppOperateLog");

        try {
            this.sendOperateLog(joinPoint);
        } catch (Exception exception) {
            log.error("记录操作日志错误");
            exception.printStackTrace();
        }

    }

    private void sendOperateLog(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = attributes.getRequest();

        String requestURI = httpServletRequest.getRequestURI();
        String requestURL = httpServletRequest.getRequestURL().toString();
        String ipAddress = NetworkUtil.getIp(httpServletRequest);
        //todo
        httpServletRequest.getQueryString();
        String read = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream()));
            //  read = IoUtil.read(reader);
            final StringBuilder builder = new StringBuilder();
            final CharBuffer buffer = CharBuffer.allocate(DEFAULT_BUFFER_SIZE);
            try {
                while (-1 != reader.read(buffer)) {
                    builder.append(buffer.flip().toString());
                }
            } catch (IOException e) {
                throw new Exception(e);
            }
            read = builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("请求 body {}", read);
        if (excludeUri.contains(requestURI)) {
            return;
        }

        // 获取方法名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        AppOperateLog appOperateLog = method.getAnnotation(AppOperateLog.class);

        //   RequiresPermissions requiresPermissions = method.getAnnotation(RequiresPermissions.class);

        AppOperateLogMessage.AppOperateLogMessageBuilder appOperateLogMessageBuilder = AppOperateLogMessage.builder()
                .appName(appName)
                .requestUri(requestURI)
                .requestUrl(requestURL)
                .ipAddress(ipAddress)
                .operateUserId(1l)
                .operateDate(LocalDateTime.now())
                // .permissions(requiresPermissions == null || requiresPermissions.value().length <= 0 ? Sets.newHashSet() : Sets.newHashSet(requiresPermissions.value()))
                .className(methodSignature.getDeclaringTypeName())
                .methodName(method.getName());

        if ("GLOBAL".equalsIgnoreCase(model)) {
            // 全局模式下，匹配 RequiresPermissions的注解，如果存在则会记录，
            // 再如果存在 AppOperateLog 则会使用，该注解设置
            if (appOperateLog != null) {
                appOperateLogMessageBuilder
                        .operateType(appOperateLog.operateType())
                        .logTitle(appOperateLog.logTitle());
            }
            sendAppOperateLogMessage.handlerMessage(appOperateLogMessageBuilder.build());

        } else if ("PART".equalsIgnoreCase(model)) {
            // 部分模式下，只使用 AppOperateLog 注解
            if (appOperateLog != null) {
                appOperateLogMessageBuilder
                        .operateType(appOperateLog.operateType())
                        .logTitle(appOperateLog.logTitle());

                sendAppOperateLogMessage.handlerMessage(appOperateLogMessageBuilder.build());
            }
        }

    }
}