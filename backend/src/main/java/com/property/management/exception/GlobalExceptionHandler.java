package com.property.management.exception;

import com.property.management.dto.ResponseResult;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(BindException.class)
    public ResponseResult<String> handleBindException(BindException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseResult.error(400, "参数验证失败: " + errors.toString());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseResult.error(400, "参数验证失败: " + errors.toString());
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseResult<String> handleException(Exception e) {
        return ResponseResult.error(500, "系统内部错误: " + e.getMessage());
    }
}