package com.wayn.mall.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayn.mall.controller.vo.SearchObjVO;
import com.wayn.mall.controller.vo.SearchPageGoodsVO;
import com.wayn.mall.dao.GoodsDao;
import com.wayn.mall.entity.Goods;
import com.wayn.mall.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsDao, Goods> implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public IPage selectPage(Page<Goods> page, Goods goods) {
        return goodsDao.selectListPage(page, goods);
    }

    @Override
    public IPage findMallGoodsListBySearch(Page<SearchPageGoodsVO> page, SearchObjVO searchObjVO) {
        return goodsDao.findMallGoodsListBySearch(page, searchObjVO);
    }


}
