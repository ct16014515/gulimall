package com.iflytek.gulimall.order.vo.combine;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *   支付者信息
 * </p>
 *
 * @author vivi.huang
 * @since 2020/5/17
 */
@Data
@EqualsAndHashCode()
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CombinePayerInfo implements Serializable {

    private static final long serialVersionUID = -5846774443653689568L;

    /**
     *  用户标识
     *  是	使用合单appid获取的对应用户openid。是用户在商户appid下的唯一标识。
     *  示例值：oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
     */
    @JSONField(name = "openid")
    private String openId;
}