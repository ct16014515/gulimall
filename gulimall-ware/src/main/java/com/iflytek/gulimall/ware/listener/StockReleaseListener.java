package com.iflytek.gulimall.ware.listener;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iflytek.common.model.enume.OrderStatusEnum;
import com.iflytek.common.model.vo.order.OrderEntityVO;
import com.iflytek.common.utils.ResultBody;
import com.iflytek.gulimall.ware.dao.WareOrderTaskDao;
import com.iflytek.gulimall.ware.dao.WareOrderTaskDetailDao;
import com.iflytek.gulimall.ware.dao.WareSkuDao;
import com.iflytek.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.iflytek.gulimall.ware.entity.WareSkuEntity;
import com.iflytek.gulimall.ware.feign.OrderService;
import com.iflytek.gulimall.ware.service.WareSkuService;
import com.iflytek.gulimall.ware.vo.WareStockDelayVO;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static com.iflytek.common.constant.WareConstant.MQ_STOCK_RELEASE_QUEUE;

@RabbitListener(queues = {MQ_STOCK_RELEASE_QUEUE})
@Service
@Slf4j
public class StockReleaseListener {

    @Autowired
    private WareOrderTaskDetailDao wareOrderTaskDetailDao;
    @Autowired
    private WareSkuDao wareSkuDao;
    @Autowired
    private WareOrderTaskDao wareOrderTaskDao;
    @Autowired
    WareSkuService wareSkuService;

    @RabbitHandler
    public void releaseStock(WareStockDelayVO wareStockDelayVO, Message message, Channel channel) throws IOException {
        log.info("*********收到库存服务的自动解锁库存消息,订单号:{}***********",wareStockDelayVO.getOrderSn());
        try {
            wareSkuService.stockRelease(wareStockDelayVO);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.info("出现异常,消息重新入队列,订单号:{}", wareStockDelayVO.getOrderSn());
            //出现异常,重新返回队列
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }

    @RabbitHandler
    public void releaseStock(OrderEntityVO orderEntityVO, Message message, Channel channel) throws IOException {
        log.info("*********收到来自订单服务的自动解锁库存消息,订单号:{}************",orderEntityVO.getOrderSn());
        try {
            wareSkuService.stockRelease(orderEntityVO);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.info("出现异常,消息重新入队列,订单号:{}", orderEntityVO.getOrderSn());
            //出现异常,重新返回队列
            channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
        }
    }


}
