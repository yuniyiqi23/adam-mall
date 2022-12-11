package com.msb.mall.product.dao;

import com.msb.mall.product.entity.AttrAttrgroupRelationEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 属性&属性分组关联
 *
 * @author adam
 * @email adam@163.com
 * @date 2022-10-19 22:15:52
 */
@Mapper
public interface AttrAttrgroupRelationDao extends BaseMapper<AttrAttrgroupRelationEntity> {

    void removeBatchRelation(@Param("entityList") List<AttrAttrgroupRelationEntity> entityList);

}
