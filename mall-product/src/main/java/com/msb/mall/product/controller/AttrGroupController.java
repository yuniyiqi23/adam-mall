package com.msb.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.msb.mall.product.entity.AttrEntity;
import com.msb.mall.product.service.AttrAttrgroupRelationService;
import com.msb.mall.product.service.AttrService;
import com.msb.mall.product.service.impl.AttrServiceImpl;
import com.msb.mall.product.vo.AttrGroupRelationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.msb.mall.product.entity.AttrGroupEntity;
import com.msb.mall.product.service.AttrGroupService;
import com.msb.common.utils.PageUtils;
import com.msb.common.utils.R;



/**
 * 属性分组
 *
 * @author adam
 * @email adam@163.com
 * @date 2022-10-19 22:15:52
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService relationService;

    // product/attrgroup/1/attr/relation?t=1670157832729
    @RequestMapping("/{attrGroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrGroupId") Long attrGroupId){
        List<AttrEntity> list = attrService.getRelationAttr(attrGroupId);

        return R.ok().put("data", list);
    }

    // product/attrgroup/1/noattr/relation?t=1670655401795&page=1&limit=10&key=
    @RequestMapping("/{attrGroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable("attrGroupId") Long attrGroupId,
                            @RequestParam Map<String, Object> params){
        PageUtils pageUtils = attrService.getNoRelationAttr(attrGroupId, params);

        return R.ok().put("page", pageUtils);
    }

    // product/attrgroup/attr/relation
    @PostMapping("/attr/relation")
    public R saveBatch(@RequestBody List<AttrGroupRelationVO> vos){
        relationService.saveBatch(vos);
        return R.ok();
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
        PageUtils page = attrGroupService.queryPage(params, catelogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getInfoById(attrGroupId);

        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
