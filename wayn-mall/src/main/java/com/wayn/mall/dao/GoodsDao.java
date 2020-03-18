package com.wayn.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.mall.controller.vo.SearchObjVO;
import com.wayn.mall.controller.vo.SearchPageGoodsVO;
import com.wayn.mall.entity.Goods;

public interface GoodsDao extends BaseMapper<Goods> {
    IPage selectListPage(Page<Goods> page, Goods goods);

    IPage findMallGoodsListBySearch(Page<SearchPageGoodsVO> page, SearchObjVO searchObjVO);
}
