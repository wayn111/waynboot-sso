package com.wayn.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayn.common.dao.DeptDao;
import com.wayn.common.dao.UserDao;
import com.wayn.common.domain.Dept;
import com.wayn.common.domain.User;
import com.wayn.common.domain.UserRole;
import com.wayn.common.domain.vo.Tree;
import com.wayn.common.service.UserRoleService;
import com.wayn.common.service.UserService;
import com.wayn.common.util.ParameterUtil;
import com.wayn.common.util.TreeBuilderUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wayn
 * @since 2019-04-13
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private DeptDao deptDao;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public Page<User> listPage(Page<User> page, User user) {
        QueryWrapper<User> wrapper = ParameterUtil.get();
        wrapper.like("userName", user.getUserName());
        wrapper.like("phone", user.getPhone());
        wrapper.like("email", user.getEmail());
        wrapper.eq(user.getUserStatus() != null, "userState", user.getUserStatus());
        wrapper.eq(user.getDeptId() != null, "deptId", user.getDeptId());
        return userDao.selectPage(page, wrapper);
    }

    @Override
    public boolean exit(Map<String, Object> params) {
        String userName = params.get("userName").toString();
        //如果是修改用户，用户名未改变则通过校验
        if (params.get("id") != null) {
            String id = params.get("id").toString();
            if (userDao.selectById(id).getUserName().equals(userName)) {
                return false;
            }
        }
        Integer count = userDao.selectCount(new QueryWrapper<User>().eq("userName", userName));
        return count > 0;
    }

    @CacheEvict(value = "menuCache", allEntries = true)
    @Transactional
    @Override
    public boolean save(User user, String roleIds) {
        user.setCreateTime(new Date());
        user.setPassword(new SimpleHash("MD5", user.getPassword(), user.getUserName(), 1024).toString());
        boolean flag = super.save(user);
        List<UserRole> list = new ArrayList<>();
        if (StringUtils.isNotBlank(roleIds)) {
            String[] split = roleIds.split(",");
            for (String roleId : split) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(user.getId());
                list.add(userRole);
            }
        }
        userRoleService.remove(new QueryWrapper<UserRole>().eq("userId", user.getId()));
        if (list.size() > 0) {
            userRoleService.saveBatch(list);
        }
        return flag;
    }

    @CacheEvict(value = "menuCache", allEntries = true)
    @Transactional
    @Override
    public boolean update(User user, String roleIds) {
        boolean flag = updateById(user);
        List<UserRole> list = new ArrayList<>();
        if (StringUtils.isNotBlank(roleIds)) {
            String[] split = roleIds.split(",");
            for (String roleId : split) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(user.getId());
                list.add(userRole);
            }
        }
        userRoleService.remove(new QueryWrapper<UserRole>().eq("userId", user.getId()));
        if (list.size() > 0) {
            userRoleService.saveBatch(list);
        }
        return flag;
    }

    @Transactional
    @Override
    public boolean remove(String id) {
        userDao.deleteById(id);
        userRoleService.remove(new QueryWrapper<UserRole>().eq("userId", id));
        return true;
    }

    @Transactional
    @Override
    public boolean batchRemove(String[] ids) {
        List<String> list = Arrays.asList(ids);
        userDao.deleteBatchIds(list);
        list.forEach(id -> userRoleService.remove(new QueryWrapper<UserRole>().eq("userId", id)));
        return true;
    }

    @Override
    public boolean resetPwd(String id, String password) {
        update(new UpdateWrapper<User>().eq("id", id).set("password", password));
        return true;
    }

    @Override
    public boolean editAccount(String id, String userName) {
        update(new UpdateWrapper<User>().eq("id", id).set("userName", userName));
        return true;
    }

    //    @Cacheable(value = "userCache", key = "#root.method  + '_user'")
    @Override
    public Tree<Dept> getTree() {
        List<Tree<Dept>> trees = new ArrayList<>();
        List<Dept> depts = deptDao.selectList(new QueryWrapper<>());
        List<User> users = userDao.selectList(new QueryWrapper<>());
        // 获取部门所有pid
        List<Long> ds = depts.stream().map(Dept::getPid).distinct().collect(Collectors.toList());
        // 获取用户所有deptId
        List<Long> us = users.stream().map(User::getDeptId).distinct().collect(Collectors.toList());
        // 合并部门pid和用户deptId
        Long[] objects = ds.toArray(new Long[]{});
        Long[] objects1 = us.toArray(new Long[]{});
        Long[] allDepts = ArrayUtils.addAll(objects, objects1);
        for (Dept dept : depts) {
            // 过滤掉无用户挂载的部门
            if (!ArrayUtils.contains(allDepts, dept.getId())) {
                continue;
            }
            Tree<Dept> tree = new Tree<>();
            tree.setId(dept.getId().toString());
            tree.setParentId(dept.getPid().toString());
            tree.setText(dept.getDeptName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "dept");
            tree.setState(state);
            trees.add(tree);
        }
        for (User user : users) {
            Tree<Dept> tree = new Tree<>();
            tree.setId(user.getId());
            tree.setParentId(user.getDeptId().toString());
            tree.setText(user.getUserName());
            Map<String, Object> state = new HashMap<>(16);
            state.put("opened", true);
            state.put("mType", "user");
            tree.setState(state);
            trees.add(tree);
        }
        return TreeBuilderUtil.build(trees);
    }

    @Override
    public List<JSONObject> selectUser2JsonObj() {
        List<User> users = userDao.selectList(new QueryWrapper<>());
        List<JSONObject> list = new ArrayList<>();
        for (User user : users) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", user.getId());
            jsonObject.put("text", user.getUserName());
            list.add(jsonObject);
        }
        return list;
    }

}
