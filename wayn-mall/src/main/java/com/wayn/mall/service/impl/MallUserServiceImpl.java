package com.wayn.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayn.mall.dao.MallUserDao;
import com.wayn.mall.entity.MallUser;
import com.wayn.mall.service.MallUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MallUserServiceImpl extends ServiceImpl<MallUserDao, MallUser> implements MallUserService {

    @Autowired
    private MallUserDao mallUserDao;

    @Override
    public IPage selectPage(Page<MallUser> page, MallUser mallUser) {
        return mallUserDao.selectListPage(page, mallUser);
    }
}
