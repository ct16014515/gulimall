package com.iflytek.gulimall.order.vo;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * <p>
 *   合并查询结果
 * </p>
 *
 * @author vivi.huang
 * @since 2020/5/17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor
@Accessors(chain = true)
public class NoticeOrderResult extends BasePayResult {

    private static final long serialVersionUID = -217253558986070692L;

    public NoticeOrderResult(String content) {
    }
}