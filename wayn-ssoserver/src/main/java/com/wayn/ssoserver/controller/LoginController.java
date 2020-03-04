package com.wayn.ssoserver.controller;

import com.google.code.kaptcha.Constants;
import com.wayn.ssocore.entity.SsoUser;
import com.wayn.ssocore.service.UserRpcService;
import com.wayn.ssoserver.manager.TokenManager;
import com.wayn.ssoserver.util.R;
import com.wayn.ssoserver.util.ShiroUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Objects;


@Controller
public class LoginController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private UserRpcService userRpcService;

    @GetMapping("login")
    public String login(@RequestParam String backUrl, HttpServletRequest request) {
        String token = getTokenByCookie(request);
        if (!StringUtils.isEmpty(token) && tokenManager.validateAndRefresh(token)) {
            return "redirect:" + backUrl + "?token=" + token;
        }
        request.setAttribute("backUrl", backUrl);
        return "login";
    }


    @ResponseBody
    @PostMapping("login")
    public R login(
            @RequestParam String backUrl,
            @RequestParam String userName,
            @RequestParam String password,
            @RequestParam String clientCaptcha,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String captcha = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
        if (!clientCaptcha.equalsIgnoreCase(captcha)) {
            return R.fail("验证码错误");
        }
        String token;
        SsoUser dbUser = userRpcService.loginValidate(userName, ShiroUtil.md5encrypt(password, userName));
        if (Objects.nonNull(dbUser)) {
            dbUser.setPassword(password);
            token = request.getContextPath().replaceAll("/", "") + ":" + tokenManager.generateToken();
            tokenManager.addToken(token, dbUser);
            Cookie cookie = new Cookie("token", token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            // 跳转到原请求
            backUrl = URLDecoder.decode(backUrl, "utf-8");
            return R.success().add("backUrl", backUrl).add("token", token);
        }
        return R.fail("用户名或者密码错误");
    }


    @GetMapping("logout")
    public String logout(
            HttpServletRequest request,
            @RequestParam String backUrl) {
        String token = getTokenByCookie(request);
        tokenManager.removeToken(token);
        return "redirect:/login?backUrl=" + backUrl;
    }

    private String getTokenByCookie(HttpServletRequest request) {
        String token = "";
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return token;
    }
}
