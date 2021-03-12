package com.iflytek.gulimall.order.service.impl;

import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.request.*;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.iflytek.gulimall.common.exception.RRException;
import com.iflytek.gulimall.order.service.PayService;


import com.iflytek.gulimall.order.vo.combine.QueryOrderRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 业务实现类
 * 支付通知结果
 * </p>
 *
 * @author vivi.huang
 * @date 2020-05-17
 */
@Slf4j
@Service
@AllArgsConstructor
public class PayServiceImpl implements PayService {
    @Autowired
    private Environment environment;
    @Autowired
    private WxPayService wxService;

//    private CombinePayService weChatPayService;

    @Override
    public <T> T unifiedOrder(WxPayUnifiedOrderRequest request) {
        try {
            return wxService.createOrder(request);
        } catch (WxPayException e) {
            log.error("统一下单异常，单号:[{}]", request.getOutTradeNo(), e);
            throw new RRException("微信统一下单支付异常");
        }
    }

    @Override
    public WxPayOrderNotifyResult parseOrderNotifyResult(String xmlData) {
        try {
            return wxService.parseOrderNotifyResult(xmlData);
        } catch (WxPayException e) {
            log.error("支付结果通知异常，内容:[{}]", xmlData, e);
            throw new RRException("支付结果通知异常");
        }
    }

    @Override
    public WxPayRefundResult refund(WxPayRefundRequest request) {
        request.setNotifyUrl(environment.getProperty("ceres.wx.ma.refund-notify-url"));
        try {
            return wxService.refund(request);
        } catch (WxPayException e) {
            log.error("申请退款异常, 单号:[{}]", request.getOutTradeNo(), e);
            throw new RRException("申请退款异常");
        }
    }

    @Override
    public WxPayRefundQueryResult refundQuery(WxPayRefundQueryRequest request) {
        try {
            return wxService.refundQuery(request);
        } catch (WxPayException e) {
            log.error("查询退款异常,单号:[{}]", request.getOutTradeNo(), e);
            throw new RRException("查询退款异常");
        }
    }

    @Override
    public WxPayOrderQueryResult queryOrder(WxPayOrderQueryRequest request) {
        try {
            return wxService.queryOrder(request);
        } catch (WxPayException e) {
            log.error("查询支付异常,单号:[{}]", request.getOutTradeNo(), e);
            throw new RRException("查询支付异常");
        }
    }

    @Override
    public WxPayRefundNotifyResult parseRefundNotifyResult(String xmlData) {
        try {
            return wxService.parseRefundNotifyResult(xmlData);
        } catch (WxPayException e) {
            log.error("查询退款异常,内容:[{}]", xmlData, e);
            throw new RRException("查询退款异常");
        }
    }

    @Override
    public WxPayOrderCloseResult closeOrder(WxPayOrderCloseRequest request) {
        try {
            return wxService.closeOrder(request);
        } catch (WxPayException e) {
            log.error("关闭支付异常, 单号:[{}]", request.getOutTradeNo(), e);
            throw new RRException("关闭支付异常");
        }
    }

//    @Override
//    public CombineOrderResult combineCreateOrder(CombineOrderRequest request) {
//        return weChatPayService.createCombineOrder(request);
//    }
//
//    @Override
//    public QueryOrderResult combineQueryOrder(QueryOrderRequest request) {
//        return weChatPayService.queryCombineOrder(request);
//    }
//
//    @Override
//    public CloseOrderResult combineCloseOrder(CloseOrderRequest request) {
//        return weChatPayService.closeCombineOrder(request);
//    }
//
//    @Override
//    public NoticeOrderResult combineCloseOrder(NoticeOrderRequest request) {
//        return weChatPayService.noticeOrder(request);
//    }
}
