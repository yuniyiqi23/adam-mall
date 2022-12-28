package com.msb.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.msb.mall.product.dao.AttrAttrgroupRelationDao;
import com.msb.mall.product.entity.AttrAttrgroupRelationEntity;
import com.msb.mall.product.entity.AttrGroupEntity;
import com.msb.mall.product.entity.CategoryEntity;
import com.msb.mall.product.service.AttrAttrgroupRelationService;
import com.msb.mall.product.service.AttrGroupService;
import com.msb.mall.product.service.CategoryService;
import com.msb.mall.product.vo.AttrResponseVo;
import com.msb.mall.product.vo.AttrVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    AttrAttrgroupRelationService attrAttrgroupRelationService;
    @Autowired
    AttrGroupService attrGroupService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    AttrAttrgroupRelationDao attrAttrgroupRelationDao;

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
        if (attrIdList != null && attrIdList.size() > 0) {
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

    @Override
    public PageUtils queryBasePage(Long catelogId, Map<String, Object> params) {
        // 1、根据catelogId查找数据
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        if (catelogId != 0) {
            wrapper.eq("catelog_id", catelogId);
        }
        // 2、根据key过滤数据
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)) {
            wrapper.and(w -> {
                w.eq("attr_id", key).or().like("attr_name", key);
            });
        }

        // 组织AttrResponseVo
        List<AttrEntity> attrEntityList = this.list(wrapper);

        List<AttrResponseVo> attrResponseVoList = attrEntityList.stream().map(m -> {
            AttrResponseVo attrResponseVo = new AttrResponseVo();
            BeanUtils.copyProperties(m, attrResponseVo);
            // 根据catelog_id查找分类名称
            CategoryEntity categoryEntity = categoryService.getById(m.getCatelogId());
            if (categoryEntity != null) {
                attrResponseVo.setCatelogName(categoryEntity.getName());
                attrResponseVo.setCatelogPath(categoryService.getPathById(m.getCatelogId()));
            }
            // 去关联表中找到对应的属性组ID
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao
                    .selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", m.getAttrId()));
            if (attrAttrgroupRelationEntity != null && attrAttrgroupRelationEntity.getAttrGroupId() != null) {
                // 获取到属性组的ID，然后根据属性组的ID我们来查询属性组的名称
                AttrGroupEntity attrGroupEntity = attrGroupService.getById(attrAttrgroupRelationEntity.getAttrGroupId());
                attrResponseVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
            return attrResponseVo;
        }).collect(Collectors.toList());

        IPage<AttrResponseVo> page = new Page<>();
        page.setRecords(attrResponseVoList);
        page.setSize(attrResponseVoList.size());
        return new PageUtils(page);
    }

    @Transactional
    @Override
    public void saveAttr(AttrVO vo) {
        // 1、属性基本信息
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(vo, attrEntity);
        this.save(attrEntity);
        // 2、更新关联关系
        if (vo.getAttrGroupId() != null) {
            Long attrId = attrEntity.getAttrId();
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrId(attrId);
            attrAttrgroupRelationEntity.setAttrGroupId(vo.getAttrGroupId());
            attrAttrgroupRelationService.save(attrAttrgroupRelationEntity);
        }

    }

    @Override
    public AttrResponseVo getDetailById(Long attrId) {
        // 声明返回的对象
        AttrResponseVo responseVo = new AttrResponseVo();
        // 1.根据ID查询规格参数的基本信息
        AttrEntity attrEntity = this.getById(attrId);
        BeanUtils.copyProperties(attrEntity, responseVo);
        // 2.查询关联的属性组信息 中间表
        AttrAttrgroupRelationEntity relationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        if (relationEntity != null) {
            AttrGroupEntity attrGroupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
            responseVo.setAttrGroupId(attrGroupEntity.getAttrGroupId());
            if (attrGroupEntity != null) {
                responseVo.setGroupName(attrGroupEntity.getAttrGroupName());
            }
        }
        // 3.查询关联的类别信息
        Long catelogId = attrEntity.getCatelogId();
        Long[] catelogPath = categoryService.getPathById(catelogId);
        responseVo.setCatelogPath(catelogPath);

        CategoryEntity categoryEntity = categoryService.getById(catelogId);
        if (categoryEntity != null) {
            responseVo.setCatelogName(categoryEntity.getName());
        }
        return responseVo;
    }

    @Transactional
    @Override
    public void updateBaseAttr(AttrVO attr) {
        AttrEntity entity = new AttrEntity();
        BeanUtils.copyProperties(attr, entity);
        // 1.更新基本数据
        this.updateById(entity);
        // 2.修改分组关联的关系
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(entity.getAttrId());
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        // 判断是否存在对应的数据
        Integer count = attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        if (count > 0) {
            // 说明有记录，直接更新
            attrAttrgroupRelationDao.update(relationEntity, new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        } else {
            // 说明没有记录，直接插入
            attrAttrgroupRelationDao.insert(relationEntity);
        }
    }
}
