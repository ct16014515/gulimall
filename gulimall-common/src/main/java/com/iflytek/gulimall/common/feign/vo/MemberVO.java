package com.iflytek.gulimall.common.feign.vo;


import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
public class MemberVO implements Serializable {
    private Long userId;//用户id
    private String nickname;//昵称
    /**
     * 积分
     */
    private Integer integration;



}
