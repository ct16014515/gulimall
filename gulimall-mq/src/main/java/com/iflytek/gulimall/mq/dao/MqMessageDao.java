package com.iflytek.gulimall.mq.dao;



import com.iflytek.gulimall.mq.entity.MqMessage;

import org.springframework.data.mongodb.repository.MongoRepository;




public interface MqMessageDao extends MongoRepository<MqMessage,String> {

}
