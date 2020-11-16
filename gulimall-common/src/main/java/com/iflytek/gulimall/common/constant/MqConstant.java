package com.iflytek.gulimall.common.constant;

public class MqConstant {

    /**
     * 订单服务的交换机,路由键,队列
     */
    public final static String MQ_ORDER_EXCHANGE = "order-event-exchange"; //订单服务消息队列交换机
    public final static String MQ_ORDER_DELAY_QUEUE = "order.delay.queue";//订单延时队列
    public final static String MQ_ORDER_RELEASE_QUEUE = "order.release.order.queue";//订单释放队列
    public final static String MQ_ORDER_SECKILL_QUEUE = "order.seckill.order.queue";//创建秒杀订单队列

    public final static String MQ_ORDER_CREATE_ROUTINGKEY = "order.create.order";//创建订单
    public final static String MQ_ORDER_RELEASE_ROUTINGKEY = "order.release.order";//订单释放路由键
    public final static String MQ_ORDERTOWARE_ROUTINGKEY = "order.ware.#";//订单通知库存服务路由,可能通知多个业务

    public final static String MQ_ORDERTOWARE_RELEASE_ROUTINGKEY = "order.ware.release";//订单服务通知库存释放库存路由键.只在业务代码使用 不参与绑定到队列和交换机
    public final static String MQ_ORDERTOWARE_REDUCE_ROUTINGKEY = "order.ware.payed";//订单支付完成服务通知库存真正减库存路由键.只在业务代码使用 不参与绑定到队列和交换机

    public final static String MQ_SECKILL_ORDER_CREATE_ROUTINGKEY = "seckill.order.create.order";//秒杀服务通知订单服务,创建秒杀订单路由键

    /**
     * 库存服务的交换机,路由键,队列
     */
    public final static String MQ_WARE_EXCHANGE = "ware-event-exchange"; //仓储服务消息队列交换机

    public final static String MQ_STOCK_DELAY_QUEUE = "ware.stock.delay.queue";//库存延时队列
    public final static String MQ_STOCK_RELEASE_QUEUE = "ware.stock.release.queue";//库存释放队列

    public final static String MQ_STOCK_LOCKED_ROUTINGKEY = "ware.stock.locked";//锁定库存路由键
    public final static String MQ_STOCK_RELEASE_ROUTINGKEY = "ware.stock.release.#";//库存释放路由键

    /**
     * 定时任务的交换机,路由键,队列
     */
    public final static String MQ_JOB_EXCHANGE = "job-event-exchange";//定时任务交换机
    public final static String MQ_JOB_SECKILLPRODUCTUP_QUEUE = "job.seckillproductup.queue";//定时任务通知秒杀商品上架处理业务队列

    public final static String MQ_JOB_SECKILLPRODUCTUP_ROUTINGKEY = "job.coupon.seckillproductup";//订单释放路由键


}
