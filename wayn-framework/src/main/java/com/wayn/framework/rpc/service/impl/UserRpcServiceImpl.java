package com.wayn.framework.rpc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wayn.common.domain.User;
import com.wayn.common.service.UserService;
import com.wayn.ssocore.entity.SsoUser;
import com.wayn.ssocore.service.UserRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserRpcServiceImpl implements UserRpcService {


    @Autowired
    private UserService userService;

    @Override
    public SsoUser loginValidate(String username, String password) {
        User user = userService.getOne(new QueryWrapper<User>().eq("userName", username).eq("password", password));
        if (Objects.isNull(user)) {
            return null;
        }
        SsoUser ssoUser = new SsoUser();
        ssoUser.setId(user.getId());
        ssoUser.setUserName(user.getUserName());
        return ssoUser;
    }
}
