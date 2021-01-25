package com.wayn.mall.core.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayn.mall.core.dao.GoodsDao;
import com.wayn.mall.core.entity.Goods;
import com.wayn.mall.core.entity.vo.SearchObjVO;
import com.wayn.mall.core.entity.vo.SearchPageGoodsVO;
import com.wayn.mall.core.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsDao, Goods> implements GoodsService {

    @Autowired
    private GoodsDao goodsDao;

    @Override
    public IPage<Goods> selectPage(Page<Goods> page, Goods goods) {
        return goodsDao.selectListPage(page, goods);
    }

    @Override
    public IPage<Goods> findMallGoodsListBySearch(Page<SearchPageGoodsVO> page, SearchObjVO searchObjVO) {
        return goodsDao.findMallGoodsListBySearch(page, searchObjVO);
    }

    @Override
    public boolean addStock(Long goodsId, Integer goodsCount) {
        return goodsDao.addStock(goodsId, goodsCount);
    }


}
