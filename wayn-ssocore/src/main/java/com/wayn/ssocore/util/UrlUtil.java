package com.wayn.ssocore.util;

import javax.servlet.http.HttpServletRequest;

public class UrlUtil {

    public static String getBackUrl(HttpServletRequest req) {
        return new StringBuilder().append(req.getRequestURL())
                .append(req.getQueryString() == null ? "" : "?" + req.getQueryString()).toString();
    }
}
