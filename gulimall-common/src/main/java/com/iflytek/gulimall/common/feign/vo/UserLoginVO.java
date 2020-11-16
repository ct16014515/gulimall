package com.iflytek.gulimall.common.feign.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用户登录入参
 */
@Data
@ToString
public class UserLoginVO implements Serializable {
    @NotBlank(message = "账户不能为空")
    private String account;  //账户可以是用户名也可以是手机号
    @NotBlank(message = "密码不能为空")
    private String password;
    private String returnUrl;

}
