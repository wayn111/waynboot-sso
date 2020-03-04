package com.wayn.gencode.controller;

import com.wayn.common.base.BaseController;
import com.wayn.common.service.MenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("home")
public class HomeController extends BaseController {
    private static final String HOME_PREFIX = "home";

    @Autowired
    private MenuService menuService;


    @RequiresPermissions("tool:gen:gen")
    @GetMapping
    public String home(Model model) throws Exception {
        request.setAttribute("msg", "gencode");
        return HOME_PREFIX + "/home";
    }

}
