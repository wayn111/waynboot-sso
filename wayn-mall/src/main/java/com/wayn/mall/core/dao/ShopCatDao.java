package com.wayn.mall.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.mall.core.entity.ShopCat;

public interface ShopCatDao extends BaseMapper<ShopCat> {

    IPage<ShopCat> selectListPage(Page<ShopCat> page, ShopCat shopCat);
}
