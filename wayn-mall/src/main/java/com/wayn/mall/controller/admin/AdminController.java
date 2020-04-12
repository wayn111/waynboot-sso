package com.wayn.mall.controller.admin;

import com.wayn.mall.base.BaseController;
import com.wayn.mall.service.AdminUserService;
import com.wayn.mall.util.http.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("admin")
public class AdminController extends BaseController {
    @Autowired
    private AdminUserService adminUserService;

    @Value(("${wayn.ssoServerUrl}"))
    private String ssoServerUrl;

    @RequestMapping
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        return "admin/index";
    }

    // 由于开启了sso登陆，故注释此代码
   /* @RequestMapping("login")
    public String login(HttpServletRequest request) {
        return "admin/login";
    }

    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session) {
        if (StringUtils.isEmpty(verifyCode)) {
            session.setAttribute("errorMsg", "验证码不能为空");
            return "admin/login";
        }
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/login";
        }
        String kaptchaCode = (String) session.getAttribute(Constants.MALL_VERIFY_CODE_KEY);
        if (StringUtils.isEmpty(kaptchaCode) || !verifyCode.equals(kaptchaCode)) {
            session.setAttribute("errorMsg", "验证码错误");
            return "admin/login";
        }
        AdminUser adminUser = adminUserService.getOne(new QueryWrapper<AdminUser>()
                .eq("login_user_name", userName)
                .eq("login_password", Md5Utils.hash(password)));
        if (adminUser != null) {
            session.setAttribute("loginUser", adminUser.getNickName());
            session.setAttribute("loginUserId", adminUser.getAdminUserId());
            //session过期时间设置为7200秒 即两小时
            //session.setMaxInactiveInterval(60 * 60 * 2);
            return "redirect:/admin";
        } else {
            session.setAttribute("errorMsg", "登陆失败，请联系作者获得测试账号");
            return "admin/login";
        }
    }*/

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("adminUser");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ssoServerUrl)
                .append("/logout?backUrl=")
                .append(HttpUtil.getRequestContext(request))
                .append("/admin");
        return redirectTo(stringBuilder.toString());
    }

}
