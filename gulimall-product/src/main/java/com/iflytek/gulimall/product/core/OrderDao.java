package com.iflytek.gulimall.product.core;

import com.iflytek.gulimall.product.core.entity.OrderEntity;

import java.util.List;

public interface OrderDao {



    public List<OrderEntity> selectAll();



    public void update();

}
