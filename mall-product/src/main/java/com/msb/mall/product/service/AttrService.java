package com.msb.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.msb.common.utils.PageUtils;
import com.msb.mall.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author adam
 * @email adam@163.com
 * @date 2022-10-19 22:15:52
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<AttrEntity> getRelationAttr(Long attrGroupId);

    PageUtils getNoRelationAttr(Long attrGroupId, Map<String, Object> params);
}

