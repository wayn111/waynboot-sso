package com.wayn.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wayn.mall.entity.MallUser;

public interface MallUserService extends IService<MallUser> {
    IPage selectPage(Page<MallUser> page, MallUser mallUser);
}
