package com.wayn.ssoserver.manager;

import com.caucho.hessian.client.HessianProxyFactory;
import com.wayn.ssocore.entity.SsoUser;
import com.wayn.ssocore.service.AuthcationRpcService;
import com.wayn.ssocore.service.UserRpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.caucho.HessianProxyFactoryBean;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

@Component
public class RpcManager {

    @Value("${sso.waynAdminUrl}")
    private String waynAdminUrl;

    public static void main(String[] args) throws MalformedURLException {
        UserRpcService o = (UserRpcService) new HessianProxyFactory().create(UserRpcService.class,
                "http://127.0.0.1/wayn/rpc/user");
        SsoUser ssoUser = o.loginValidate("admin", "123456");
        System.out.println(ssoUser);
    }

    @Bean(name = "/rpc/authcationRpcService")
    public HessianServiceExporter authcationRpcService(AuthcationRpcService authcationRpcService) {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setServiceInterface(AuthcationRpcService.class);
        exporter.setService(authcationRpcService);
        return exporter;
    }

    @Bean
    public HessianProxyFactoryBean UserProxyFactoryBean() {
        HessianProxyFactoryBean factoryBean = new HessianProxyFactoryBean();
        factoryBean.setServiceUrl(waynAdminUrl + "/rpc/user");
        factoryBean.setServiceInterface(UserRpcService.class);
        return factoryBean;
    }
}
