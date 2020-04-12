package com.wayn.admin.controller.monitor;

import com.wayn.common.annotation.Log;
import com.wayn.common.base.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/monitor/interface")
public class InterfaceController extends BaseController {

    private static final String PREFIX = "monitor/interface";

    @Log(value = "系统接口")
    @RequiresPermissions("monitor:interface:interface")
    @GetMapping
    public String interfaceIndex(Model model) {
        return redirectTo("/swagger-ui.html");
    }
}
