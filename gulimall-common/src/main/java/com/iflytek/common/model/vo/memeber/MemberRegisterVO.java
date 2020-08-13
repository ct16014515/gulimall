package com.iflytek.common.model.vo.memeber;

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
