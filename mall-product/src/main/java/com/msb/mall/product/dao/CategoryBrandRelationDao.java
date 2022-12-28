package com.msb.mall.product.dao;

import com.msb.mall.product.entity.CategoryBrandRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 品牌分类关联
 *
 * @author adam
 * @email adam@163.com
 * @date 2022-10-19 22:15:52
 */
@Mapper
@Repository
public interface CategoryBrandRelationDao extends BaseMapper<CategoryBrandRelationEntity> {

}
