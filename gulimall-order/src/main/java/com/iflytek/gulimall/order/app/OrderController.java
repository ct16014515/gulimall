package com.iflytek.gulimall.order.app;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;


import com.iflytek.common.model.vo.order.OrderEntityVO;
import com.iflytek.common.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.iflytek.gulimall.order.entity.OrderEntity;
import com.iflytek.gulimall.order.service.OrderService;
import com.iflytek.common.utils.PageUtils;
import com.iflytek.common.utils.R;


/**
 * 订单
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-06-07 01:03:35
 */
@RestController
@RequestMapping("order/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("order:order:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = orderService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("order:order:info")
    public R info(@PathVariable("id") Long id) {
        OrderEntity order = orderService.getById(id);
        return R.ok().put("order", order);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("order:order:save")
    public R save(@RequestBody OrderEntity order) {
        orderService.save(order);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("order:order:update")
    public R update(@RequestBody OrderEntity order) {
        orderService.updateById(order);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("order:order:delete")
    public R delete(@RequestBody Long[] ids) {
        orderService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    @GetMapping("/getFreightMoneyByAddressId/{addressId}")
    public ResultBody getFreightMoneyByAddressId(@PathVariable("addressId") Long addressId) {
        BigDecimal bigDecimal = orderService.getFreightMoneyByAddressId(addressId);
        return new ResultBody(bigDecimal);
    }

    @GetMapping("/getOrderEntityByOrderSn/{orderSn}")
    public ResultBody<OrderEntity> getOrderEntityByOrderSn(@PathVariable("orderSn") String orderSn) {
        ResultBody<OrderEntity> resultBody=  orderService.getOrderEntityByOrderSn(orderSn);
        return  resultBody;
    }


}
