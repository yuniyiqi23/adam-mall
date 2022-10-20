package com.msb.mall.product.dao;

import com.msb.mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author adam
 * @email adam@163.com
 * @date 2022-10-19 22:15:52
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
