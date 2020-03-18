package com.wayn.mall.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wayn.mall.controller.vo.OrderListVO;
import com.wayn.mall.entity.Order;

public interface OrderDao extends BaseMapper<Order> {

    IPage selectListVOPage(Page<OrderListVO> page, Order order);

    IPage selectListPage(Page<Order> page, Order order);
}
