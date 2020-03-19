package com.wayn.mall.config;

import com.wayn.mall.constant.Constants;
import com.wayn.mall.intercepter.MallLoginValidateIntercepter;
import com.wayn.mall.intercepter.MallShopCartNumberInterceptor;
import com.wayn.ssocore.filter.SsoFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import java.util.Arrays;
import java.util.LinkedHashMap;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${wayn.ssoServerUrl}")
    private String ssoServerUrl;

    @Value("${wayn.xssFilter.excludeUrls}")
    private String excludeUrls;

    @Value("${wayn.uploadDir}")
    private String uploadDir;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        SsoFilter ssoFilter = new SsoFilter();
        ssoFilter.setSsoServerUrl(ssoServerUrl);
        bean.setFilter(ssoFilter);
        bean.setDispatcherTypes(DispatcherType.REQUEST);
        bean.setName("ssoFilter");
        bean.setUrlPatterns(Arrays.asList("/admin/*"));
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("excludeUrls", excludeUrls);
        bean.setInitParameters(linkedHashMap);
        return bean;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/index");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 本地文件上传路径 */
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + uploadDir + "/");
        registry.addResourceHandler("/goods-img/**").addResourceLocations("file:" + Constants.FILE_UPLOAD_DIC);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MallLoginValidateIntercepter())
                .excludePathPatterns("/login")
                .excludePathPatterns("/logout")
                .excludePathPatterns("/register")
                .excludePathPatterns("/upload/**")
                .excludePathPatterns("/goods-img/**")
                .excludePathPatterns("/common/**")
                .excludePathPatterns("/mall/**")
                .excludePathPatterns("/admin/**");

        // 购物车中的数量统一处理
        registry.addInterceptor(mallShopCartNumberInterceptor())
                .excludePathPatterns("/admin/**")
                .excludePathPatterns("/register")
                .excludePathPatterns("/login")
                .excludePathPatterns("/logout");

        // 添加一个拦截器，拦截以/admin为前缀的url路径（后台登陆拦截）
        /*registry.addInterceptor(new AdminLoginInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/dist/**")
                .excludePathPatterns("/admin/plugins/**");*/
    }

    @Bean
    public MallShopCartNumberInterceptor mallShopCartNumberInterceptor() {
        return new MallShopCartNumberInterceptor();
    }
}
