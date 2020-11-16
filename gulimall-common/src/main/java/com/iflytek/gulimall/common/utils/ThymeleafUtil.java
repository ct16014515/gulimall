package com.iflytek.gulimall.common.utils;

import java.math.BigDecimal;

/**
 * thymeleaf模板引擎spel表达式调用的java静态方法
 * https://blog.csdn.net/gdjlc/article/details/102595423
 */
public class ThymeleafUtil {

    /**
     * decimal保留两位小数
     * @param decimal
     * @return
     */
    public static BigDecimal decimalFormate(BigDecimal decimal) {
        return decimal.setScale(2);
    }

}
