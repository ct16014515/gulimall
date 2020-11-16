package com.iflytek.gulimall.common.feign.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SendMessageRequest implements Serializable {
    String exchange; //交换机
    String routingKey;//路由键
    Object object;//传递的数据
    String className;//传递数据的className


}
