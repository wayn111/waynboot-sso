package com.wayn.framework.shiro.token;

import org.apache.shiro.authc.UsernamePasswordToken;

public class SsoToken extends UsernamePasswordToken {
    private String token;

    public SsoToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public SsoToken setToken(String token) {
        this.token = token;
        return this;
    }
}