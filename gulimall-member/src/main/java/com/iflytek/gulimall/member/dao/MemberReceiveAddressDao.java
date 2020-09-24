package com.iflytek.gulimall.member.dao;

import com.iflytek.gulimall.member.entity.MemberReceiveAddressEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 会员收货地址
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 11:08:00
 */
@Mapper
public interface MemberReceiveAddressDao extends BaseMapper<MemberReceiveAddressEntity> {
    @Select("SELECT * FROM `ums_member_receive_address` WHERE member_id =#{uid}")
    List<MemberReceiveAddressEntity> getMemberReceiveAddressByUid(@Param("uid") Long uid);
}
