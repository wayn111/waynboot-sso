package com.wayn.ssocore.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SsoUser implements Serializable {

    private static final long serialVersionUID = -4564540297268973965L;

    private String id;

    private String userName;

    private String password;

}
