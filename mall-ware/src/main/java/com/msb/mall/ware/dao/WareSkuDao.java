package com.msb.mall.ware.dao;

import com.msb.mall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author adam
 * @email adam@163.com
 * @date 2022-10-20 21:44:14
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
