package com.wayn.mall.rpc;

import com.wayn.ssocore.service.UserRpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class RpcManager {

    @Value("${wayn.waynAdminUrl}")
    private String waynAdminUrl;


    @Bean
    public HessianProxyFactoryBean UserProxyFactoryBean() {
        HessianProxyFactoryBean factoryBean = new HessianProxyFactoryBean();
        factoryBean.setServiceUrl(waynAdminUrl + "/rpc/user");
        factoryBean.setServiceInterface(UserRpcService.class);
        return factoryBean;
    }
}
