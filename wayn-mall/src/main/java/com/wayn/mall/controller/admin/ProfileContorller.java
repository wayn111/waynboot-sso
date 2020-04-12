package com.wayn.mall.controller.admin;

import com.wayn.mall.base.BaseController;
import com.wayn.mall.exception.BusinessException;
import com.wayn.ssocore.entity.SessionUser;
import com.wayn.ssocore.service.UserRpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("admin/profile")
public class ProfileContorller extends BaseController {

    private static final String PREFIX = "admin/profile";

    @Autowired
    private UserRpcService userRpcService;

    @GetMapping
    public String profile(HttpServletRequest request) {
        SessionUser adminUser = (SessionUser) request.getSession().getAttribute("adminUser");
        if (adminUser == null) {
            return "admin/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", adminUser.getUser().getUserName());
        return PREFIX + "/profile";
    }

    @PostMapping("/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request,
                                 @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        SessionUser adminUser = (SessionUser) request.getSession().getAttribute("adminUser");
        if (adminUser == null || !adminUser.getUser().getPassword().equals(originalPassword)) {
            throw new BusinessException("原密码输入错误");
        }
        if (userRpcService.updatePassword(adminUser.getUser().getId(), newPassword)) {
            // 修改成功后清空session中的数据，前端控制跳转至登录页
            request.getSession().removeAttribute("adminUser");
            Cookie cookie = new Cookie("token", null);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return "success";
        } else {
            return "修改失败";
        }
    }

    @PostMapping("/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request,
                             @RequestParam("loginUserName") String loginUserName) {
        SessionUser adminUser = (SessionUser) request.getSession().getAttribute("adminUser");
        if (adminUser == null) {
            throw new BusinessException("服务器内部错误");
        }

        if (userRpcService.updateUserName(adminUser.getUser().getId(), loginUserName)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
            SessionUser sessionUser = (SessionUser) request.getSession().getAttribute("adminUser");
            sessionUser.getUser().setUserName(loginUserName);
            return "success";
        } else {
            return "修改失败";
        }
    }
}
