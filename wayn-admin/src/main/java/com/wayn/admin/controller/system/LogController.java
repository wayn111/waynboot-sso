package com.wayn.admin.controller.system;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.common.base.BaseController;
import com.wayn.common.domain.OperLog;
import com.wayn.common.enums.Operator;
import com.wayn.common.service.LogService;
import com.wayn.common.util.ParameterUtil;
import com.wayn.common.util.R;
import com.wayn.common.util.UserAgentUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/system/log")
public class LogController extends BaseController {
    private static final String PREFIX = "system/log";

    @Autowired
    private LogService logService;

    @RequiresPermissions("sys:log:log")
    @GetMapping
    public String log(ModelMap map) {
        JSONArray arr = new JSONArray();
        for (Operator operator : Operator.values()) {
            JSONObject obj = new JSONObject();
            obj.put("id", operator.getCode());
            obj.put("text", operator.getName());
            arr.add(obj);
        }
        map.addAttribute("operation", arr);
        return PREFIX + "/log";
    }

    @RequiresPermissions("sys:log:log")
    @ResponseBody
    @PostMapping("/list")
    public Page<OperLog> list(Model model, OperLog log) {
        Page<OperLog> page = getPage();
        // 设置通用查询字段
        ParameterUtil.setWrapper();
        return logService.listPage(page, log);
    }

    @RequestMapping("/detail/{id}")
    @GetMapping
    public String detail(ModelMap map, @PathVariable("id") String id) {
        OperLog operLog = logService.getById(id);
        String browserName = UserAgentUtils.getBrowserName(operLog.getAgent());
        String osName = UserAgentUtils.getOsName(operLog.getAgent());
        operLog.setAgent(browserName + "\t" + osName);
        map.addAttribute("log", operLog);
        return PREFIX + "/detail";
    }

    @RequiresPermissions("sys:log:remove")
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    public R remove(ModelMap map, @PathVariable("id") String id) {
        logService.getById(id);
        return R.success("删除日志成功");
    }

    @RequiresPermissions("sys:log:remove")
    @ResponseBody
    @PostMapping("/batchRemove")
    public R batchRemove(ModelMap map, @RequestParam("ids[]") String[] ids) {
        List<String> idList = Arrays.asList(ids);
        logService.removeByIds(idList);
        return R.success("删除日志成功");
    }

}
