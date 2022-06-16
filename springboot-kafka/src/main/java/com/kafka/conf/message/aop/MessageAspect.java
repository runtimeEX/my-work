
package com.kafka.conf.message.aop;

import com.kafka.conf.message.LocaleService;
import com.kafka.response.Response;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class MessageAspect {

    protected final LocaleService localeService;

    public MessageAspect(LocaleService localeService) {
        this.localeService = localeService;
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void aspectRequestMapping() {

    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void aspectPostMapping() {

    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void aspectGetMapping() {

    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.ResponseStatus)")
    public void aspectResponseStatus() {

    }
    /**
     * message转换方法，针对参数校验message和普通message
     *
     * @param messageCode 待转换message code
     * @return String 国际化转换后的message
     */
    private String convertMessage(String messageCode) {
        if (StringUtils.isNotEmpty(messageCode)) {
            String[] codeArray = StringUtils.split(messageCode, " ");
            if (codeArray != null && codeArray.length > 0) {
                for (String s : codeArray) {
                    String str = localeService.getMessage(s, null, null);
                    if (StringUtils.isNotEmpty(str)) {
                        messageCode = messageCode.replace(s, str);
                    }
                }
                return messageCode;
            }
            return localeService.getMessage(messageCode, null, null);
        }
        return null;
    }

    @Around("aspectRequestMapping() || aspectPostMapping() || aspectGetMapping() || aspectResponseStatus()")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        Object object = joinPoint.proceed();
        if (object instanceof Response) {
            Response response = (Response) object;
            String message = this.convertMessage(response.getMessage());
            if (StringUtils.isNotEmpty(message)) {
                response.setMessage(message);
            }
            object = response;
        }
        return object;
    }

}

