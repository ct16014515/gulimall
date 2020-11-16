package com.iflytek.gulimall.product.exception;

import com.iflytek.gulimall.common.exception.GulimallExceptinCodeEnum;
import com.iflytek.gulimall.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice(basePackages = "com.iflytek.gulimall.product.controller")
public class GulimallExceptionControllerAdvice {
    /**
     * 拦截数据校验出现的异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleVaildateException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String, String> map = new HashMap<>();
        fieldErrors.stream().forEach(item -> {
            map.put(item.getField(), item.getDefaultMessage());
        });
        return R.error(GulimallExceptinCodeEnum.VALIDATE_EXCEPTION).put("data",map);
    }


    @ExceptionHandler(Throwable.class)
    public R handleException(Throwable e) {
         log.error(e.getMessage());
        return R.error(GulimallExceptinCodeEnum.UNKNOWN_EXCEPTION);
    }



}
