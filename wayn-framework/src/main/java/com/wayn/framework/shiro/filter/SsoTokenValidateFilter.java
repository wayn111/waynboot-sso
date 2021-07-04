package com.wayn.framework.shiro.filter;

import com.wayn.common.util.http.HttpUtil;
import com.wayn.ssocore.service.AuthcationRpcService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * SSO与Shiro整合核心类
 */
public class SsoTokenValidateFilter extends AccessControlFilter {

    private AuthcationRpcService authcationRpcService;

    public void setAuthcationRpcService(AuthcationRpcService authcationRpcService) {
        this.authcationRpcService = authcationRpcService;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String token = HttpUtil.getTokenByCookie((HttpServletRequest) request);
        if (StringUtils.isNotEmpty(token)) {
            return authcationRpcService.validateAndRefresh(token);
        }
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        Subject subject = getSubject(request, response);
        if (subject != null) {
            subject.logout();
        }
        saveRequestAndRedirectToLogin(request, response);
        return false;
    }
}
