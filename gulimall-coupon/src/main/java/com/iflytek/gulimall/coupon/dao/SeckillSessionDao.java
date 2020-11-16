package com.iflytek.gulimall.coupon.dao;

import com.iflytek.gulimall.coupon.entity.SeckillSessionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 秒杀活动场次
 * 
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-10-19 16:40:01
 */
@Mapper
public interface SeckillSessionDao extends BaseMapper<SeckillSessionEntity> {
    @Select("select * from  sms_seckill_session where start_time between now() and ADDDATE(now(),INTERVAL 3 DAY ) and `status` =1")
    List<SeckillSessionEntity> selectSeckillSessionLast3Days();
}
