package com.wayn.ssoserver.service.impl;

import com.wayn.ssocore.entity.SsoUser;
import com.wayn.ssocore.service.AuthcationRpcService;
import com.wayn.ssoserver.manager.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthcationRpcServiceImpl implements AuthcationRpcService {

    @Autowired
    private TokenManager tokenManager;

    @Override
    public boolean validateAndRefresh(String token) {
        return tokenManager.validateAndRefresh(token);
    }

    @Override
    public SsoUser findUserByToken(String token) throws Exception {
        return tokenManager.getUserByToken(token);
    }

}
