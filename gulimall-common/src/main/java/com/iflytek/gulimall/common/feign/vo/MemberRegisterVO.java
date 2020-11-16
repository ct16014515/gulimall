package com.iflytek.gulimall.common.feign.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class MemberRegisterVO implements Serializable {

    private String username;

    private String password;

    private String phone;

}
