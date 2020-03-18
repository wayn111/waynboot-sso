package com.wayn.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.mall.entity.MallUser;

public interface MallUserDao extends BaseMapper<MallUser> {

    IPage selectListPage(Page<MallUser> page, MallUser mallUser);
}
