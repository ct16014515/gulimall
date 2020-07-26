package com.iflytek.gulimall.member.dao;

import com.iflytek.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 11:08:00
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
