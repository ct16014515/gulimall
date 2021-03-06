package com.iflytek.gulimall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iflytek.gulimall.common.exception.RRException;
import com.iflytek.gulimall.common.feign.*;
import com.iflytek.gulimall.common.feign.vo.*;
import com.iflytek.gulimall.common.model.enume.OrderPayTypeEnum;
import com.iflytek.gulimall.common.model.enume.OrderStatusEnum;

import com.iflytek.gulimall.common.model.mq.to.OrderEntityPayedTO;
import com.iflytek.gulimall.common.model.mq.to.OrderEntityReleaseTO;

import com.iflytek.gulimall.common.model.mq.to.SecKillOrderCreateTO;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.Query;
import com.iflytek.gulimall.common.utils.R;
import com.iflytek.gulimall.common.utils.ResultBody;
import com.iflytek.gulimall.order.config.AlipayTemplate;
import com.iflytek.gulimall.order.dao.OrderDao;
import com.iflytek.gulimall.order.dao.OrderItemDao;
import com.iflytek.gulimall.order.entity.OrderEntity;
import com.iflytek.gulimall.order.entity.OrderItemEntity;

import com.iflytek.gulimall.order.interceptor.LoginInterceptor;
import com.iflytek.gulimall.order.service.OrderItemService;
import com.iflytek.gulimall.order.service.OrderService;
import com.iflytek.gulimall.order.vo.*;
import com.lly835.bestpay.model.PayResponse;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.iflytek.gulimall.common.constant.MqConstant.*;
import static com.iflytek.gulimall.common.constant.OrderConstant.ORDER_TOKEN_REDIS_PREFIX;


@Service("orderService")
//@RabbitListener(queues = {"hello.java.queue"})
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    @Autowired
    private CartServiceAPI cartServiceApi;
    @Autowired
    private MemberServiceAPI memberServiceAPI;
    @Autowired
    private TaskExecutor executor;

    @Autowired
    private WareServiceAPI wareServiceAPI;

    @Autowired
    private ProductServiceAPI productServiceAPI;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDao orderDao;

    @Autowired
    MqServiceAPI mqServiceAPI;
    @Autowired
    OrderItemDao orderItemDao;

    @Autowired
    AlipayTemplate alipayTemplate;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVO toTrade() throws ExecutionException, InterruptedException {
        OrderConfirmVO orderConfirmVO = new OrderConfirmVO();
        MemberVO memberVO = LoginInterceptor.getMemberVO();
        Long userId = memberVO.getUserId();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        /**
         * ?????????????????????
         */
        CompletableFuture<Void> listCompletableFuture = CompletableFuture.runAsync(() -> {
            /**
             * 1???????????? feign????????????????????????,???GulimallFeignConfig??????
             * RequestContextHolder?????????????????????????????????,??????????????????,???????????????????????????????????????,
             * ????????????????????????,?????????????????????????????????
             *
             */
            RequestContextHolder.setRequestAttributes(requestAttributes);
            ResultBody<List<CartItemVO>> resultBody = cartServiceApi.getCartListByUid(String.valueOf(userId));
            List<CartItemVO> cartItemVOS = resultBody.getData();
            orderConfirmVO.setOrderItemVOS(cartItemVOS);
        }, executor);
        //????????????
        CompletableFuture<Void> hasStocFuture = listCompletableFuture.thenRunAsync(() -> {
            List<CartItemVO> cartItemVOS = orderConfirmVO.getOrderItemVOS();
            List<Long> skuIds = cartItemVOS.stream().map((itemVO) -> itemVO.getSkuId()).collect(Collectors.toList());
            //?????????????????????
            R hasStock = wareServiceAPI.hasStock(skuIds);
            List<WareHasStockVO> hasStockVOS = hasStock.getData(new TypeReference<List<WareHasStockVO>>() {
            });
            Map<Long, Integer> hasStockMap = hasStockVOS.stream().collect(Collectors.toMap(WareHasStockVO::getSkuId, WareHasStockVO::getHasStock));
            for (CartItemVO orderItemVO : cartItemVOS) {
                Long skuId = orderItemVO.getSkuId();
                orderItemVO.setHasStock(hasStockMap.get(skuId));
            }
        }, executor);
        //????????????,???????????????
        CompletableFuture<Void> priceFuture = listCompletableFuture.thenRunAsync(() -> {
            List<CartItemVO> cartItemVOS = orderConfirmVO.getOrderItemVOS();
            List<Long> skuIds = cartItemVOS.stream().map((itemVO) -> itemVO.getSkuId()).collect(Collectors.toList());
            ResultBody<List<SkuInfoPriceVO>> skuPriceBySkuIds = productServiceAPI.getSkuPriceBySkuIds(skuIds);
            List<SkuInfoPriceVO> skuInfoPriceVOS = skuPriceBySkuIds.getData();
            Map<Long, BigDecimal> collect = skuInfoPriceVOS.stream().collect(Collectors.toMap(SkuInfoPriceVO::getSkuId, SkuInfoPriceVO::getPrice));
            /**
             * ???????????????,??????
             */
            BigDecimal totalMoney = new BigDecimal("0.00");
            Integer orderItemCount = 0;
            for (CartItemVO orderItemVO : cartItemVOS) {
                Long skuId = orderItemVO.getSkuId();
                BigDecimal skuPrice = collect.get(skuId);
                orderItemVO.setSkuPrice(skuPrice);
                orderItemVO.setSkuTotalMoney(skuPrice.multiply(new BigDecimal(orderItemVO.getSkuCount())));
                totalMoney = totalMoney.add(orderItemVO.getSkuTotalMoney());
                orderItemCount += orderItemVO.getSkuCount();
            }
            orderConfirmVO.setTotalMoney(totalMoney);
            orderConfirmVO.setOrderItemCount(orderItemCount);
        }, executor);

        /**
         * ????????????
         */
        CompletableFuture<Void> emberReceiveAddressFuture = CompletableFuture.runAsync(() -> {
            RequestContextHolder.setRequestAttributes(requestAttributes);
            /**
             * ????????????
             */
            ResultBody<List<MemberReceiveAddressVO>> memberReceiveAddressByUid = memberServiceAPI.getMemberReceiveAddressByUid(userId);
            List<MemberReceiveAddressVO> memberReceiveAddressVOS = memberReceiveAddressByUid.getData();
            Optional<MemberReceiveAddressVO> first = memberReceiveAddressVOS.stream().filter(item -> item.getDefaultStatus() == 1).findFirst();
            if (first.isPresent()) {
                MemberReceiveAddressVO memberReceiveAddressVO = first.get();
                Long receiveAddressVOId = memberReceiveAddressVO.getId();
                BigDecimal freightMoneyByAddressId = getFreightMoneyByAddressId(receiveAddressVOId);
                /**
                 * ??????
                 */
                orderConfirmVO.setFreightMoney(freightMoneyByAddressId);
            }
            orderConfirmVO.setMemberReceiveAddressVOS(memberReceiveAddressVOS);

        }, executor);


        CompletableFuture.allOf(listCompletableFuture, emberReceiveAddressFuture, hasStocFuture, priceFuture).get();

        /**
         * ??????
         */
        Integer integration = memberVO.getIntegration();
        orderConfirmVO.setIntegration(integration);
        /**
         * ????????????
         */
        orderConfirmVO.setPayMoney(orderConfirmVO.getTotalMoney().add(orderConfirmVO.getFreightMoney()));
        /**
         * ???????????????
         */
        String orderToken = UUID.randomUUID().toString().replaceAll("-", "");
        orderConfirmVO.setOrderToken(orderToken);
        redisTemplate.opsForValue().set(
                ORDER_TOKEN_REDIS_PREFIX + memberVO.getUserId(),
                orderToken,
                30,
                TimeUnit.MINUTES);


        return orderConfirmVO;
    }

    //????????????id????????????
    @Override
    public BigDecimal getFreightMoneyByAddressId(Long addressId) {
        return new BigDecimal("0.01");
    }

    /**
     * 1???????????? ???????????? ??????token?????????token??????????????????,??????lua??????
     *
     * @param orderSubmitVO
     * @return
     */
    @Override
    // @GlobalTransactional
    @Transactional
    public OrderSubmitResposeVO submitOrder(OrderSubmitVO orderSubmitVO) {
        OrderSubmitResposeVO orderSubmitResposeVO = new OrderSubmitResposeVO();
        MemberVO memberVO = LoginInterceptor.getMemberVO();
        String orderToken = orderSubmitVO.getOrderToken();
        /**
         *??????lua??????,???????????????????????????????????????????????????,???????????????Long, Boolean, List
         * https://www.cnblogs.com/liuyu7177/p/10918250.html
         * the script result type. Should be one of Long, Boolean, List, or deserialized value type
         */
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long result = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                Arrays.asList(ORDER_TOKEN_REDIS_PREFIX + memberVO.getUserId()),
                orderToken);
        if (result != null && result == 1L) {
            //??????????????????,????????????
            OrderCreateVO orderCreateVO = createOrderCreateVO(orderSubmitVO);
            BigDecimal subtract = orderCreateVO.getOrderEntity().getPayAmount().subtract(orderSubmitVO.getPayMoney());
            if (subtract.compareTo(new BigDecimal("0.00")) == 0) {
                //TODO,????????????
                saveOrderSubmitResposeVOToDB(orderCreateVO);
                //TODO,???????????????,?????????????????????????????????
                wareSkuLock(orderSubmitResposeVO, orderCreateVO);
            } else {
                throw new RRException("?????????????????????????????????,???????????????", 2);
            }
        } else {
            throw new RRException("????????????", 1);
        }
        return orderSubmitResposeVO;
    }

    @Override
    public OrderEntity getOrderEntityByOrderSnAndUserId(String orderSn, Long userId) {
        OrderEntity orderEntity = orderDao.selectOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderSn).eq("member_id", userId));
        return orderEntity;
    }

    @Override
    public OrderEntity getOrderEntityByOrderSn(String orderSn) {
        OrderEntity orderEntity = orderDao.selectOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderSn));
        return orderEntity;
    }

    /**
     * ????????????
     * ??????: ????????????1????????????????????????,??????2????????????????????????
     * ?????????????????????????????????????????????????????????10????????????????????????,???????????????????????????????????????????????????,????????????????????????
     * ??????: ????????????????????????????????????????????????????????????
     *
     * @param entity
     */
    @Override
    @Transactional
    public void closeOrder(OrderEntityReleaseTO entity) {
        OrderEntity orderEntity = orderDao.selectOne(new QueryWrapper<OrderEntity>().
                eq("id", entity.getId()).
                eq("status", OrderStatusEnum.CREATE_NEW.getCode()));
        if (orderEntity != null) {
            OrderEntity update = new OrderEntity();
            update.setId(entity.getId());
            update.setStatus(OrderStatusEnum.CANCLED.getCode());
            orderDao.updateById(update);
            log.info("*****?????????????????????,?????????????????????,?????????:{}******", entity.getOrderSn());
            /**
             * ????????????,??????????????????????????????
             */
            SendMessageRequest request = SendMessageRequest.builder().exchange(MQ_ORDER_EXCHANGE).
                    routingKey(MQ_ORDERTOWARE_RELEASE_ROUTINGKEY).className(entity.getClass().getName())
                    .object(entity).build();
            mqServiceAPI.sendMessage(request);
            /**
             * ???????????????????????????????????????????????????????????????????????????????????????????????????,???????????????????????????????????????40004??????
             * ??????????????????,????????????????????????????????????????????????,?????????????????????????????????.
             */
            //closePayOrder(orderEntity);
        }
    }

    private void closePayOrder(OrderEntity entity) {
        if (OrderPayTypeEnum.ALI.getCode().equals(entity.getPayType())) {
            alipayTemplate.closeOrder(entity.getOrderSn());
        }


    }

    @Override
    public String getSkuNameByOrderSn(String orderSn) {
        String skuName = orderItemDao.getSkuNameByOrderSn(orderSn);
        return skuName;
    }

    @Override

    public PayVO getPayVOByOrderSn(String orderSn) {
        OrderEntity orderEntity = getOrderEntityByOrderSn(orderSn);
        PayVO payVo = new PayVO();
        payVo.setTotal_amount(orderEntity.getPayAmount().toString());
        payVo.setOut_trade_no(orderSn);
        String skuName = getSkuNameByOrderSn(orderSn);
        //??????????????????subject????????????????????????,????????????????????????INVALID_PARAMETER??????
        skuName = skuName.replaceAll("[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~???@#???%??????&*????????????+|{}??????'??????\"\"?????? ??????]", "");
        payVo.setSubject(skuName);
        payVo.setBody("??????");
        /**
         * ???????????????????????????????????????
         */
        updatePayTypeByOrderId(orderEntity.getId(), OrderPayTypeEnum.ALI.getCode());
        return payVo;
    }

    @Transactional
    public void updatePayTypeByOrderId(Long orderId, Integer payType) {
        OrderEntity update = new OrderEntity();
        update.setId(orderId);
        update.setPayType(payType);
        this.updateById(update);

    }

    @Override
    @Transactional
    public void createSecKillOrder(SecKillOrderCreateTO entityTO) {
        OrderEntity orderEntity = baseMapper.selectOne(new QueryWrapper<OrderEntity>().eq("order_sn", entityTO.getOrderSn()));
        OrderItemEntity orderItemEntity = orderItemDao.selectOne(new QueryWrapper<OrderItemEntity>().eq("order_sn", entityTO.getOrderSn()));
        if (orderEntity == null && orderItemEntity == null) {
            OrderEntity saveOrder = new OrderEntity();
            saveOrder.setOrderSn(entityTO.getOrderSn());
            saveOrder.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
            saveOrder.setModifyTime(new Date());
            saveOrder.setCreateTime(new Date());
            saveOrder.setMemberId(entityTO.getMemberId());
            saveOrder.setPayAmount(entityTO.getPrice().multiply(new BigDecimal(entityTO.getNumber().toString())));
            baseMapper.insert(saveOrder);
            OrderItemEntity orderItemSave = new OrderItemEntity();
            orderItemSave.setOrderSn(entityTO.getOrderSn());
            orderItemSave.setSkuId(entityTO.getSkuId());
            orderItemSave.setSkuQuantity(entityTO.getNumber());
            orderItemDao.insert(orderItemSave);
        }
    }


    @Override
    public PageUtils orderWithOrderItemList(Map<String, Object> params) {
        MemberVO memberVO = LoginInterceptor.getMemberVO();
        IPage<OrderEntity> page = page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>().eq("member_id", memberVO.getUserId()).
                        orderByDesc("create_time")
        );
        IPage<OrderEntityVO> pageVO = new Page<OrderEntityVO>();
        List<OrderEntity> records = page.getRecords();
        List<OrderEntityVO> orderEntityVOS = records.stream().map(item -> {
            OrderEntityVO orderEntityVO = new OrderEntityVO();
            BeanUtils.copyProperties(item, orderEntityVO);
            orderEntityVO.setStatusStr(OrderStatusEnum.getMsg(item.getStatus()));
            List<OrderItemVO> orderItemVOS = orderItemDao.selectOrderItemVOListByOrderSn(orderEntityVO.getOrderSn());
            orderEntityVO.setOrderItemVOS(orderItemVOS);
            return orderEntityVO;
        }).collect(Collectors.toList());
        pageVO.setRecords(orderEntityVOS);
        pageVO.setTotal(page.getTotal());
        pageVO.setCurrent(page.getCurrent());
        pageVO.setSize(page.getSize());
        pageVO.setPages(page.getPages());
        return new PageUtils(pageVO);
    }

    /**
     * ???????????????
     * ??????????????????????????????????????? out_trade_no ?????????????????????????????????????????????
     * ?????? total_amount ?????????????????????????????????????????????????????????????????????????????????
     * ?????????????????? seller_id????????? seller_email) ????????? out_trade_no ?????????????????????????????????????????????????????????????????????????????? seller_id/seller_email??????
     * ?????? app_id ????????????????????????
     *
     * @param stringMap
     * @return
     */
    @Override
    @Transactional
    public String aliPayCallBack(Map<String, String> stringMap) {
        try {
            String tradeStatus = stringMap.get("trade_status");
            if ("TRADE_SUCCESS".equals(tradeStatus)) {
                String outTradeNo = stringMap.get("out_trade_no");//?????????
                OrderEntity orderEntity = this.getOrderEntityByOrderSn(outTradeNo);
                String payAmount = orderEntity.getPayAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString();//???????????????
                String totalAmount = stringMap.get("total_amount");
                String appId = stringMap.get("app_id");
                if (OrderStatusEnum.CREATE_NEW.getCode().equals(orderEntity.getStatus())
                        && payAmount.equals(totalAmount) && alipayTemplate.app_id.equals(appId)) {
                    updateOrderStatus(outTradeNo, payAmount, orderEntity, 1);
                    return "success";
                }
            }
            return "fail";
        } catch (Exception e) {
            return "fail";
        }
    }

    @Override
    @Transactional
    public String wxPayCallBack(PayResponse payResponse) {
        OrderEntity orderEntity = orderService.getOrderEntityByOrderSn(payResponse.getOrderId());
        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        if (orderEntity != null) {
            Integer status = orderEntity.getStatus();
            if (OrderStatusEnum.PAYED.getCode().equals(status)) {
                parameterMap.put("return_code", "SUCCESS");
                parameterMap.put("return_msg", "OK");
            }
            if (OrderStatusEnum.CREATE_NEW.getCode().equals(status)) {
                String payAmount = orderEntity.getPayAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString();//???????????????
                String orderAmount = payResponse.getOrderAmount().toString();
                if (payAmount.equals(orderAmount)) {
                    String orderSn = orderEntity.getOrderSn();
                    updateOrderStatus(orderSn, payAmount, orderEntity, 2);
                    parameterMap.put("return_code", "SUCCESS");
                    parameterMap.put("return_msg", "OK");
                }
            }
        } else {
            parameterMap.put("return_code", "FAIL");
        }
        return setRequestXml(parameterMap);
    }


    //??????xml??????
    public String setRequestXml(SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            sb.append("<" + key + ">" + "<![CDATA[" + value + "]]></" + key + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * ??????????????????
     *
     * @param outTradeNo
     * @param payAmount
     * @param orderEntity
     */
    public void updateOrderStatus(String outTradeNo, String payAmount, OrderEntity orderEntity, Integer payType) {
        OrderEntity update = new OrderEntity();
        update.setId(orderEntity.getId());
        update.setOrderSn(outTradeNo);
        update.setStatus(OrderStatusEnum.PAYED.getCode());
        update.setPayType(payType);
        orderDao.updateById(update);
        log.info("??????:{}????????????,?????????:{},????????????:{},????????????:{}", orderEntity.getMemberId(), outTradeNo, payAmount, payType == 1 ? "?????????" : "??????");
        /**
         * ??????????????????,????????????????????????????????????
         */
        OrderEntityPayedTO orderEntityPayedTO = new OrderEntityPayedTO();
        BeanUtils.copyProperties(update, orderEntityPayedTO);
        SendMessageRequest request = SendMessageRequest.builder().exchange(MQ_ORDER_EXCHANGE).object(orderEntityPayedTO)
                .routingKey(MQ_ORDERTOWARE_REDUCE_ROUTINGKEY).className(OrderEntityPayedTO.class.getName()).build();
        mqServiceAPI.sendMessage(request);
    }


    private void wareSkuLock(OrderSubmitResposeVO orderSubmitResposeVO, OrderCreateVO orderCreateVO) {
        WareSkuLockVO wareSkuLockVO = new WareSkuLockVO();
        OrderEntity orderEntity = orderCreateVO.getOrderEntity();
        BeanUtils.copyProperties(orderEntity, wareSkuLockVO);
        wareSkuLockVO.setOrderId(orderEntity.getId());
        wareSkuLockVO.setReceiverAddress(orderEntity.getReceiverProvince() + orderEntity.getReceiverCity() + orderEntity.getReceiverDetailAddress());
        List<SkuOrderItem> collect = orderCreateVO.getOrderItemEntities().stream().map(orderItemEntity -> {
            SkuOrderItem skuOrderItem = new SkuOrderItem();
            skuOrderItem.setNum(orderItemEntity.getSkuQuantity());
            skuOrderItem.setSkuId(orderItemEntity.getSkuId());
            skuOrderItem.setSkuName(orderItemEntity.getSkuName());
            return skuOrderItem;
        }).collect(Collectors.toList());
        wareSkuLockVO.setSkuOrderItems(collect);
        ResultBody booleanResultBody = wareServiceAPI.wareSkuLock(wareSkuLockVO);
        int code = booleanResultBody.getCode();
        if (code == 0) {
            //??????????????????
            orderSubmitResposeVO.setCode(0);
            orderSubmitResposeVO.setOrderEntity(orderEntity);
            //TODO,???????????????????????????,????????????,??????????????????,?????????????????????
            // int i=2/0;
            //??????????????????,???????????????,???????????????????????????(1??????),1????????????????????????,1?????????,?????????????????????????????????
            // ??????????????????????????????????????????,????????????????????????????????????????????????,??????????????????????????????
            OrderEntityReleaseTO orderEntityReleaseTO = new OrderEntityReleaseTO();
            BeanUtils.copyProperties(orderEntity, orderEntityReleaseTO);
            SendMessageRequest sendMessageRequest = SendMessageRequest.builder().exchange(MQ_ORDER_EXCHANGE).routingKey(MQ_ORDER_CREATE_ROUTINGKEY)
                    .object(orderEntityReleaseTO).className(orderEntityReleaseTO.getClass().getName()).build();
            mqServiceAPI.sendMessage(sendMessageRequest);
        } else {
            throw new RRException("????????????", 3);
        }
    }

    private void saveOrderSubmitResposeVOToDB(OrderCreateVO orderCreateVO) {
        //????????????
        OrderEntity orderEntity = orderCreateVO.getOrderEntity();
        orderEntity.setModifyTime(new Date());
        orderEntity.setCreateTime(new Date());
        orderService.save(orderEntity);
        orderItemService.saveBatch(orderCreateVO.getOrderItemEntities());

    }

    private OrderCreateVO createOrderCreateVO(OrderSubmitVO orderSubmitVO) {
        OrderCreateVO orderCreateVO = new OrderCreateVO();
        String orderSn = IdWorker.getIdStr();
        //??????????????????
        OrderEntity orderEntity = createOrderEntity(orderSubmitVO, orderSn);
        //?????????????????????
        List<OrderItemEntity> orderItemEntities = createOrderItemEntities(orderSn);
        orderCreateVO.setOrderItemEntities(orderItemEntities);
        //????????????,??????
        calculatePrice(orderEntity, orderItemEntities);
        orderCreateVO.setOrderEntity(orderEntity);
        return orderCreateVO;

    }

    private void calculatePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        BigDecimal total = new BigDecimal("0.0");
        //?????????
        BigDecimal coupon = new BigDecimal("0.0");
        BigDecimal intergration = new BigDecimal("0.0");
        BigDecimal promotion = new BigDecimal("0.0");
        //??????????????????
        Integer integrationTotal = 0;
        Integer growthTotal = 0;
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            //???????????????
            coupon = coupon.add(orderItemEntity.getCouponAmount());
            intergration = intergration.add(orderItemEntity.getIntegrationAmount());
            promotion = promotion.add(orderItemEntity.getPromotionAmount());
            //??????
            total = total.add(orderItemEntity.getRealAmount());
            //??????????????????????????????
            integrationTotal += orderItemEntity.getGiftIntegration();
            growthTotal += orderItemEntity.getGiftGrowth();
        }
        //1????????????????????????
        orderEntity.setTotalAmount(total);
        //??????????????????(??????+??????)
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        //orderEntity.setPayAmount(new BigDecimal("0.01"));
        orderEntity.setCouponAmount(coupon);
        orderEntity.setPromotionAmount(promotion);
        orderEntity.setIntegrationAmount(intergration);

        //???????????????????????????
        orderEntity.setIntegration(integrationTotal);
        orderEntity.setGrowth(growthTotal);

        //??????????????????(0-????????????1-?????????)
        orderEntity.setDeleteStatus(0);
    }

    /**
     * ?????????????????????
     *
     * @param
     * @param orderSn
     * @return
     */
    private List<OrderItemEntity> createOrderItemEntities(String orderSn) {
        MemberVO memberVO = LoginInterceptor.getMemberVO();
        ResultBody<List<CartItemVO>> listByUid = cartServiceApi.getCartListByUid(String.valueOf(memberVO.getUserId()));
        List<CartItemVO> itemVOList = listByUid.getData();
        Map<Long, Integer> skuCountMap = itemVOList.stream().collect(Collectors.toMap(CartItemVO::getSkuId, CartItemVO::getSkuCount));
        List<Long> skuIds = itemVOList.stream().map(itemVO -> itemVO.getSkuId()).collect(Collectors.toList());
        //?????????????????????????????????,????????????
        List<OrderItemVO> orderItemVOS = productServiceAPI.getOrderItemsBySkuIds(skuIds);
        List<OrderItemEntity> collect = orderItemVOS.stream().map(orderItemVO -> {
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            BeanUtils.copyProperties(orderItemVO, orderItemEntity);
            orderItemEntity.setOrderSn(orderSn);
            orderItemEntity.setSkuQuantity(skuCountMap.get(orderItemVO.getSkuId()));
            //??????,??????*???????????????
            BigDecimal gif = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity()));
            BigDecimal bigDecimal = gif.setScale(0, BigDecimal.ROUND_DOWN);
            orderItemEntity.setGiftIntegration(Integer.valueOf(bigDecimal.toString()));
            orderItemEntity.setGiftGrowth(Integer.valueOf(bigDecimal.toString()));
            orderItemEntity.setPromotionAmount(BigDecimal.ZERO);
            orderItemEntity.setCouponAmount(BigDecimal.ZERO);
            orderItemEntity.setIntegrationAmount(BigDecimal.ZERO);
            BigDecimal realAmount = orderItemEntity.getSkuPrice().
                    multiply(new BigDecimal(orderItemEntity.getSkuQuantity())).
                    subtract(orderItemEntity.getCouponAmount()).
                    subtract(orderItemEntity.getIntegrationAmount()).
                    subtract(orderItemEntity.getPromotionAmount());
            orderItemEntity.setRealAmount(realAmount);
            return orderItemEntity;

        }).collect(Collectors.toList());

        return collect;
    }

    /**
     * ??????????????????
     *
     * @param orderSubmitVO
     * @return
     */
    private OrderEntity createOrderEntity(OrderSubmitVO orderSubmitVO, String orderSn) {
        MemberVO memberVO = LoginInterceptor.getMemberVO();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(orderSn);//?????????
        orderEntity.setMemberId(memberVO.getUserId());
        orderEntity.setMemberUsername(memberVO.getNickname());
        orderEntity.setSourceType(0);//????????????[0->PC?????????1->app??????]
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());//?????????
        orderEntity.setAutoConfirmDay(14);
        MemberReceiveAddressVO memberReceiveAddressVO = memberServiceAPI.getMemberreceiveaddressById(orderSubmitVO.getAddressId()).getData();
        orderEntity.setReceiverName(memberReceiveAddressVO.getName());
        orderEntity.setReceiverPhone(memberReceiveAddressVO.getPhone());
        orderEntity.setReceiverPostCode(memberReceiveAddressVO.getPostCode());
        orderEntity.setReceiverProvince(memberReceiveAddressVO.getProvince());
        orderEntity.setReceiverCity(memberReceiveAddressVO.getCity());
        orderEntity.setReceiverRegion(memberReceiveAddressVO.getRegion());
        orderEntity.setReceiverDetailAddress(memberReceiveAddressVO.getDetailAddress());
        orderEntity.setConfirmStatus(0);
        BigDecimal freightMoney = getFreightMoneyByAddressId(orderSubmitVO.getAddressId());//??????
        orderEntity.setFreightAmount(freightMoney);
        return orderEntity;
    }

    @RabbitHandler
    public void receiveMessageOrderEntity(Message message, OrderEntity order, Channel channel) throws InterruptedException {


        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //??????ack,??????????????????
            log.info("order:{},channel{},deliveryTag:{}", order, channel, deliveryTag);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //@RabbitListener(queues = "hello.java.queue")
    public void receiveMessageOrderItemEntity(Message message, OrderItemEntity orderItemEntity, Channel channel) throws InterruptedException {


        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("orderItemEntity:{},channel{},deliveryTag:{}", orderItemEntity, channel, deliveryTag);
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}