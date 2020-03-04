package com.wayn.common.service;

import java.util.List;

import com.wayn.common.domain.User;
import com.wayn.common.domain.UserOnline;

public interface UserOnlineService {


    List<UserOnline> list();

    List<User> listUser();

    void forceLogout(String sessionId);
}
