package com.iflytek.gulimall.coupon.controller;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;


import com.iflytek.gulimall.common.exception.GulimallExceptinCodeEnum;
import com.iflytek.gulimall.common.feign.CouponServiceAPI;
import com.iflytek.gulimall.common.utils.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.iflytek.gulimall.coupon.entity.SeckillSessionEntity;
import com.iflytek.gulimall.coupon.service.SeckillSessionService;
import com.iflytek.gulimall.common.utils.PageUtils;
import com.iflytek.gulimall.common.utils.R;


/**
 * 秒杀活动场次
 *
 * @author rclin
 * @email rclin@iflytek.com
 * @date 2020-10-19 16:40:01
 */
@RestController
@RequestMapping("coupon/seckillsession")
public class SeckillSessionController {
    @Autowired
    private SeckillSessionService seckillSessionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    // @RequiresPermissions("coupon:seckillsession:list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = seckillSessionService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    // @RequiresPermissions("coupon:seckillsession:info")
    public R info(@PathVariable("id") Long id) {
        SeckillSessionEntity seckillSession = seckillSessionService.getById(id);

        return R.ok().put("seckillSession", seckillSession);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    // @RequiresPermissions("coupon:seckillsession:save")
    public R save(@RequestBody SeckillSessionEntity seckillSession) {
        R result = timeCheck(seckillSession);
        if (!"success".equals(result.get("msg"))) {
            return result;
        }
        seckillSessionService.save(seckillSession);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    // @RequiresPermissions("coupon:seckillsession:update")
    public R update(@RequestBody SeckillSessionEntity seckillSession) {
        R result = timeCheck(seckillSession);
        if (!"success".equals(result.get("msg"))) {
            return result;
        }
        seckillSessionService.updateById(seckillSession);
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    // @RequiresPermissions("coupon:seckillsession:delete")
    public R delete(@RequestBody Long[] ids) {
        seckillSessionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    R timeCheck(SeckillSessionEntity seckillSessionEntity) {
        LocalDateTime nowTime = LocalDateTime.now();
        if (seckillSessionEntity.getStartTime().isBefore(nowTime)) {
            return R.error("开始时间要大于当前时间");
        }
        if (seckillSessionEntity.getEndTime().isBefore(nowTime)) {
            return R.error("结束时间要大于当前时间");
        }
        if (seckillSessionEntity.getStartTime().isAfter(seckillSessionEntity.getEndTime())) {
            return R.error("结束时间要大于开始时间");
        }
        return R.ok();
    }


}
