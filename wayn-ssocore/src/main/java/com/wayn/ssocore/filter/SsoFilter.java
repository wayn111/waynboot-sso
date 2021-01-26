package com.wayn.ssocore.filter;

import com.caucho.hessian.client.HessianProxyFactory;
import com.wayn.ssocore.entity.SessionUser;
import com.wayn.ssocore.entity.SsoUser;
import com.wayn.ssocore.service.AuthcationRpcService;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Objects;

/**
 * sso系统接入过滤器
 */
@Data
public class SsoFilter implements Filter {

    private static final String ADMIN_USER = "adminUser";
    private String ssoServerUrl;
    private String[] excludeUrls;
    private boolean isSsoServer = false;
    private AuthcationRpcService authcationRpcService;
    private AntPathMatcher antPathMatcher = new AntPathMatcher(File.separator);

    public static void main(String[] args) {
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        if (antPathMatcher.match("/**/*.js", "/admin/plugins/jquery/jquery.min.js")) {
            System.out.println(true);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) {
        if (isSsoServer) {
            ssoServerUrl = filterConfig.getServletContext().getContextPath();
        }
        if (StringUtils.isEmpty(ssoServerUrl) && !isSsoServer) {
            throw new RuntimeException("客户端ssoServerUrl参数不能为空！");
        }
        String excludeUrl = filterConfig.getInitParameter("excludeUrls");
        excludeUrls = excludeUrl == null ? new String[]{} : excludeUrl.split(",");

        if (Objects.isNull(authcationRpcService)) {
            try {
                authcationRpcService = (AuthcationRpcService) new HessianProxyFactory().create(AuthcationRpcService.class,
                        ssoServerUrl + "/rpc/authcationRpcService");
            } catch (MalformedURLException e) {
                e.printStackTrace();
                throw new RuntimeException("authcationRpcService初始化失败！");
            }
        }
    }

    @SneakyThrows(Exception.class)
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (handleExcludeURL(req, resp)) {
            chain.doFilter(request, response);
            return;
        }
        HttpSession session = req.getSession();
        SessionUser sessionUser = Objects.isNull(session) ? null : (SessionUser) session.getAttribute(ADMIN_USER);
        String token = (sessionUser == null) ? null : sessionUser.getToken();
        if (StringUtils.isEmpty(token)) {
            // 从parameter和cookie中查询token参数
            if (!StringUtils.isEmpty(token = req.getParameter("token"))) {
                SsoUser user = authcationRpcService.findUserByToken(token);
                SessionUser sessionUser1 = new SessionUser();
                sessionUser1.setToken(token);
                sessionUser1.setUser(user);
                session.setAttribute(ADMIN_USER, sessionUser1);
                String backUrl = getBackUrl(req);
                backUrl = backUrl.substring(0, backUrl.indexOf("token") - 1);
                // 重跳转当前url，去除token参数
                resp.sendRedirect(backUrl);
                return;
            }
        } else {
            if (authcationRpcService.validateAndRefresh(token)) {
                chain.doFilter(request, response);
                return;
            }
            session.removeAttribute(ADMIN_USER);
        }
        resp.sendRedirect(ssoServerUrl + "/login?" + "backUrl=" + URLEncoder.encode(getBackUrl(req), "UTF-8"));
    }

    @Override
    public void destroy() {

    }

    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {
        if (excludeUrls == null || excludeUrls.length == 0) {
            return false;
        }
        String uri = request.getServletPath();
        for (String pattern : excludeUrls) {
            if (antPathMatcher.match(pattern, uri)) {
                return true;
            }
        }
        System.out.println("ssoFilter failed:" + uri);
        return false;
    }

    public String getBackUrl(HttpServletRequest req) {
        return req.getRequestURL() +
                (req.getQueryString() == null ? "" : "?" + req.getQueryString());
    }
}
