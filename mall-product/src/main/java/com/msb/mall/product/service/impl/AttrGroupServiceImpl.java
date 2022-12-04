package com.msb.mall.product.service.impl;

import com.msb.mall.product.service.CategoryService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.common.utils.PageUtils;
import com.msb.common.utils.Query;

import com.msb.mall.product.dao.AttrGroupDao;
import com.msb.mall.product.entity.AttrGroupEntity;
import com.msb.mall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        // 查询条件
        String key = (String)params.get("key");
        if(StringUtils.isNotBlank(key)){
            wrapper.and(obj ->{
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }

        if (catelogId != 0) {
            wrapper.eq("catelog_id", catelogId);
        }
        IPage page = this.page(new Query<AttrGroupEntity>().getPage(params),
                wrapper);

        return new PageUtils(page);
    }

    @Override
    public AttrGroupEntity getInfoById(Long attrGroupId) {
        AttrGroupEntity attrGroupEntity = this.getById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        Long[] categoryPath = categoryService.getPathById(catelogId);
        attrGroupEntity.setCatelogPath(categoryPath);
        return attrGroupEntity;
    }

}
