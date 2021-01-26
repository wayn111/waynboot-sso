package com.wayn.framework.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.caucho.hessian.client.HessianProxyFactory;
import com.wayn.common.service.DeptService;
import com.wayn.framework.redis.RedisOpts;
import com.wayn.framework.shiro.cache.RedisCacheManager;
import com.wayn.framework.shiro.credentials.MyCredentialsMatcher;
import com.wayn.framework.shiro.filter.OnlineSessionFilter;
import com.wayn.framework.shiro.filter.TokenLoginFilter;
import com.wayn.framework.shiro.filter.TokenValidateFilter;
import com.wayn.framework.shiro.realm.SsoRealm;
import com.wayn.framework.shiro.session.OnlineSessionFactory;
import com.wayn.framework.shiro.session.RedisSessionDAO;
import com.wayn.ssocore.service.AuthcationRpcService;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.cache.RedisCache;

import javax.servlet.Filter;
import java.net.MalformedURLException;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Value("${shiro.session-timeout}")
    private int sessionTimeout;
    @Value("${shiro.retryCount}")
    private int retryCount;
    @Value("${shiro.algorithmName}")
    private String algorithmName;
    @Value("${shiro.iterations}")
    private int iterations;
    @Value("${shiro.loginUrl}")
    private String loginUrl;
    @Value("${shiro.successUrl}")
    private String successUrl;
    @Value("${shiro.unauthorizedUrl}")
    private String unauthorizedUrl;

    @Value("${wayn.ssoServerUrl}")
    private String ssoServerUrl;

    @Value("${wayn.currentServerUrl}")
    private String currentServerUrl;

    @Autowired
    private RedisOpts opts;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private DeptService deptService;

    @Bean
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        shiroFilterFactoryBean.setSuccessUrl(successUrl);
        shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
        // 定义自己的过滤器
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("tokenLogin", tokenLoginFilter());
        filters.put("logout", logoutFilter());
        filters.put("tokenValidate", tokenValidateFilter());
        filters.put("onlineSession", onlineSessionFilter());
        shiroFilterFactoryBean.setFilters(filters);
        // 定义拦过滤器链
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        filterChainDefinitionMap.put("/upload/**", "anon");
        filterChainDefinitionMap.put("/plugin/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/login", "tokenLogin");
        filterChainDefinitionMap.put("/rpc/**", "anon");
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/**", "authc,tokenValidate,onlineSession");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    public OnlineSessionFilter onlineSessionFilter() {
        OnlineSessionFilter onlineSessionFilter = new OnlineSessionFilter();
        onlineSessionFilter.setForceLogoutUrl(loginUrl);
        onlineSessionFilter.setSessionDAO(sessionDAO());
        onlineSessionFilter.setDeptService(deptService);
        return onlineSessionFilter;
    }

    public LogoutFilter logoutFilter() {
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setRedirectUrl(ssoServerUrl + "/logout?backUrl=" + currentServerUrl);
        return logoutFilter;
    }

    public TokenLoginFilter tokenLoginFilter() {
        TokenLoginFilter tokenValidateFilter = new TokenLoginFilter();
        tokenValidateFilter.setFailureUrl(loginUrl);
        tokenValidateFilter.setSuccessUrl(successUrl);
        return tokenValidateFilter;
    }

    public TokenValidateFilter tokenValidateFilter() {
        TokenValidateFilter tokenValidateFilter = new TokenValidateFilter();
        try {
            AuthcationRpcService authcationRpcService = (AuthcationRpcService) new HessianProxyFactory().create(AuthcationRpcService.class,
                    ssoServerUrl + "/rpc/authcationRpcService");
            tokenValidateFilter.setAuthcationRpcService(authcationRpcService);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("tokenValidateFilter:authcationRpcService初始化失败！");
        }
        return tokenValidateFilter;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm
        securityManager.setRealm(ssoRealm());
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(rediscacheManager());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    @Bean
    SsoRealm ssoRealm() {
        SsoRealm ssoRealm = new SsoRealm();
        ssoRealm.setSsoServerUrl(ssoServerUrl);
        // 定义自己的的密码验证服务
        MyCredentialsMatcher credentialsMatcher = new MyCredentialsMatcher();
        Cache passwordRetryCache = cacheManager.getCache("passwordRetryCache");
        RedisCache targetCache = (RedisCache) ((TransactionAwareCacheDecorator) passwordRetryCache).getTargetCache();
        credentialsMatcher.setPasswordRetryCache(targetCache);
        credentialsMatcher.setRetryCount(retryCount);
        credentialsMatcher.setHashAlgorithmName(algorithmName);
        credentialsMatcher.setHashIterations(iterations);
        ssoRealm.setCredentialsMatcher(credentialsMatcher);
        return ssoRealm;
    }

    /**
     * 启动shiro注解
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     */
    private RedisCacheManager rediscacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setOpts(opts);
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    private RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setTimeOut(sessionTimeout);
        return redisSessionDAO;
    }


    @Bean
    public SessionDAO sessionDAO() {
        return redisSessionDAO();

    }

    public SessionFactory sessionFactory() {
        return new OnlineSessionFactory();
    }

    /**
     * shiro session的管理
     */
    @Bean
    public DefaultWebSessionManager sessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 设置cookie
        SimpleCookie sessionIdCookie = new SimpleCookie();
        sessionIdCookie.setName("wayn-session-id");
        sessionManager.setSessionIdCookie(sessionIdCookie);
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        // 设置sessionValidation任务执行周期时间
        sessionManager.setSessionValidationInterval(15 * 60 * 1000);
        // 设置全局session超时时间
        sessionManager.setGlobalSessionTimeout(sessionTimeout * 1000);
        // 设置sessionDao实现
        sessionManager.setSessionDAO(sessionDAO());
        sessionManager.setSessionFactory(sessionFactory());
        return sessionManager;
    }

    /**
     * ShiroDialect，为了在thymeleaf里使用shiro的标签的bean
     *
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

}
