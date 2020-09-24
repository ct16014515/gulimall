package com.iflytek.gulimall.autherserver.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Data
@ConfigurationProperties("auth2.weibo")
public class Auth2WeiBoComponent {

    private String client_id;
    private String client_secret;
    private String grant_type;
    private String redirect_uri;
    private String request_url;

}
