package com.wayn.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.mall.entity.ShopCat;

public interface ShopCatDao extends BaseMapper<ShopCat> {

    IPage selectListPage(Page<ShopCat> page, ShopCat shopCat);
}
