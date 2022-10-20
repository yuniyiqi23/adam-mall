package com.msb.mall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.msb.common.utils.PageUtils;
import com.msb.mall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author adam
 * @email adam@163.com
 * @date 2022-10-20 21:42:27
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

