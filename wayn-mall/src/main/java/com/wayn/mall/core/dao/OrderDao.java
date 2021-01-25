package com.wayn.mall.core.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.mall.core.entity.Order;
import com.wayn.mall.core.entity.vo.DayTransactionAmountVO;
import com.wayn.mall.core.entity.vo.OrderListVO;
import com.wayn.mall.core.entity.vo.OrderVO;

import java.util.List;

public interface OrderDao extends BaseMapper<Order> {

    IPage<OrderListVO> selectListVOPage(Page<OrderListVO> page, Order order);

    IPage<Order> selectListPage(Page<Order> page, OrderVO order);

    List<DayTransactionAmountVO> countMallTransactionAmount(Integer dayNum);
}
