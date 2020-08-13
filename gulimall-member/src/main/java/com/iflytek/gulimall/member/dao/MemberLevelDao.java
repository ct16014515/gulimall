package com.iflytek.gulimall.member.dao;

import com.iflytek.gulimall.member.entity.MemberLevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 会员等级
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 11:08:00
 */
@Mapper
public interface MemberLevelDao extends BaseMapper<MemberLevelEntity> {
    @Select(" SELECT * FROM ums_member_level WHERE default_status = 1")
    Long selectDefalutLevelId();
}
