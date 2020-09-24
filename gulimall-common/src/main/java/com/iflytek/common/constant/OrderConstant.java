package com.iflytek.common.constant;

public class OrderConstant {


    public static final String ORDER_TOKEN_REDIS_PREFIX = "gulimall:order:orderToken";

    public final static String MQ_ORDER_EXCHANGE = "order-event-exchange"; //订单服务消息队列交换机
    public final static String MQ_ORDER_DELAY_QUEUE = "order.delay.queue";//订单延时队列
    public final static String MQ_ORDER_RELEASE_QUEUE = "order.release.order.queue";//订单释放队列

    public final static String MQ_ORDER_CREATE_ROUTINGKEY = "order.create.order";//创建订单
    public final static String MQ_ORDER_RELEASE_ROUTINGKEY = "order.release.order";//订单释放路由键
    public final static String MQ_ORDERTOSTOCK_RELEASE_ROUTINGKEY = "order.release.ware.#";//订单通知库存释放路由
}
