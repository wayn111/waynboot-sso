package com.wayn.admin.controller.system;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.common.annotation.Log;
import com.wayn.common.base.BaseController;
import com.wayn.common.domain.Dept;
import com.wayn.common.domain.Role;
import com.wayn.common.domain.User;
import com.wayn.common.domain.vo.RoleChecked;
import com.wayn.common.domain.vo.Tree;
import com.wayn.common.enums.Operator;
import com.wayn.common.service.*;
import com.wayn.common.util.ParameterUtil;
import com.wayn.common.util.R;
import com.wayn.common.util.shiro.util.ShiroUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {
    private static final String PREFIX = "system/user";

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private DictService dictService;

    @RequiresPermissions("sys:user:user")
    @GetMapping
    public String userIndex(Model model) {
        model.addAttribute("states", dictService.selectDictsValueByType("state"));
        return PREFIX + "/user";
    }

    @Log(value = "用户管理")
    @RequiresPermissions("sys:user:user")
    @ResponseBody
    @PostMapping("/list")
    public Page<User> list(Model model, User user) {
        Page<User> page = getPage();
        //设置通用查询字段
        ParameterUtil.setWrapper();
        return userService.listPage(page, user);
    }

    @RequiresPermissions("sys:user:add")
    @GetMapping("/add")
    public String add(Model model) {
        List<Role> list = roleService.list(new QueryWrapper<Role>().eq("roleStatus", 1));
        model.addAttribute("roles", list);
        return PREFIX + "/add";
    }

    @RequiresPermissions("sys:user:edit")
    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable("id") String id) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        Dept dept = deptService.getById(user.getDeptId());
        if (Objects.nonNull(dept)) {
            String deptName = dept.getDeptName();
            model.addAttribute("deptName", deptName);
        }
        List<RoleChecked> roleCheckedList = roleService.listCheckedRolesByUid(id);
        model.addAttribute("roles", roleCheckedList);
        return PREFIX + "/edit";
    }

    @RequiresPermissions("sys:user:resetPwd")
    @GetMapping("/resetPwd/{id}")
    public String resetPwd(Model model, @PathVariable("id") String id) {
        model.addAttribute("id", id);
        return PREFIX + "/resetPwd";
    }

    @RequiresPermissions("sys:user:resetPwd")
    @ResponseBody
    @PostMapping("/resetPwd")
    public R resetPwd(Model model, @RequestParam String id, @RequestParam String password) {
        userService.resetPwd(id, ShiroUtil.md5encrypt(password, userService.getById(id).getUserName()));
        return R.success("修改用户密码成功");
    }

    @RequiresPermissions("sys:user:editAcount")
    @GetMapping("/editAcount/{id}")
    public String editAcount(Model model, @PathVariable("id") String id) {
        model.addAttribute("id", id);
        model.addAttribute("userName", userService.getById(id).getUserName());
        return PREFIX + "/editAcount";
    }

    @RequiresPermissions("sys:user:editAcount")
    @ResponseBody
    @PostMapping("/editAcount")
    public R editAcount(Model model, @RequestParam String id, @RequestParam String userName) {
        userService.editAccount(id, userName);
        return R.success("修改用户名称成功");
    }

    @ResponseBody
    @PostMapping("/exists")
    public boolean exists(Model model, @RequestParam Map<String, Object> params) {
        return !userService.exit(params);
    }

    @Log(value = "用户管理", operator = Operator.ADD)
    @RequiresPermissions("sys:user:add")
    @ResponseBody
    @PostMapping("/addSave")
    public R addSave(Model model, User user, String roleIds) {
        userService.save(user, roleIds);
        return R.success("新增用户成功");
    }

    @Log(value = "用户管理", operator = Operator.UPDATE)
    @RequiresPermissions("sys:user:edit")
    @ResponseBody
    @PostMapping("/editSave")
    public R editSave(Model model, User user, String roleIds) {
        userService.update(user, roleIds);
        return R.success("修改用户成功");

    }

    @Log(value = "用户管理", operator = Operator.DELETE)
    @RequiresPermissions("sys:user:remove")
    @ResponseBody
    @DeleteMapping("/remove/{id}")
    public R remove(Model model, @PathVariable("id") String id) {
        userService.remove(id);
        return R.success("删除用户成功");

    }

    @Log(value = "用户管理", operator = Operator.DELETE)
    @RequiresPermissions("sys:user:remove")
    @ResponseBody
    @PostMapping("/batchRemove")
    public R batchRemove(Model model, @RequestParam("ids[]") String[] ids) {
        userService.batchRemove(ids);
        return R.success("删除用户成功");

    }


    @ResponseBody
    @PostMapping("/tree")
    public Tree<Dept> tree(Model model) {
        return userService.getTree();
    }

    @GetMapping("/treeView")
    public String treeView(Model model) {
        return PREFIX + "/treeView";
    }
}
