package com.msb.mall.coupon.dao;

import com.msb.mall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author adam
 * @email adam@163.com
 * @date 2022-10-20 21:40:37
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
