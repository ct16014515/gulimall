package com.iflytek.gulimall.common.exception;


import com.iflytek.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = RRException.class)
    @ResponseBody
    public R coBusinessExceptionHandler(HttpServletRequest req, RRException e) {
        log.error("", e);
        int code = e.getCode();
        String msg = e.getMsg();
        return R.error(code, msg);
    }

    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("", e);
        return R.error(GulimallExceptinCodeEnum.SYS_ERROR);
    }
}
