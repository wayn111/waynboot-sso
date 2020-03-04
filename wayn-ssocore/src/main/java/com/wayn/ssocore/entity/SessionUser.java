package com.wayn.ssocore.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class SessionUser implements Serializable {

    private static final long serialVersionUID = 1103246763829813386L;
    private String token;
    private SsoUser user;
}
