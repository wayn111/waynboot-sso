package com.wayn.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wayn.mall.controller.vo.MallUserVO;
import com.wayn.mall.controller.vo.OrderListVO;
import com.wayn.mall.controller.vo.ShopCatVO;
import com.wayn.mall.entity.Order;

import java.util.List;

public interface OrderService extends IService<Order> {
    IPage selectMyOrderPage(Page<OrderListVO> page, Order order);

    IPage selectPage(Page<Order> page, Order order);

    String saveOrder(MallUserVO mallUserVO, List<ShopCatVO> shopcatVOList);

}
