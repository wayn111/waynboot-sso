package com.wayn.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayn.common.dao.RoleDao;
import com.wayn.common.dao.RoleMenuDao;
import com.wayn.common.dao.UserRoleDao;
import com.wayn.common.domain.Role;
import com.wayn.common.domain.RoleMenu;
import com.wayn.common.domain.UserRole;
import com.wayn.common.domain.vo.RoleChecked;
import com.wayn.common.exception.BusinessException;
import com.wayn.common.service.RoleService;
import com.wayn.common.service.UserRoleService;
import com.wayn.common.util.ParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author wayn
 * @since 2019-04-13
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleDao, Role> implements RoleService {

    @Autowired
    private RoleMenuDao roleMenuDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public Page<Role> listPage(Page<Role> page, Role role) {
        QueryWrapper<Role> wrapper = ParameterUtil.get();
        wrapper.like("roleName", role.getRoleName());
        wrapper.eq(role.getRoleStatus() != null, "roleState", role.getRoleStatus());
        return roleDao.selectPage(page, wrapper);
    }

    @CacheEvict(value = "menuCache", allEntries = true)
    @Transactional
    @Override
    public boolean save(Role role, String menuIds) {
        boolean flag = super.save(role);
        List<RoleMenu> list = new ArrayList<>();
        if (StringUtils.isNotBlank(menuIds)) {
            String[] split = menuIds.split(",");
            for (String menuId : split) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                roleMenu.setMenuId(Long.valueOf(menuId));
                roleMenu.setRoleId(role.getId());
                list.add(roleMenu);
            }
        }
        roleMenuDao.delete(new QueryWrapper<RoleMenu>().eq("roleId", role.getId()));
        if (list.size() > 0) {
            roleMenuDao.batchSave(list);
        }
        return flag;
    }

    @CacheEvict(value = "menuCache", allEntries = true)
    @Transactional()
    @Override
    public boolean update(Role role, String menuIds) {
        boolean flag = updateById(role);
        List<RoleMenu> list = new ArrayList<>();
        if (StringUtils.isNotBlank(menuIds)) {
            String[] split = menuIds.split(",");
            for (String menuId : split) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setId(UUID.randomUUID().toString().replaceAll("-", ""));
                roleMenu.setMenuId(Long.valueOf(menuId));
                roleMenu.setRoleId(role.getId());
                list.add(roleMenu);
            }
        }
        roleMenuDao.delete(new QueryWrapper<RoleMenu>().eq("roleId", role.getId()));
        if (list.size() > 0) {
            roleMenuDao.batchSave(list);
        }
        return flag;
    }

    @CacheEvict(value = "menuCache", allEntries = true)
    @Transactional
    @Override
    public boolean remove(String roleId) throws BusinessException {
        if (userRoleDao.selectList(new QueryWrapper<UserRole>().eq("roleId", roleId)).size() > 0) {
            throw new BusinessException("该角色有绑定用户，请先解绑");
        }
        roleDao.deleteById(roleId);
        roleMenuDao.delete(new QueryWrapper<RoleMenu>().eq("roleId", roleId));
        userRoleDao.delete(new QueryWrapper<UserRole>().eq("roleId", roleId));
        return true;
    }

    @CacheEvict(value = "menuCache", allEntries = true)
    @Override
    public boolean batchRemove(String[] ids) throws BusinessException {
        for (String id : ids) {
            if (userRoleDao.selectList(new QueryWrapper<UserRole>().eq("roleId", id)).size() > 0) {
                throw new BusinessException("该角色有绑定用户，请先解绑");
            }
        }
        return removeByIds(Arrays.asList(ids));
    }

    /**
     * 设置当前用户的角色checkbox
     *
     * @param uid 当前用户id
     */
    @Override
    public List<RoleChecked> listCheckedRolesByUid(String uid) {
        List<Role> list = roleDao.selectList(new QueryWrapper<Role>().eq("roleState", 1));
        Set<String> sets = userRoleService.findRolesByUid(uid);
        return list.stream().map(role -> {
            RoleChecked checked = new RoleChecked();
            BeanUtils.copyProperties(role, checked);
            sets.forEach(item -> {
                if (item.equals(checked.getId())) {
                    checked.setChecked(true);
                }
            });
            return checked;
        }).collect(Collectors.toList());
    }

}
