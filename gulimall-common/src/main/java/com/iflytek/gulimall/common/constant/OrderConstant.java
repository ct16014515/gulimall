package com.iflytek.gulimall.common.constant;

public class OrderConstant {


    public static final String ORDER_TOKEN_REDIS_PREFIX = "gulimall:order:orderToken";

//    public final static String MQ_ORDER_EXCHANGE = "order-event-exchange"; //订单服务消息队列交换机
//    public final static String MQ_ORDER_DELAY_QUEUE = "order.delay.queue";//订单延时队列
//    public final static String MQ_ORDER_RELEASE_QUEUE = "order.release.order.queue";//订单释放队列
//
//    public final static String MQ_ORDER_CREATE_ROUTINGKEY = "order.create.order";//创建订单
//    public final static String MQ_ORDER_RELEASE_ROUTINGKEY = "order.release.order";//订单释放路由键
//    public final static String MQ_ORDERTOWARE_ROUTINGKEY = "order.ware.#";//订单通知库存服务路由,可能通知多个业务
//
//    public final static String MQ_ORDERTOWARE_RELEASE_ROUTINGKEY = "order.ware.release";//订单服务通知库存释放库存路由键.只在业务代码使用 不参与绑定到队列和交换机
//    public final static String MQ_ORDERTOWARE_REDUCE_ROUTINGKEY = "order.ware.payed";//订单支付完成服务通知库存真正减库存路由键.只在业务代码使用 不参与绑定到队列和交换机
}
