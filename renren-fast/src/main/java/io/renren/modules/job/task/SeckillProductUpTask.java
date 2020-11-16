package io.renren.modules.job.task;

import com.iflytek.gulimall.common.feign.CouponServiceAPI;
import com.iflytek.gulimall.common.feign.MqServiceAPI;
import com.iflytek.gulimall.common.feign.vo.SendMessageRequest;
import com.iflytek.gulimall.common.utils.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.iflytek.gulimall.common.constant.MqConstant.MQ_JOB_EXCHANGE;
import static com.iflytek.gulimall.common.constant.MqConstant.MQ_JOB_SECKILLPRODUCTUP_ROUTINGKEY;

@Component("seckillProductUp")
@Slf4j
public class SeckillProductUpTask implements ITask {

    @Autowired
    CouponServiceAPI couponServiceAPI;

    @Autowired
    MqServiceAPI mqServiceAPI;

    /**
     * 如果远程调用时间比较长,需要用队列防止出现超时
     * @param params   参数，多参数使用JSON数据
     */
    @Override
    public void run(String params) {
        SendMessageRequest request = SendMessageRequest.builder().className("").exchange(MQ_JOB_EXCHANGE).
                object(params).routingKey(MQ_JOB_SECKILLPRODUCTUP_ROUTINGKEY).build();
        mqServiceAPI.sendMessage(request);
    }
}
