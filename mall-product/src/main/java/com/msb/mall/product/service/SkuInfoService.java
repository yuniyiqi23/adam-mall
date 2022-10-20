package com.msb.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.msb.common.utils.PageUtils;
import com.msb.mall.product.entity.SkuInfoEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author adam
 * @email adam@163.com
 * @date 2022-10-19 22:15:52
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

