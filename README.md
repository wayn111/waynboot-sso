# waynboot-sso

### 项目介绍
基于SpringBoot，Shiro，Redis，Mybatis-Plus，SSO的多模块系统，包含了SSO单点登陆，
通用后台管理，NewBee-mall商城，每日一文等多个模块，支持Shiro与SSO模块的集成，易于上手，学习，二次开发。

#### 主要特性
- 项目按系统模块化，提升开发，测试效率
- ssoserver为SSO模块，支持单点登录登出
- admin模块支持Shiro + SSO使用
- mall模块包含前台和后端，后端支持SSO使用
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
 
### 内置模块
1. wayn-admin
    > 后台权限管理系统
2. wayn-cmomon
    > 后台权限系统的通用类聚集模块
3. wayn-framework
    > 后台权限系统的核心配置模块，包含shiro，数据源等配置
4. wayn-mall
    > newbee-mall商城系统，包含前后太系统
5. wayn-others
    > 集成framework的others模块，包含每日一文
6. wayn-ssocore
    > sso单点登录的核心模块
7. wayn-ssoserver
    > sso单点登录系统，共其他系统集成使用

### 获取源码
- [waynboot github](https://github.com/wayn111/waynboot-sso)
- [waynboot gitee](https://gitee.com/wayn111/waynboot-sso)

### 参考项目
- [AdminLTE-admin](https://gitee.com/zhougaojun/KangarooAdmin/tree/master)
- [bootdo](https://gitee.com/lcg0124/bootdo)
- [RuoYi](https://gitee.com/y_project/RuoYi)
- [NewBee-mall](https://github.com/newbee-ltd/newbee-mall)
