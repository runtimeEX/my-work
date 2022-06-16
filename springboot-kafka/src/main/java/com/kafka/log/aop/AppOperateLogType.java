package com.kafka.log.aop;

/**
 * 操作类型 QUERY 查询 ADD 添加 UPDATE 修改 DELETE 删除 IMPORT 导入 EXPORT 导出
 */
public interface AppOperateLogType {
    String QUERY = "QUERY";
    String ADD = "ADD";
    String UPDATE = "UPDATE";
    String DELETE = "DELETE";
    String IMPORT = "IMPORT";
    String EXPORT = "EXPORT";
}