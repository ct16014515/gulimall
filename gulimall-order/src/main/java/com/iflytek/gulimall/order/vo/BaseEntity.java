package com.iflytek.gulimall.order.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author vivi.huang
 * @since 2020/6/18
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 6538105338796911177L;

    /**
     * 公众账号ID	appid	string（32）	是	query 微信分配的公众账号ID
     * 示例值：wx8888888888888888
     */
    @JSONField(name = "appid")
    protected String appId;

    /**
     * 二级商户号	sub_mchid	string(32)	是	补差的电商平台二级商户，填写微信支付分配的商户号。
     * 示例值：1900000109
     */
    @JSONField(name = "sub_mchid")
    protected String subMchid;

    /**
     * 微信订单号	transaction_id	string(64)	是	微信支付订单号。
     * 示例值： 4208450740201411110007820472
     */
    @JSONField(name = "transaction_id")
    protected String transactionId;

    /**
     * 商户分账单号	out_order_no	string(64)	是	商户系统内部的分账单号，在商户系统内部唯一（单次分账、多次分账、完结分账应使用不同的商户分账单号），同一分账单号多次请求等同一次。
     * 示例值：P20150806125346
     */
    @JSONField(name = "out_order_no")
    protected String out_order_no;

    /**
     * 微信分账单号	order_id	string(64)	是	微信分账单号，微信系统返回的唯一标识。
     * 示例值： 6754760740201411110007865434
     */
    @JSONField(name = "order_id")
    protected String orderId;

    /**
     * 商户回退单号	out_return_no	string(64)	是	query此回退单号是商户在自己后台生成的一个新的回退单号，在商户后台唯一。
     * 示例值：R20190516001
     */
    @JSONField(name = "out_return_no")
    protected String outReturnNo;

    /**
     * 回退商户号	return_mchid	string(32)	是	query只能对原分账请求中成功分给商户接收方进行回退。
     * 示例值：86693852
     */
    @JSONField(name = "return_mchid")
    protected String returnMchid;

    /**
     * 金额	amount	int 单位为分
     * 示例值：10
     */
    @JSONField(name = "amount")
    protected Long amount;

    /**
     * 描述
     */
    @JSONField(name = "description")
    protected String description;

    /**
     * 微信回退单号	return_no	string(64)	是	微信分账回退单号，微信系统返回的唯一标识。
     * 示例值： 3008450740201411110007820472
     */
    @JSONField(name = "return_no")
    protected String returnNo;

    /**
     * result	string(32)	是	如果请求返回为处理中，则商户可以通过调用回退结果查询接口获取请求的最终处理结果，枚举值：
     * PROCESSING：处理中
     * SUCCESS：已成功
     * FAIL：已失败
     *
     * 注意：如果返回为处理中，请勿变更商户回退单号，使用相同的参数再次发起分账回退，否则会出现资金风险 在处理中状态的回退单如果5天没有成功，会因为超时被设置为已失败
     * 示例值：SUCCESS
     */
    @JSONField(name = "result")
    protected String result;

    /**
     * 失败原因	fail_reason	string(32)	否	回退失败的原因，此字段仅回退结果为FAIL时存在，枚举值：
     * ACCOUNT_ABNORMAL：分账接收方账户异常
     * TIME_OUT_CLOSED:：超时关单
     * 示例值：TIME_OUT_CLOSED
     */
    @JSONField(name = "fail_reason")
    protected String failReason;

    /**
     * 完成时间	finish_time	string(64)	是	分账回退完成时间，遵循rfc3339标准格式
     * 格式为YYYY-MM-DDTHH:mm:ss:sss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss:sss表示时分秒毫秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35.120+08:00表示，北京时间2015年5月20日 13点29分35秒。
     * 示例值：2015-05-20T13:29:35.120+08:00
     */
    @JSONField(name = "finish_time")
    protected String finishTime;

    /**
     * 接收方类型	type	string（32）	是	query 分账接收方的类型，枚举值：
     * MERCHANT_ID：商户
     * PERSONAL_OPENID：个人
     * 示例值：MERCHANT_ID
     */
    @JSONField(name = "type")
    protected String type;

    /**
     * 接收方账号	account	string（64）	是	分账接收方的账号
     * 类型是MERCHANT_ID时，是商户号
     * 类型是PERSONAL_OPENID时，是个人openid
     * 示例值：190001001
     */
    @JSONField(name = "account")
    protected String account;

    /**
     * 电商平台APPID	sp_appid	string(32)	是	query电商平台在微信公众平台申请服务号对应的APPID，申请商户功能的时候微信支付会配置绑定关系。
     * 示例值：wx8888888888888888
     */
    @JSONField(name = "sp_appid")
    protected String spAppid;

    /**
     * 二级商户APPID	sub_appid	string(32)	否	query二级商户在微信申请公众号成功后分配的帐号ID，需要电商平台侧配置绑定关系才能传参。
     * 示例值：wx8888888888888888
     */
    @JSONField(name = "sub_appid")
    protected String subAppid;

    /**
     * 商户退款单号	out_refund_no	string(64)	是	query商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@，同一退款单号多次请求只退一笔。
     * 示例值：1217752501201407033233368018
     */
    @JSONField(name = "out_refund_no")
    protected String outRefundNo;

    /**
     * create_time	string(64)	是	1、退款受理时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日13点29分35秒。
     * 2、当退款状态为退款成功时返回此字段。
     * 示例值：2018-06-08T10:34:56+08:00
     */
    @JSONField(name = "create_time")
    protected String createTime;

    /**
     * 提现状态更新时间	update_time	string(32)	否
     * 提现状态更新时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss:sss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss:sss表示时分秒毫秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日13点29分35秒。
     * 示例值： 2015-05-20T13:29:35.120+08:00
     */
    @JSONField(name = "update_time")
    protected String updateTime;

    /**
     * 通知ID	id	string(32)	是	通知的唯一ID
     * 示例值：EV-2018022511223320873
     */
    @JSONField(name = "id")
    protected String id;

    /**
     * 日期	date	string（10）	是	指定查询商户日终余额的日期
     * 示例值：2019-08-17
     */
    @JSONField(name = "date")
    protected String date;

    /**
     * 备注	remark	string(56)	否	query商户对提现单的备注
     * 示例值：交易提现
     */
    @JSONField(name = "remark")
    protected String remark;

    /**
     * 商户提现单号	out_request_no	string(32)	否	商户提现单号
     * 示例值： 20190611222222222200000000012122
     */
    @JSONField(name = "out_request_no")
    protected String outRequestNo;

    /**
     * 电商平台商户号	sp_mchid	string(32)	是	电商平台商户号
     * 示例值： 1800000123
     */
    @JSONField(name = "sp_mchid")
    protected String spMchid;

    /**
     * 银行附言	bank_memo	string(32)	否	query展示在收款银行系统中的附言，数字、字母最长32个汉字（能否成功展示依赖银行系统支持）。
     * 示例值：微信支付提现
     */
    @JSONField(name = "bank_memo")
    protected String bankMemo;


}
