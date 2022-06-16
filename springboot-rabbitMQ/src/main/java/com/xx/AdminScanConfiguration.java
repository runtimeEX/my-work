package com.xx;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author : zhouhao
 * @version V1.0
 * @Project: springboot-rabbitMQ
 * @Package com.xx
 * @Description: TODO
 * @date Date : 2022年04月21日 上午11:18
 */
@Configuration
@ComponentScan(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
public class AdminScanConfiguration {
}
