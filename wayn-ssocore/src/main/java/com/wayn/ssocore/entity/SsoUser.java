package com.wayn.ssocore.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SsoUser implements Serializable {

    private static final long serialVersionUID = -4564540297268973965L;

    private String id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像
     */
    private String userImg;

    /**
     * 手机
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

}
