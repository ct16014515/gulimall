package com.iflytek.gulimall.autherserver.vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ToString
public class RegisterVO {

    @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$", message = "用户名的长度为6-10位")
    private String username;
    @Pattern(regexp = "^[a-zA-Z0-9]{6,10}$", message = "密码的长度为6-10位")
    private String password;
    @Pattern(regexp = "^1\\d{10}$", message = "手机号格式错误")
    private String phone;
    @NotBlank(message = "验证码不能为空")
    private String code;
}
