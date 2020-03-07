package com.wayn.admin.controller.home;

import com.wayn.common.base.BaseController;
import com.wayn.common.domain.Menu;
import com.wayn.common.service.MenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("home")
public class HomeController extends BaseController {
    private static final String HOME_PREFIX = "home";
    private static final String MAIN_PREFIX = "main";

    @Autowired
    private MenuService menuService;


    @RequiresPermissions("sys:user:user")
    @GetMapping
    public String index(Model model) throws Exception {
        List<Menu> treeMenus = menuService.selectTreeMenuByUserId(getCurUserId());
        model.addAttribute("treeMenus", treeMenus);
        model.addAttribute("user", getCurUser());
        return HOME_PREFIX + "/home";
    }

    @GetMapping("/mainIndex")
    public String mainIndex(Model model) {
        return MAIN_PREFIX + "/main";
    }
}
