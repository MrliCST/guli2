package com.atlearn.guli.exception;

import org.dromara.common.core.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局异常处理器
 *
 * @author mayao
 * @date 2026-08-03
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.atlearn.guli.controller")
public class GuliExceptionHandler {

    /**
     * 处理 @Validated 校验失败
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();  // 字段校验错误
        Map<String, String> errors = new HashMap<>(fieldErrors.size());
        for (FieldError err : fieldErrors) {
            errors.put(err.getField(), err.getDefaultMessage());
        }
        log.warn("参数校验失败: {}", errors);
        return R.fail(ErrorCodeEnum.VALIDATION_FAILED.getCode(), ErrorCodeEnum.VALIDATION_FAILED.getMsg(), errors);
    }

    /**
     * 兜底：未知错误
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("未知错误", e);
        return R.fail(ErrorCodeEnum.UNKNOWN_ERROR.getCode(), ErrorCodeEnum.UNKNOWN_ERROR.getMsg());
    }
}
