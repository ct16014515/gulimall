package com.iflytek.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.iflytek.common.vliadte.AddGroup;
import com.iflytek.common.vliadte.UpdateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 品牌
 * 
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 01:12:52
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@NotNull(message = "brandId不能为空",groups = {UpdateGroup.class})
	@Null(message = "brandId必须为空",groups = {AddGroup.class})
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotNull(message = "品牌名不能为空")
	private String name;
	/**
	 * 品牌logo地址
	 */
    @URL(message ="url不合法" )
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	//@TableLogic(value = "1",delval="0")
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	private String firstLetter;
	/**
	 * 排序
	 */
	private Integer sort;

}
