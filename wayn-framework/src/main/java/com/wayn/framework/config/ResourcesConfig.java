package com.wayn.framework.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourcesConfig implements WebMvcConfigurer {

    @Value("${wayn.uploadDir}")
    private String uploadDir;


    @Override

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 本地文件上传路径 */
        registry.addResourceHandler("/upload/**").addResourceLocations("file:" + uploadDir + "/");
    }
}
