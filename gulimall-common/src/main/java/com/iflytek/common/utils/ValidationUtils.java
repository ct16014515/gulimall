package com.iflytek.common.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JSR303数据校验工具类
 */
public class ValidationUtils {

    /**
     * 有错误的情况下校验
     * @param result
     * @return
     */
    public static Map<String, String> validationErrors(BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        Map<String, String> errors = fieldErrors.stream().
                collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
        return errors;
    }

}
