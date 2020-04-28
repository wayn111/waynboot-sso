# wayn-admin

### 模块介绍
基于Spring,Shiro,Redis,Mybatis的通用后台权限管理系统，并且集成了消息通知，任务调度，代码生成，文件管理等常用功能，易于上手，学习，使用二次开发。

#### 主要特性
- 项目按功能模块化，提升开发，测试效率
- 支持多数据源操作
- 集成redis
- 支持ip2regon ip本地化
- 集成elfinder文件管理
- 集成日志切面，方便日志记录
- 前端js代码简洁，清晰，避免过度封装
- 支持统一输出异常，避免繁琐的判断

### 技术选型
1. 后端
    - 核心框架：Spring
    - 控制层框架：SpringMVC
    - 权限控制：Shiro
    - 持久层框架：Mybatis-Plus
    - 日志管理：SLF4J > logback
    - 缓存控制：Redis
    - 环境控制：使用spring profile可根据`System/JVM`参数灵活切换配置文件
2. 前端
    - 模板引擎：Thymeleaf
    - 管理模板：H+
    - JS框架：jQuery
    - 数据表格：bootstrapTable
    - 弹出层：layer
    - 树结构控件：jsTree
    - checkbox选择控件：bootstrapSwitch
3. 开发平台
    - JDK版本：1.8+
    - Maven：3.5+
    - 数据库：mysql5+
    - ide：Eclipse/Idea
 
### 内置模块
1. 系统管理
    - 用户管理：系统操作者，可绑定多角色
    - 角色管理：菜单权限携带者，可配置到按钮级权限
    - 菜单管理：配置系统目录，菜单链接，操作权限
    - 部门管理：用户所属部门
    - 日志操作：记录用户操作，包含请求参数
 
### 启动顺序
1. 启动wayn-ssoserver下的WaynSsoApplication
2. 启动wayn-admin下的WaynAdminApplication
3. 启动wayn-others下的WaynOthersApplication
4. 启动wayn-mall下的WaynMallApplication

### 获取源码
- [wayn-admin 码云](https://gitee.com/wayn111/crowdfounding)
- [wayn-admin github](https://github.com/wayn111/crowdfounding)

### 参考项目
- [AdminLTE-admin](https://gitee.com/zhougaojun/KangarooAdmin/tree/master)
- [bootdo](https://gitee.com/lcg0124/bootdo)
- [RuoYi](https://gitee.com/y_project/RuoYi)

### 实例截图
__系统登陆__
![输入图片说明](./images/系统登陆.png "系统登陆.png")
__首页__
![输入图片说明](./images/首页.png "首页.png")
__用户管理__
![输入图片说明](./images/用户管理.png "用户管理.png")
__添加角色__
![输入图片说明](./images/添加角色.png "添加角色.png")
__菜单管理__
![输入图片说明](./images/菜单管理.png "菜单管理.png")
