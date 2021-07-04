# waynboot-sso

### 项目介绍

基于SpringBoot，Shiro，Redis，Mybatis plus的多模块系统，实现了SSO单点登陆。 集成通用后台管理，新蜂商城，每日一文等多个模块，支持Shiro与SSO模块的集成，易于上手，学习，二次开发。

### 主要特性

- 项目按系统模块化，提升开发，测试效率
- 提供SSO模块，方便各个系统集成实现单点登录登出
- admin模块支持Shiro + SSO使用
- 新蜂商城包含前台和后端，后台系统支持SSO使用
- 使用hessian作为各系统间rpc通信
- 使用Mybatis-Plus作为数据层框架，代码简介高效
- 页面模板使用thymeleaf，配置灵活
- js代码简洁，清晰，避免过度封装
- 支持统一输出异常，避免繁琐的判断

### 技术选型

1. 后端
    - 核心框架：SpringBoot
    - 持久层框架：Mybatis-Plus
    - 权限控制：admin模块 -> Shiro
    - 日志管理：SLF4J > logback
    - NoSql: redis
    - Rpc: hessian
2. 前端
    - 模板选型：Thymeleaf
    - JS框架：jQuery，vew
    - 数据表格：bootstrapTable，jqGrid
    - 弹出层：layer，bootstrap-modal
    - 通知消息：Toastr
    - 树结构控件：jsTree
3. 开发平台
    - JDK版本：1.8+
    - Maven：3.5+
    - 数据库：mysql5+
    - ide：Eclipse/Idea

### 使用教程
1. 环境需要  
   jdk1.8+、mysql8.0+、redis3.0+、idea或者eclipse
2. 导入sql文件  
   创建waynboot-sso数据库，将wayn-admin模块下wayn-admin.sql导入
   创建newbee_mall_db数据库，将wayn-mall模块下newbee_mall_db.sql导入
3. 启动顺序  
   1. 必须先启动wayn-ssoserver模块下主启动类WaynSsoApplication（单点登陆服务）  
   2. 启动wayn-admin模块主启动类，打开浏览器访问http://localhost    
   3. 启动wayn-admin模块主启动类，打开浏览器访问http://localhost:84/mall
   
### 新模块接入步骤
1. pom文件引入sso-core依赖
```java
        <dependency>
            <groupId>com.wayn</groupId>
            <artifactId>wayn-ssocore</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```
2. 在yml文件配置sso服务地址
```java
wayn:
  uploadDir: E:/wayn/upload  # 上传路劲
  ssoServerUrl: http://127.0.0.1:82/ssoserver # sso服务访问路径
  currentServerUrl: http://127.0.0.1:83/others # 当前项目访问路径
  xssFilter: # 过滤器忽略路径
    excludeUrls: /**/*.js,/**/*.css,/favicon.ico,/fonts/*,/plugin/*
```
3. 添加Springboot web配置
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${wayn.ssoServerUrl}")
    private String ssoServerUrl;

    @Value("${wayn.xssFilter.excludeUrls}")
    private String excludeUrls;

    @Bean
    public FilterRegistrationBean<SsoFilter> filterRegistrationBean() {
        FilterRegistrationBean<SsoFilter> bean = new FilterRegistrationBean<>();
        SsoFilter ssoFilter = new SsoFilter();
        ssoFilter.setSsoServerUrl(ssoServerUrl);
        bean.setFilter(ssoFilter);
        bean.setDispatcherTypes(DispatcherType.REQUEST);
        bean.setName("ssoFilter");
        bean.setUrlPatterns(Collections.singletonList("/*"));
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("excludeUrls", excludeUrls);
        bean.setInitParameters(linkedHashMap);
        return bean;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new HttpsClientRequestFactory());
    }
    ...
}

```

### 内置模块

1. wayn-admin

   后台权限管理系统
2. wayn-cmomon

   后台权限系统的通用类聚集模块
3. wayn-framework

   后台权限系统的核心配置模块，包含shiro，数据源等配置
4. wayn-mall

   newbee-mall商城系统，包含前后端系统
5. wayn-others

   集成framework的爬虫模块，包含每日一文
6. wayn-ssocore

   sso单点登录的核心模块
7. wayn-ssoserver

   sso单点登录系统，供其他系统集成使用

### 单点登陆流程

登陆
![sso登陆](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/08fb9b643e444414b6fefe7b60f99a6e~tplv-k3u1fbpfcp-watermark.image)
登出
![sso登出](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/db0aaeaf124f4b3089799a46245aae26~tplv-k3u1fbpfcp-watermark.image)

### 获取源码

- [waynboot github](https://github.com/wayn111/waynboot-sso)
- [waynboot gitee](https://gitee.com/wayn111/waynboot-sso)

### 参考项目

- [AdminLTE-admin](https://gitee.com/zhougaojun/KangarooAdmin/tree/master)
- [bootdo](https://gitee.com/lcg0124/bootdo)
- [RuoYi](https://gitee.com/y_project/RuoYi)
- [NewBee-mall](https://github.com/newbee-ltd/newbee-mall)
 
