package com.iflytek.gulimall.mq.entity;


import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@ToString
@Document(collection = "mq_logs")
public class MqMessage implements Serializable {
    @Id
    private String messageId;


    private String content;

    private String toExchange;


    private String routingKey;


    private String classType;

    //1 -新建 2错误抵达 3已抵达
    private Integer messageStatus;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;

    private Integer replyCode;

    private String replyText;

    private Integer isSendFromTask=2;//是否已经从定时投递1是,2否,


}
