package com.wayn.others.config;

import com.wayn.ssocore.filter.SsoFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
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

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean bean = new FilterRegistrationBean();
        SsoFilter ssoFilter = new SsoFilter();
        ssoFilter.setSsoServerUrl(ssoServerUrl);
        bean.setFilter(ssoFilter);
        bean.setDispatcherTypes(DispatcherType.REQUEST);
        bean.setName("ssoFilter");
        bean.setUrlPatterns(Arrays.asList("/*"));
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put("excludeUrls", excludeUrls);
        bean.setInitParameters(linkedHashMap);
        return bean;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(new HttpsClientRequestFactory());
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/others/article");
    }
}
