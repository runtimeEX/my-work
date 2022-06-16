package com.kafka.log.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface AppOperateLog {

    /**
     * 日志标题
     */
    String logTitle() default "";

    /**
     * 操作类型 QUERY查询 ADD添加 UPDATE修改 DELETE删除 IMPORT导入 EXPORT导出
     */
    String operateType() default AppOperateLogType.QUERY;
}
