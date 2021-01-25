package com.wayn.mall.core.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayn.mall.core.dao.AdminUserDao;
import com.wayn.mall.core.entity.AdminUser;
import com.wayn.mall.core.service.AdminUserService;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserDao, AdminUser> implements AdminUserService {
}
