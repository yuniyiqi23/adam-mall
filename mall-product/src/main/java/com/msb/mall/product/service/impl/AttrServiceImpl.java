package com.msb.mall.product.service.impl;

import com.msb.mall.product.entity.AttrAttrgroupRelationEntity;
import com.msb.mall.product.entity.AttrGroupEntity;
import com.msb.mall.product.service.AttrAttrgroupRelationService;
import com.msb.mall.product.service.AttrGroupService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.msb.common.utils.PageUtils;
import com.msb.common.utils.Query;

import com.msb.mall.product.dao.AttrDao;
import com.msb.mall.product.entity.AttrEntity;
import com.msb.mall.product.service.AttrService;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    AttrAttrgroupRelationService attrAttrgroupRelationService;
    @Autowired
    AttrGroupService attrGroupService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    public List<AttrEntity> getRelationAttr(Long attrGroupId) {
        // 关系标查找数据
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_group_id", attrGroupId);
        List<AttrAttrgroupRelationEntity> list = attrAttrgroupRelationService.list(wrapper);
        // 属性表获取数据
        List<AttrEntity> attrEntityList = list.stream()
                .map(item -> {
                    return this.getById(item.getAttrId());
                })
                .filter(item -> item != null)
                .collect(Collectors.toList());

        return attrEntityList;
    }

    @Override
    public PageUtils getNoRelationAttr(Long attrGroupId, Map<String, Object> params) {
        // 1、根据属性组id获取属性组信息（包含分类id）
        AttrGroupEntity attrGroupEntity = attrGroupService.getInfoById(attrGroupId);
        Long catelogId = attrGroupEntity.getCatelogId();
        // 2、根据属性组id获取属性属性组关系
        QueryWrapper<AttrAttrgroupRelationEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("attr_group_id", attrGroupId);
        List<AttrAttrgroupRelationEntity> list = attrAttrgroupRelationService.list(queryWrapper);
        // 3、根据前两者查找属性表，获取没有关系的属性列表
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id", catelogId);
        // 已关联属性
        List<Long> attrIdList = list.stream()
                .map(item -> item.getAttrId())
                .collect(Collectors.toList());
        if(attrIdList != null && attrIdList.size() > 0){
            wrapper.notIn("attr_id", attrIdList);
        }
        // 4、查找条件
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(w -> w.eq("attr_id", key)
                    .or().like("attr_name", key));
        }

        // 查询对应的相关信息
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }
}
