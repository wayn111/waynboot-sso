package com.wayn.admin.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.common.annotation.Log;
import com.wayn.common.base.BaseController;
import com.wayn.common.domain.Role;
import com.wayn.common.enums.Operator;
import com.wayn.common.exception.BusinessException;
import com.wayn.common.service.DictService;
import com.wayn.common.service.MenuService;
import com.wayn.common.service.RoleService;
import com.wayn.common.util.ParameterUtil;
import com.wayn.common.util.R;
import com.wayn.framework.util.ShiroCacheUtil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {
	private static final String PREFIX = "system/role";
	@Autowired
	private RoleService roleService;

	@Autowired
	private DictService dictService;

	@Autowired
	private MenuService menuService;

	@RequiresPermissions("sys:role:role")
	@GetMapping
	public String roleIndex(Model model) {
		model.addAttribute("states", dictService.selectDictsValueByType("state"));
		return PREFIX + "/role";
	}

	@Log(value = "角色管理")
	@RequiresPermissions("sys:role:role")
	@ResponseBody
	@PostMapping("/list")
	public Page<Role> list(Model model, Role role) {
		Page<Role> page = getPage();
		//设置通用查询字段
		ParameterUtil.setWrapper();
		return roleService.listPage(page, role);
	}

	@RequiresPermissions("sys:role:add")
	@GetMapping("/add")
	public String add(Model model) {
		return PREFIX + "/add";
	}

	@RequiresPermissions("sys:role:edit")
	@GetMapping("/edit")
	public String edit(Model model, String id) {
		Role role = roleService.getById(id);
		model.addAttribute("role1", role);
		return PREFIX + "/edit";
	}

	@Log(value = "角色管理", operator = Operator.ADD)
	@RequiresPermissions("sys:role:add")
	@ResponseBody
	@PostMapping("/addSave")
	public R addSave(Model model, Role role, String menuIds) {
		role.setCreateTime(new Date());
		roleService.save(role, menuIds);
		ShiroCacheUtil.clearCachedAuthorizationInfo();
		return R.success("新增角色成功");
	}

	@Log(value = "角色管理", operator = Operator.UPDATE)
	@RequiresPermissions("sys:role:edit")
	@ResponseBody
	@PostMapping("/editSave")
	public R editSave(Model model, Role role, String menuIds) throws Exception {
		roleService.update(role, menuIds);
		ShiroCacheUtil.clearCachedAuthorizationInfo();
		return R.success("修改角色成功");
	}

	@Log(value = "角色管理", operator = Operator.DELETE)
	@RequiresPermissions("sys:role:remove")
	@ResponseBody
	@DeleteMapping("/remove/{roleId}")
	public R remove(Model model, @PathVariable("roleId") String roleId) throws BusinessException {
		roleService.remove(roleId);
		return R.success("删除角色成功");
	}

	@Log(value = "角色管理", operator = Operator.DELETE)
	@RequiresPermissions("sys:role:remove")
	@ResponseBody
	@PostMapping("/batchRemove")
	public R batchRemove(Model model, @RequestParam("ids[]") String[] ids) throws BusinessException {
		roleService.batchRemove(ids);
		return R.success("批量删除角色成功");
	}
}
