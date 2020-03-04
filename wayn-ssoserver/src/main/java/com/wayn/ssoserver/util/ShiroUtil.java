package com.wayn.ssoserver.util;

import org.apache.shiro.crypto.hash.SimpleHash;

public class ShiroUtil {
    /**
     * 密码加密
     *
     * @param password
     * @param salt
     * @return
     */
    public static String md5encrypt(String password, Object salt) {
        return new SimpleHash("MD5", password, salt, 1024).toString();
    }
}
