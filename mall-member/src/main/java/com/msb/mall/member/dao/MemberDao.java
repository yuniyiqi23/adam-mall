package com.msb.mall.member.dao;

import com.msb.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author adam
 * @email adam@163.com
 * @date 2022-10-20 21:42:27
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
