package com.wayn.framework.rpc.manager;

import com.wayn.ssocore.service.UserRpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.stereotype.Component;

@Component
public class RpcExporterManager {

    // hessian导出
    @Bean("/rpc/user")
    public HessianServiceExporter userRpcServiceExporter(UserRpcService userRpcService) {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setServiceInterface(UserRpcService.class);
        exporter.setService(userRpcService);
        return exporter;
    }
}
