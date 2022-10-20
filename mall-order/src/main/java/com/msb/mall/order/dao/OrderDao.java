package com.msb.mall.order.dao;

import com.msb.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author adam
 * @email adam@163.com
 * @date 2022-10-20 21:37:41
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
