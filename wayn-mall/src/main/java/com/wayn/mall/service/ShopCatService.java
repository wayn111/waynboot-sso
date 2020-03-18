package com.wayn.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wayn.mall.controller.vo.ShopCatVO;
import com.wayn.mall.entity.ShopCat;

import java.util.List;

public interface ShopCatService extends IService<ShopCat> {
    void saveShopCat(ShopCat shopCat);

    List<ShopCatVO> getShopcatVOList(Long userId);
}
