package com.wayn.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wayn.mall.controller.vo.SearchObjVO;
import com.wayn.mall.controller.vo.SearchPageGoodsVO;
import com.wayn.mall.entity.Goods;

public interface GoodsService extends IService<Goods> {

    IPage selectPage(Page<Goods> page, Goods goods);

    IPage findMallGoodsListBySearch(Page<SearchPageGoodsVO> page, SearchObjVO searchObjVO);

}
