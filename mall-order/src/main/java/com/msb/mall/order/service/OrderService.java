package com.msb.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.msb.common.utils.PageUtils;
import com.msb.mall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author adam
 * @email adam@163.com
 * @date 2022-10-20 21:37:41
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

