package com.iflytek.gulimall.order.web;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.iflytek.gulimall.common.model.enume.OrderPayTypeEnum;
import com.iflytek.gulimall.order.config.AlipayTemplate;
import com.iflytek.gulimall.order.config.WxAccountConfig;
import com.iflytek.gulimall.order.entity.OrderEntity;
import com.iflytek.gulimall.order.service.OrderService;
import com.iflytek.gulimall.order.vo.PayVO;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.lly835.bestpay.enums.BestPayTypeEnum.WXPAY_NATIVE;

@Controller
@Slf4j
public class PayWebController {

    @Autowired
    AlipayTemplate alipayTemplate;

    @Autowired
    OrderService orderService;


    @Autowired
    private BestPayService bestPayService;

    @Autowired
    private WxAccountConfig wxAccountConfig;

    /**
     * 支付宝同步跳转页面
     *
     * @param orderSn
     * @return
     * @throws AlipayApiException
     */
    @GetMapping(value = "/aliPayOrder", produces = "text/html")
    @ResponseBody
    public String toPay(@RequestParam("orderSn") String orderSn) throws AlipayApiException {
        PayVO payVo = orderService.getPayVOByOrderSn(orderSn);
        String pay = alipayTemplate.pay(payVo);
        return pay;
    }

    /**
     * 支付宝回调
     *
     * @param request
     * @return
     * @throws AlipayApiException
     */
    @PostMapping("order/aliPayNotifyUrl")
    @ResponseBody
    public String aliPayNotifyUrl(HttpServletRequest request) throws AlipayApiException {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        //验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayTemplate.alipay_public_key, alipayTemplate.charset, alipayTemplate.sign_type); //调用SDK验证签名
        if (signVerified) {
            String result = orderService.aliPayCallBack(params);
            return result;
        } else {
            return "fail";
        }
    }


    /**
     * 微信支付
     *
     * @param orderSn
     * @return
     */
    @GetMapping(value = "/weixiPayOrder")
    public String weixinPayOrder(@RequestParam("orderSn") String orderSn, Model model) {
        OrderEntity orderInfo = orderService.getOrderEntityByOrderSn(orderSn);
        if (orderInfo == null) {
            throw new RuntimeException("订单不存在");
        }
        String skuName = orderService.getSkuNameByOrderSn(orderSn);
        skuName = skuName.replaceAll("[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】'；：\"\"。， 、？]", "");
        PayRequest request = new PayRequest();
        request.setOrderName(skuName);
        request.setOrderId(orderInfo.getOrderSn());
        request.setOrderAmount(new Double(orderInfo.getPayAmount().toString()));
        request.setPayTypeEnum(WXPAY_NATIVE);
        PayResponse payResponse = bestPayService.pay(request);
        //传入前台的二维码路径生成支付二维码
        model.addAttribute("codeUrl", payResponse.getCodeUrl());
        model.addAttribute("orderSn", orderInfo.getOrderSn());
        model.addAttribute("returnUrl", wxAccountConfig.getReturnUrl());
        /**
         * 修改订单的支付方式为微信
         */
        orderService.updatePayTypeByOrderId(orderInfo.getId(), OrderPayTypeEnum.WX.getCode());
        return "createForWxNative";
    }

    /**
     * 微信回调
     *
     * @param
     * @return
     * @throws
     */
    @PostMapping("order/wxPayNotifyUrl")
    @ResponseBody
    public String wxPayNotifyUrl(@RequestBody String notifyData) {
        //签名效验
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("payResponse={}", payResponse);
        String result = orderService.wxPayCallBack(payResponse);
        return result;
    }

}
