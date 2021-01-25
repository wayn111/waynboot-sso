package com.wayn.mall.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.mall.core.entity.Carousels;

public interface CarouselsDao extends BaseMapper<Carousels> {

    IPage<Carousels> selectListPage(Page page, Carousels carousels);
}
