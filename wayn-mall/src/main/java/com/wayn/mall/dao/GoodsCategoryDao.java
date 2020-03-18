package com.wayn.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.mall.entity.GoodsCategory;

public interface GoodsCategoryDao extends BaseMapper<GoodsCategory> {

    IPage selectListPage(Page page, GoodsCategory goodsCategory);
}
