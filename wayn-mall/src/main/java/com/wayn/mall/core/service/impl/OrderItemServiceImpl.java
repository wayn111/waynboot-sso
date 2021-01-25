package com.wayn.mall.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wayn.mall.core.dao.OrderItemDao;
import com.wayn.mall.core.entity.OrderItem;
import com.wayn.mall.core.service.OrderItemService;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItem> implements OrderItemService {
}
