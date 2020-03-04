package com.wayn.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true, proxyTargetClass = true)
// 指定要扫描的Mapper类的包的路径
@MapperScan("com.wayn.**.dao")
public class ApplicationConfig {
}
