package com.wayn.framework.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.EventListener;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean delegatingFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy filterProxy = new DelegatingFilterProxy();
        filterProxy.setTargetBeanName("shiroFilter");
        filterProxy.setTargetFilterLifecycle(true);
        registrationBean.setUrlPatterns(Collections.singleton("/**"));
        registrationBean.setFilter(filterProxy);
        return registrationBean;
    }

    @Bean
    public ServletListenerRegistrationBean<EventListener> getDemoListener() {
        ServletListenerRegistrationBean<EventListener> registrationBean
                = new ServletListenerRegistrationBean<>();
        registrationBean.setListener(myRequestContextListener());
        registrationBean.setOrder(1);
        return registrationBean;
    }

    public MyRequestContextListener myRequestContextListener() {
        return new MyRequestContextListener();
    }
}
