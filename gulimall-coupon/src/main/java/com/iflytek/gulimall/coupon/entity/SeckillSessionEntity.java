package com.iflytek.gulimall.coupon.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iflytek.gulimall.common.base.Entity;
import lombok.Data;

/**
 * 秒杀活动场次
 * 
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-10-19 16:40:01
 */
@Data
@TableName("sms_seckill_session")
public class SeckillSessionEntity extends Entity {
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	@TableId
	private Long id;
	/**
	 * 场次名称
	 */
	private String name;
	/**
	 * 每日开始时间
	 */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	private LocalDateTime startTime;
	/**
	 * 每日结束时间
	 */
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	private LocalDateTime endTime;
	/**
	 * 启用状态
	 */
	private Integer status;

   //活动和商品关联表
	@TableField(exist = false)
	private List<SeckillSkuRelationEntity> skuRelationEntityList;


}
