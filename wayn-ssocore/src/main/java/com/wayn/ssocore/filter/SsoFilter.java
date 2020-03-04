package com.wayn.ssocore.filter;

import com.caucho.hessian.client.HessianProxyFactory;
import com.wayn.ssocore.entity.SessionUser;
import com.wayn.ssocore.entity.SsoUser;
import com.wayn.ssocore.service.AuthcationRpcService;
import com.wayn.ssocore.util.UrlUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.Objects;


@Data
public class SsoFilter implements Filter {

    private String ssoServerUrl;

    private String[] excludeUrls;

    private boolean isSsoServer = false;

    private AuthcationRpcService authcationRpcService;

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
        for (String s : excludeUrls) {
            if (req.getServletPath().contains(s)) {
                chain.doFilter(request, response);
                return;
            }
        }
        HttpSession session = req.getSession();
        SessionUser sessionUser = Objects.isNull(session) ? null : (SessionUser) session.getAttribute("sessionUser");
        String token = (sessionUser == null) ? null : sessionUser.getToken();
        if (StringUtils.isEmpty(token)) {
            // 从parameter和cookie中查询token参数
            if (!StringUtils.isEmpty(token = req.getParameter("token"))) {
                SsoUser user = authcationRpcService.findUserByToken(token);
                SessionUser sessionUser1 = new SessionUser();
                sessionUser1.setToken(token);
                sessionUser1.setUser(user);
                session.setAttribute("sessionUser", sessionUser1);
                String backUrl = UrlUtil.getBackUrl(req);
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
            session.setAttribute("sessionUser", null);
        }
        resp.sendRedirect(ssoServerUrl + "/login?" + "backUrl=" + URLEncoder.encode(UrlUtil.getBackUrl(req), "UTF-8"));
    }

    @Override
    public void destroy() {

    }
}
