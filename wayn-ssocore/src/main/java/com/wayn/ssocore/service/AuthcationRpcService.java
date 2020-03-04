package com.wayn.ssocore.service;


import com.wayn.ssocore.entity.SsoUser;

public interface AuthcationRpcService {

    boolean validateAndRefresh(String token);

    SsoUser findUserByToken(String token) throws Exception;
}
