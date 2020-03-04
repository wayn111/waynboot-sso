package com.wayn.ssocore.service;

import com.wayn.ssocore.entity.SsoUser;

public interface UserRpcService {

    SsoUser loginValidate(String username,String password);
}
