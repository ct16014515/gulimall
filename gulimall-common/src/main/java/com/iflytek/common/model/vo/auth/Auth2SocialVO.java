package com.iflytek.common.model.vo.auth;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
@Data
@ToString
public class Auth2SocialVO implements Serializable {
    private String access_token;

    private String remind_in;

    private int expires_in;

    private String uid;

    private String isRealName;
    /**
     * 1微博,2微信,3qq
     */
    private Integer type;

}
