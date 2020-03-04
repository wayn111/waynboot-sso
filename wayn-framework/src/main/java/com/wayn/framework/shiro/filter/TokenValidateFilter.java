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

public class TokenValidateFilter extends AccessControlFilter {

    private AuthcationRpcService authcationRpcService;

    public void setAuthcationRpcService(AuthcationRpcService authcationRpcService) {
        this.authcationRpcService = authcationRpcService;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String token = HttpUtil.getValueByCookie((HttpServletRequest) request);
        if (StringUtils.isNotEmpty(token)) {
            if (!authcationRpcService.validateAndRefresh(token)) {
                return false;
            }
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
