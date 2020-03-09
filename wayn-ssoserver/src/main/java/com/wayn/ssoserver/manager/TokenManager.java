package com.wayn.ssoserver.manager;

import com.wayn.ssocore.entity.SsoUser;
import com.wayn.ssoserver.config.RedisOpts;
import com.wayn.ssoserver.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenManager {

    @Autowired
    private RedisOpts redisOpts;

    public boolean validateAndRefresh(String token) {
        String value = redisOpts.get(token);
        boolean b = StringUtils.isNotEmpty(value);
        if (b) redisOpts.set(token, value);
        return b;
    }

    public void addToken(String token, SsoUser user) throws Exception {
        redisOpts.set(token, JsonUtil.marshal(user));
    }

    public void removeToken(String token) {
        redisOpts.del(token);
    }

    public SsoUser getUserByToken(String token) throws Exception {
        String value = redisOpts.get(token);
        if (StringUtils.isEmpty(value)) return null;
        return JsonUtil.unmarshal(value, SsoUser.class);
    }

    public String generateToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
